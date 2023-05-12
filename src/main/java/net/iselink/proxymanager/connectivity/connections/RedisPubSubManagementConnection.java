package net.iselink.proxymanager.connectivity.connections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.messages.Message;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.connectivity.messages.requests.*;
import net.iselink.proxymanager.utils.IDisposable;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Management connection implemented via redis's channels.
 */
public class RedisPubSubManagementConnection extends ManagementConnection implements IDisposable {

	/**
	 * Name of the channel which is used to communication between proxies.
	 **/
	private static final String CHANNEL_NAME = "PROXY_COMMAND";

	private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private final ScheduledTask txTask;
	private final ScheduledTask recvTask;
	private JedisPool pool;

	private final Runnable transmissionRunnable = () -> {
		try (Jedis jedis = pool.getResource()) {
			if (jedis.isConnected()) {
				jedis.connect();
			}

			while (true) {
				Message msg = getTransmisitonQueue().poll();
				if (msg == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						return;
					}
					continue;
				}

				String payload = msg.selfSerialize();
				jedis.publish(CHANNEL_NAME, payload);
			}
		}
	};

	private final Runnable recvRunnable = () -> {
		try (Jedis jedis = pool.getResource()) {
			if (!jedis.isConnected())
				jedis.connect();

			jedis.subscribe(new JedisPubSub() {
				@Override
				public void onMessage(String channel, String message) {
					if (!channel.contentEquals(CHANNEL_NAME))
						return;
					try {
						TempMessageTypeClass base = gson.fromJson(message, TempMessageTypeClass.class);

						MessageType type = base.getType();
						if (type == null) {
							getProxyManager().getLogger().info("Received invalid message on proxy command channel.");
							getProxyManager().getLogger().info(message);
							return;
						}

						Class<? extends Message> msgClaszz = type.getAssociatedClass();
						Message deseralizedMessage = gson.fromJson(message, msgClaszz);
						getReceivingQueue().add(deseralizedMessage);
					} catch (Exception ex) {
						getProxyManager().getLogger().warning("Exception occurred while handling proxy command message.");
						getProxyManager().getLogger().warning(ex.getMessage());
					}

				}
			}, CHANNEL_NAME);
		}
	};

	public RedisPubSubManagementConnection(ProxyManagerPlugin proxyManagerPlugin, JedisPool redisConnection) {
		super(proxyManagerPlugin);
		pool = redisConnection;

		txTask = proxyManagerPlugin.getProxy().getScheduler().runAsync(proxyManagerPlugin, transmissionRunnable);
		recvTask = proxyManagerPlugin.getProxy().getScheduler().runAsync(proxyManagerPlugin, recvRunnable);
	}

	@Override
	public void broadcast(String text, String playerName, boolean showPlayerName) {
		getTransmisitonQueue().add(new MessageBroadcastRequest(text, playerName, showPlayerName));

	}


	@Override
	public void requestPlayerKick(String player, String reason, String sender) {
		getTransmisitonQueue().add(new PlayerKickRequest(player, reason, sender));

	}

	@Override
	public void pingProxies(String sender) {
		getTransmisitonQueue().add(new ProxyPingRequest(sender, getProxyManager().getProxy().getName()));
	}

	@Override
	public void transmitMessage(Message message) {
		getTransmisitonQueue().add(message);

	}

	@Override
	public void kickAll(String executer, String reason) {
		getTransmisitonQueue().add(new KickAllRequest(executer, reason));

	}

	@Override
	public void whereIsPlayer(String playerName, String commandSenderName) {
		getTransmisitonQueue().add(new WhereIsPlayerRequest(playerName, commandSenderName));
	}

	private static class TempMessageTypeClass {
		@Expose
		@SerializedName("message_type")
		private MessageType type;

		public MessageType getType() {
			return type;
		}
	}

	@Override
	public void playerLoginEvent(ProxiedPlayer player) {
		String playerAddress = null;
		SocketAddress sockAdr = player.getSocketAddress();
		if (sockAdr instanceof InetSocketAddress) {
			InetAddress a = ((InetSocketAddress) sockAdr).getAddress();
			if (a != null)
				playerAddress = a.getHostAddress();
		}
		getTransmisitonQueue().add(new PlayerJoinBroadcastRequest(player.getName(), getProxyManager().getConfiguration().getUuid(), playerAddress));
	}

	@Override
	public void dispose() {
		txTask.cancel();
		recvTask.cancel();
		pool.close();
	}
}

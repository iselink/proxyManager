package net.iselink.proxymanager.connectivity.connections;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.messages.Message;
import net.iselink.proxymanager.connectivity.messages.requests.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple loopback-ish dummy connection for early development.
 */
public class DummyManagementConnection extends ManagementConnection {

	private static final Logger logger = LoggerFactory.getLogger(DummyManagementConnection.class);
	private final Thread copyThread;

	public DummyManagementConnection(ProxyManagerPlugin proxyManagerPlugin) {
		super(proxyManagerPlugin);
		//not great, not recommended and discouraged
		//who care if it just works?
		copyThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				Message msg = getTransmisitonQueue().poll();
				if (msg != null) {
					getReceivingQueue().add(msg);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
			}
		});
		copyThread.setDaemon(true);
		copyThread.start();
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

	@Override
	public void playerLoginEvent(ProxiedPlayer player) {
		//do nothing
		//forwarding this event into queue will kick player out, in current implementation
	}
}

package net.iselink.proxymanager;

import net.iselink.proxymanager.events.BroadcastEvent;
import net.iselink.proxymanager.events.ProxyPingResponseEvent;
import net.iselink.proxymanager.utils.MessageBuilder;
import net.iselink.proxymanager.utils.RedisPlayerInformation;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.Jedis;

import java.util.logging.Logger;

/**
 * Class for event handlers.
 */
public class PluginEventListener implements Listener {
	private final ProxyManagerPlugin proxyManagerPlugin;
	private final ProxyServer proxy;
	private final Logger logger;

	public PluginEventListener(ProxyManagerPlugin proxyManagerPlugin) {
		this.proxyManagerPlugin = proxyManagerPlugin;
		proxyManagerPlugin.getProxy().getPluginManager().registerListener(proxyManagerPlugin, this);
		proxy = proxyManagerPlugin.getProxy();
		logger = proxyManagerPlugin.getProxy().getLogger();
	}

	@EventHandler
	public void onBroadcast(BroadcastEvent event) {
		logger.info(event.getMessage());
		proxy.broadcast(event.createMessage());
	}

	@EventHandler
	public void onPlayerPreJoin(PostLoginEvent event) {
		//if redis sync is enabled, check if player is set
		//otherwise send login event
		if (proxyManagerPlugin.getConfiguration().isRedisSyncActivePlayer()) {
			try (Jedis jedis = proxyManagerPlugin.getRedisConnection().getResource()) {
				boolean exist = jedis.exists(event.getPlayer().getName());
				if (exist) {
					event.getPlayer().disconnect(new MessageBuilder().addText("You are already connected from different proxy.").toArray());
				} else {
					jedis.setex(event.getPlayer().getName(), 60 * 2, new RedisPlayerInformation(event.getPlayer()).toJson());    //TODO: hardcoded expiry time
				}
			}

		} else {
			proxyManagerPlugin.getManagementConnection().playerLoginEvent(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		if (proxyManagerPlugin.getConfiguration().isRedisSyncActivePlayer()) {
			//remove player from redis
			try (Jedis jedis = proxyManagerPlugin.getRedisConnection().getResource()) {
				jedis.del(event.getPlayer().getName());
			}
		}
	}

	@EventHandler
	public void onPlayerServerReconnect(ServerSwitchEvent event) {
		if (proxyManagerPlugin.getConfiguration().isRedisSyncActivePlayer()) {
			//remove player from redis
			try (Jedis jedis = proxyManagerPlugin.getRedisConnection().getResource()) {
				jedis.setex(event.getPlayer().getName(), 60 * 2, new RedisPlayerInformation(event.getPlayer()).toJson());//TODO: hardcoded expiry time
			}
		}
	}

	@EventHandler
	public void onProxyPingResponse(ProxyPingResponseEvent e) {
		if (e.getCommandSender() == null) {
			logger.info(String.format("Proxy ping response from %s (%d online).", e.getName(), e.getPlayerCount()));
			return;
		}

		ProxiedPlayer player = proxy.getPlayer(e.getCommandSender());
		player.sendMessage(new TextComponent(String.format("Proxy ping response from %s (%d online).", e.getName(), e.getPlayerCount())));
	}

}

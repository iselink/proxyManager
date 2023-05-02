package net.iselink.proxymanager;

import net.iselink.proxymanager.events.BroadcastEvent;
import net.iselink.proxymanager.events.ProxyPingResponseEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
	public void onProxyPingResponse(ProxyPingResponseEvent e) {
		if (e.getCommandSender() == null) {
			logger.info(String.format("Proxy ping response from %s (%d online).", e.getName(), e.getPlayerCount()));
			return;
		}

		ProxiedPlayer player = proxy.getPlayer(e.getCommandSender());
		player.sendMessage(new TextComponent(String.format("Proxy ping response from %s (%d online).", e.getName(), e.getPlayerCount())));
	}

}

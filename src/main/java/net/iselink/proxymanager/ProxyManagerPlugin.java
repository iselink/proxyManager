package net.iselink.proxymanager;

import net.iselink.proxymanager.commands.*;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.connections.RedisPubSubManagementConnection;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class ProxyManagerPlugin extends Plugin implements Listener {

	private static final Logger logger = LoggerFactory.getLogger(ProxyManagerPlugin.class);
	private ManagementConnection managementConnection = null;
	private PluginEventListener eventListener = null;

	@Override
	public void onEnable() {
		// Plugin startup logic
		eventListener = new PluginEventListener(this);

		//register itself for some event handlers
		getProxy().getPluginManager().registerListener(this, this);

		//register commands
		getProxy().getPluginManager().registerCommand(this, new BroadcastCommand(this));
		getProxy().getPluginManager().registerCommand(this, new KickCommand(this));
		getProxy().getPluginManager().registerCommand(this, new ProxyPingCommand(this));
		getProxy().getPluginManager().registerCommand(this, new KickAllCommand(this));
		getProxy().getPluginManager().registerCommand(this, new WhereIsCommand(this));


		//create dummy connection
		//TODO: for development only ↓
		//managementConnection = new DummyManagementConnection(this);
		managementConnection = new RedisPubSubManagementConnection(this);


		//pooling from management connection
		getProxy().getScheduler().schedule(this, () -> {
			managementConnection.processEvents();
		}, 1L, 100L, TimeUnit.MILLISECONDS);


	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public ManagementConnection getManagementConnection() {
		return managementConnection;
	}
}

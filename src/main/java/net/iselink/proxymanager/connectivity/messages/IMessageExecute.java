package net.iselink.proxymanager.connectivity.messages;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.md_5.bungee.api.ProxyServer;

public interface IMessageExecute {
	void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection);
}

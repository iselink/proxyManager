package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ProxyPingCommand extends Command {

	private final ProxyManagerPlugin manager;

	public ProxyPingCommand(ProxyManagerPlugin manager) {
		super("proxyping");    //TODO: implement authentication or something like this
		this.manager = manager;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String senderName = null;

		//if sender is null, stats are printed into console only!
		if (sender instanceof ProxiedPlayer) {
			senderName = sender.getName();
		}

		manager.getManagementConnection().pingProxies(senderName);
	}
}

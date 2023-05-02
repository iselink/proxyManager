package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

	private final ProxyManagerPlugin proxyManagerPlugin;

	public BroadcastCommand(ProxyManagerPlugin proxyManagerPlugin) {
		super("broadcast");    //TODO: add permission or security check
		this.proxyManagerPlugin = proxyManagerPlugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		proxyManagerPlugin.getManagementConnection().broadcast(Utils.makeStringFromStrings(args), sender.getName(), true);
	}
}

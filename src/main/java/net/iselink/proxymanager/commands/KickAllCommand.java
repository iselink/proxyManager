package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class KickAllCommand extends Command {

	private final ProxyManagerPlugin manager;

	public KickAllCommand(ProxyManagerPlugin manager) {
		super("kickall", CommandPermissions.KICK_ALL.getPermission());
		this.manager = manager;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String executer = sender.getName();
		String reason = Utils.makeStringFromStrings(args);
		manager.getManagementConnection().kickAll(executer, reason);
	}
}

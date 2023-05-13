package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	private final ProxyManagerPlugin proxyManagerPlugin;

	public KickCommand(ProxyManagerPlugin proxyManagerPlugin) {
		super("kick", CommandPermissions.KICK.getPermission());
		this.proxyManagerPlugin = proxyManagerPlugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent("Usage: <player name> [reason]"));
			return;
		}

		String playerName = args[0];
		String reason = Utils.makeStringFromStrings(1, args.length, args);

		proxyManagerPlugin.getManagementConnection().requestPlayerKick(playerName, reason, sender.getName());
	}
}

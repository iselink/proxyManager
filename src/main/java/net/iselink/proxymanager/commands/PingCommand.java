package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.utils.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

	private final ProxyManagerPlugin proxyManagerPlugin;

	public PingCommand(ProxyManagerPlugin proxyManagerPlugin) {
		super("ping", CommandPermissions.PING.getPermission());
		this.proxyManagerPlugin = proxyManagerPlugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			int ping = ((ProxiedPlayer) sender).getPing();
			sender.sendMessage(
					new MessageBuilder()
							.addText("Ping to the proxy server: ")
							.addText(Integer.toString(ping), ChatColor.DARK_AQUA)
							.addText(" ms.")
							.toArray()
			);
		} else {
			if (args.length != 1) {
				sender.sendMessage(new TextComponent("This command is for players in-game only; console require player's name."));
				return;
			}

			String player = args[0];
			ProxiedPlayer pplayer = proxyManagerPlugin.getProxy().getPlayer(player);
			if (pplayer == null) {
				//TODO: multi-proxy support (not important as this command is for players mainly)
				sender.sendMessage(new TextComponent("This player is not online or on this proxy."));
				return;
			}

			sender.sendMessage(
					new MessageBuilder()
							.addText("Latency for player ")
							.addText(player)
							.addText(" is ")
							.addText(Integer.toString(pplayer.getPing()))
							.addText(" ms.")
							.toArray()
			);
		}

	}
}

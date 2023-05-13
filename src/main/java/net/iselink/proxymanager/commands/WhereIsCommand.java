package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.utils.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhereIsCommand extends Command {

	private final ProxyManagerPlugin proxyManagerPlugin;

	public WhereIsCommand(ProxyManagerPlugin proxyManagerPlugin) {
		super("whereis", CommandPermissions.WHERE_IS.getPermission());
		this.proxyManagerPlugin = proxyManagerPlugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent("Invalid usage. (/whereis <player name>)"));
			return;
		}

		String name = args[0];
		ProxiedPlayer player = proxyManagerPlugin.getProxy().getPlayer(name);
		if (player != null) {
			//player is connected to this proxy
			sender.sendMessage(new MessageBuilder()
					.addText("Player ")
					.addText(player.getName(), ChatColor.GOLD)
					.addText(" is on server ")
					.addText(player.getServer().getInfo().getName(), ChatColor.GOLD)
					.toArray());
			return;
		}

		//send 'where is' request to all proxies.
		sender.sendMessage(new TextComponent("Player is offline or connected on different proxy, sending lookup request..."));
		proxyManagerPlugin.getManagementConnection().whereIsPlayer(name, sender instanceof ProxiedPlayer ? sender.getName() : null);
	}

}

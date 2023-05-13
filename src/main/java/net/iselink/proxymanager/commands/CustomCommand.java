package net.iselink.proxymanager.commands;

import net.iselink.proxymanager.configuration.CustomCommandConfiguration;
import net.iselink.proxymanager.utils.MessageBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CustomCommand extends Command {

	private final String command;

	public CustomCommand(CustomCommandConfiguration ccc) {
		super(ccc.getName(), ccc.getPermission());
		this.command = ccc.getCommand();
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			((ProxiedPlayer) sender).chat(command.startsWith("/") ? command : "/" + command);
		} else {
			sender.sendMessage(new MessageBuilder().addText("This command is custom command and available only for players in-game.").toArray());
		}
	}
}

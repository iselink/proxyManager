package net.iselink.proxymanager.connectivity.messages.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.MessageResponse;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.utils.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WhereIsPlayerResponse extends MessageResponse implements IMessageExecute {


	@Expose
	@SerializedName("name")
	private final String name;

	@Expose
	@SerializedName("command_sender_name")
	private final String commandSenderName;

	@Expose
	@SerializedName("server_name")
	private final String serverName;

	public WhereIsPlayerResponse(String name, String commandSenderName, String serverName) {
		super(MessageType.WHERE_IS_PLAYER_RESPONSE);
		this.name = name;
		this.commandSenderName = commandSenderName;
		this.serverName = serverName;
	}

	public String getName() {
		return name;
	}

	public String getCommandSenderName() {
		return commandSenderName;
	}

	public String getServerName() {
		return serverName;
	}

	@Override
	public void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection) {
		if (commandSenderName == null) {
			proxy.getLogger().info(String.format("Player %s is on server %s.", name, serverName));
		} else {
			ProxiedPlayer commandSender = proxy.getPlayer(commandSenderName);
			if (commandSender != null) {
				commandSender.sendMessage(new MessageBuilder()
						.addText("Player ")
						.addText(name, ChatColor.GOLD)
						.addText(" is on server ")
						.addText(serverName, ChatColor.GOLD)
						.toArray());
			} else {
				proxy.getLogger().info(String.format("Player %s is on server %s.", name, serverName));
			}
		}
	}
}

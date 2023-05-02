package net.iselink.proxymanager.connectivity.messages.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.MessageRequest;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.connectivity.messages.responses.WhereIsPlayerResponse;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WhereIsPlayerRequest extends MessageRequest implements IMessageExecute {


	@Expose
	@SerializedName("player_name")
	private final String playerName;

	@Expose
	@SerializedName("command_sender_name")
	private final String commandSenderName;

	public WhereIsPlayerRequest(String playerName, String commandSenderName) {
		super(MessageType.WHERE_IS_PLAYER_REQ);
		this.playerName = playerName;
		this.commandSenderName = commandSenderName;
	}

	@Override
	public void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection) {
		ProxiedPlayer player = proxy.getPlayer(playerName);
		if (player != null) {
			connection.transmitMessage(new WhereIsPlayerResponse(player.getName(), commandSenderName, player.getServer().getInfo().getName()));
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getCommandSenderName() {
		return commandSenderName;
	}
}

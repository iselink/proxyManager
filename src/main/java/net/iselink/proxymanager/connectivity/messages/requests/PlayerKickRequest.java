package net.iselink.proxymanager.connectivity.messages.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.MessageRequest;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerKickRequest extends MessageRequest implements IMessageExecute {


	@Expose
	@SerializedName("player_name")
	private final String playerName;

	@Expose
	@SerializedName("reason")
	private final String reason;

	@Expose
	@SerializedName("sender_name")
	private final String senderName;

	public PlayerKickRequest(String playerName, String reason, String senderName) {
		super(MessageType.PLAYER_KICK_REQ);
		this.playerName = playerName;
		this.reason = reason;
		this.senderName = senderName;
	}

	public PlayerKickRequest(String playerName, String reason) {
		super(MessageType.PLAYER_KICK_REQ);
		this.playerName = playerName;
		this.reason = reason;
		this.senderName = null;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getReason() {
		return reason;
	}

	public String getSenderName() {
		return senderName;
	}

	@Override
	public void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection) {
		ProxiedPlayer player = proxy.getPlayer(getPlayerName());
		if (player == null) {
			return;
		}

		player.disconnect(Utils.buildKickMessage(senderName, reason));

		//TODO: send back PlayerKickResponse
	}


}

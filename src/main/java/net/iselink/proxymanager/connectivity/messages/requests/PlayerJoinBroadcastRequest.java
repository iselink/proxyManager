package net.iselink.proxymanager.connectivity.messages.requests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.MessageRequest;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.utils.MessageBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * Always sent when any player decide to join network.
 * This message will kick any player with same name.
 */
public class PlayerJoinBroadcastRequest extends MessageRequest implements IMessageExecute {

	@Expose
	@SerializedName("player_name")
	private final String playerName;

	@Expose
	@SerializedName("proxy_server")
	private final UUID proxyServer;

	@Expose
	@SerializedName("ip_address")
	private final String playerIpAddress;


	public PlayerJoinBroadcastRequest(String playerName, UUID proxyServer, String playerIpAddress) {
		super(MessageType.PLAYER_JOIN_BROADCAST_REQ);
		this.playerName = playerName;
		this.proxyServer = proxyServer;
		this.playerIpAddress = playerIpAddress;
	}


	@Override
	public void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection) {
		ProxiedPlayer player = proxy.getPlayer(playerName);
		if (player != null && manager.getConfiguration().getUuid().compareTo(proxyServer) != 0) {
			player.disconnect(new MessageBuilder().addText("Player with same name is connecting from different proxy server.").toArray());
			proxy.getLogger().info(String.format("Kicking player %s for connecting to different proxy server (%s).", player, proxyServer));
		}
	}
}

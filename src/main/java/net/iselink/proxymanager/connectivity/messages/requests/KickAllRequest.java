package net.iselink.proxymanager.connectivity.messages.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.utils.Utils;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.MessageRequest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class KickAllRequest extends MessageRequest implements IMessageExecute {

	@Expose
	@SerializedName("name")
	private final String name;

	@Expose
	@SerializedName("reason")
	private final String reason;

	public KickAllRequest(String name, String reason) {
		super(MessageType.KICK_ALL_REQ);
		this.name = name;
		this.reason = reason;
	}

	public String getName() {
		return name;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection) {
		proxy.getLogger().info(String.format("Received command kickall, executed by '%s' for reason '%s'", name, reason));
		for (ProxiedPlayer player : proxy.getPlayers()) {
			player.disconnect(Utils.buildKickMessage(name, reason));
		}
	}
}

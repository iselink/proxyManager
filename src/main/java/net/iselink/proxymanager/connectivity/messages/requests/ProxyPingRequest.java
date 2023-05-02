package net.iselink.proxymanager.connectivity.messages.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.messages.IMessageExecute;
import net.iselink.proxymanager.connectivity.messages.MessageRequest;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.connectivity.messages.responses.ProxyPingResponse;
import net.md_5.bungee.api.ProxyServer;

public class ProxyPingRequest extends MessageRequest implements IMessageExecute {

	/**
	 * Who send command (player's name) or null.
	 */

	@Expose
	@SerializedName("sender")
	private final String sender;

	/**
	 * Who send request (proxy).
	 * Used for direct reporting.
	 */

	@Expose
	@SerializedName("initiator")
	private final String initiator;

	public ProxyPingRequest(String sender, String initiator) {
		super(MessageType.PROXY_PING_REQ);

		this.sender = sender;
		this.initiator = initiator;
	}

	@Override
	public void execute(ProxyManagerPlugin manager, ProxyServer proxy, ManagementConnection connection) {
		proxy.getLogger().info(String.format("Proxy ping request: from '%s' issued by '%s'", initiator, sender));

		connection.transmitMessage(new ProxyPingResponse(proxy.getName(), proxy.getOnlineCount(), sender));
	}
}

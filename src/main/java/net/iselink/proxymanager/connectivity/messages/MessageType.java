package net.iselink.proxymanager.connectivity.messages;

import net.iselink.proxymanager.connectivity.messages.requests.*;
import net.iselink.proxymanager.connectivity.messages.responses.ProxyPingResponse;
import net.iselink.proxymanager.connectivity.messages.responses.WhereIsPlayerResponse;

/**
 * Enum of all message types.
 */
public enum MessageType {

	KICK_ALL_REQ(KickAllRequest.class),
	BROADCAST_REQ(MessageBroadcastRequest.class),
	PLAYER_KICK_REQ(PlayerKickRequest.class),
	PROXY_PING_REQ(ProxyPingRequest.class),
	WHERE_IS_PLAYER_REQ(WhereIsPlayerRequest.class),
	PROXY_PING_RESPONSE(ProxyPingResponse.class),
	WHERE_IS_PLAYER_RESPONSE(WhereIsPlayerResponse.class);


	private final Class<? extends Message> associatedClass;

	MessageType(Class<? extends Message> associatedClass) {

		this.associatedClass = associatedClass;
	}

	public Class<? extends Message> getAssociatedClass() {
		return associatedClass;
	}
}

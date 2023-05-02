package net.iselink.proxymanager.connectivity.messages.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.connectivity.messages.IMessageEventPublisher;
import net.iselink.proxymanager.connectivity.messages.MessageRequest;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.events.BroadcastEvent;
import net.md_5.bungee.api.plugin.Event;

public class MessageBroadcastRequest extends MessageRequest implements IMessageEventPublisher {


	@Expose
	@SerializedName("text")
	private final String text;

	@Expose
	@SerializedName("player_name")
	private final String playerName;


	@Expose
	@SerializedName("include_player_name")
	private final boolean includePlayerName;

	public MessageBroadcastRequest(String text, String playerName) {
		super(MessageType.BROADCAST_REQ);
		this.text = text;
		this.playerName = playerName;
		this.includePlayerName = false;
	}

	public MessageBroadcastRequest(String text, String playerName, boolean includePlayerName) {
		super(MessageType.BROADCAST_REQ);
		this.text = text;
		this.playerName = playerName;
		this.includePlayerName = includePlayerName;
	}

	public String getText() {
		return text;
	}

	public String getPlayerName() {
		return playerName;
	}

	public boolean isIncludePlayerName() {
		return includePlayerName;
	}

	@Override
	public Event createEvent() {
		return new BroadcastEvent(text, playerName, includePlayerName);
	}
}

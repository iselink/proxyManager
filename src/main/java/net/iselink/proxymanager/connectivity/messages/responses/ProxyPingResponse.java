package net.iselink.proxymanager.connectivity.messages.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.iselink.proxymanager.connectivity.messages.IMessageEventPublisher;
import net.iselink.proxymanager.connectivity.messages.MessageResponse;
import net.iselink.proxymanager.connectivity.messages.MessageType;
import net.iselink.proxymanager.events.ProxyPingResponseEvent;
import net.md_5.bungee.api.plugin.Event;

public class ProxyPingResponse extends MessageResponse implements IMessageEventPublisher {


	@Expose
	@SerializedName("name")
	private final String name;

	@Expose
	@SerializedName("player_count")
	private final int playerCount;


	@Expose
	@SerializedName("command_sender")
	private final String commandSender;

	public ProxyPingResponse(String name, int playerCount, String commandSender) {
		super(MessageType.PROXY_PING_RESPONSE);
		this.name = name;
		this.playerCount = playerCount;
		this.commandSender = commandSender;
	}

	public String getName() {
		return name;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public String getCommandSender() {
		return commandSender;
	}

	@Override
	public Event createEvent() {
		return new ProxyPingResponseEvent(name, playerCount, commandSender);
	}
}

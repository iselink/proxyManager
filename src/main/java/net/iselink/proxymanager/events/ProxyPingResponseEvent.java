package net.iselink.proxymanager.events;

import net.md_5.bungee.api.plugin.Event;

public class ProxyPingResponseEvent extends Event {
	private final String name;
	private final int playerCount;
	private final String commandSender;

	public ProxyPingResponseEvent(String name, int playerCount, String commandSender) {
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
}

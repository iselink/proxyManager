package net.iselink.proxymanager.events;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Event;

public class BroadcastEvent extends Event {

	private final String message;
	private final String player;
	private final boolean includePlayerName;

	public BroadcastEvent(String message) {
		this.message = message;
		this.player = null;
		this.includePlayerName = false;
	}

	public BroadcastEvent(String message, String player) {
		this.message = message;
		this.player = player;
		this.includePlayerName = false;
	}


	public BroadcastEvent(String message, String player, boolean includePlayerName) {
		this.message = message;
		this.player = player;
		this.includePlayerName = includePlayerName;
	}

	public String getMessage() {
		return message;
	}

	public String getPlayer() {
		return player;
	}

	public BaseComponent[] createMessage() {
		TextComponent prefix = new TextComponent("BROADCAST");
		prefix.setColor(ChatColor.RED);

		TextComponent separator = new TextComponent(" > ");
		prefix.setColor(ChatColor.DARK_RED);

		TextComponent playerName = new TextComponent(player != null ? player : "SYSTEM");
		playerName.setColor(ChatColor.GOLD);

		TextComponent text = new TextComponent(message);
		text.setColor(ChatColor.WHITE);

		if (includePlayerName) {
			return new TextComponent[]{
					prefix,
					separator,
					playerName,
					separator,
					text
			};
		} else {
			return new TextComponent[]{
					prefix,
					separator,
					text
			};
		}

	}
}

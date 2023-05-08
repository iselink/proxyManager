package net.iselink.proxymanager.utils;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Small builder for building text messages from TextComponent.
 */
public class MessageBuilder {

	private static final int RESIZE_COUNT = 16;
	private TextComponent[] components;
	private int indexOfLastEntry;

	public MessageBuilder() {
		this.components = new TextComponent[RESIZE_COUNT];
		this.indexOfLastEntry = 0;
	}

	public MessageBuilder addText(String text) {
		return addText(text, null);
	}

	public MessageBuilder addText(String text, ChatColor color) {
		return addText(new TextComponent(text), color);
	}


	public MessageBuilder addText(TextComponent textComponent, ChatColor color) {
		if (indexOfLastEntry == components.length) {
			resize();
		}

		if (color != null) {
			textComponent.setColor(color);
		}

		components[indexOfLastEntry++] = textComponent;

		return this;
	}

	public MessageBuilder clear() {
		for (int i = 0; i < indexOfLastEntry; i++) {
			components[i] = null;
		}
		return this;
	}

	public TextComponent[] toArray() {
		TextComponent[] c = new TextComponent[indexOfLastEntry];
		System.arraycopy(components, 0, c, 0, indexOfLastEntry);
		return c;
	}


	private void resize() {
		TextComponent[] newBuffer = new TextComponent[components.length + RESIZE_COUNT];
		if (indexOfLastEntry >= 0)
			System.arraycopy(components, 0, newBuffer, 0, indexOfLastEntry);
		components = newBuffer;
	}

}

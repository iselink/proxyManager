package net.iselink.proxymanager.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {

	/**
	 * Make from multiple strings one string.
	 * All strings will be concatenated with space in between.
	 *
	 * @param strings Strings to concatenate.
	 * @return String of all strings.
	 */
	public static String makeStringFromStrings(String... strings) {
		return makeStringFromStrings(0, strings.length, strings);
	}

	/**
	 * Make from multiple strings one string.
	 * All strings will be concatenated with space in between.
	 *
	 * @param startIndex Index of first string to concatenate.
	 * @param strings Strings to concatenate.
	 * @return String of all strings.
	 */
	public static String makeStringFromStrings(int startIndex, int endIndex, String... strings) {
		if (startIndex > endIndex) {
			throw new IllegalStateException("Start index is bigger than end index.");
		} else if (startIndex > strings.length || startIndex < 0) {
			throw new IndexOutOfBoundsException("Start index is out of bound.");
		} else if (endIndex > strings.length) {
			throw new IndexOutOfBoundsException("End index is out of bound.");
		}

		StringBuilder builder = new StringBuilder();
		for (int index = startIndex; index < endIndex; index++) {
			builder.append(strings[index]);
			if (index + 1 < strings.length) {
				builder.append(' ');
			}
		}

		return builder.toString();
	}


	public static TextComponent[] buildKickMessage(String senderName, String reason) {
		TextComponent l1 = new TextComponent("You has been kicked from the server for ");
		TextComponent l2 = new TextComponent(reason != null && reason.length() > 0 ? reason : "<no reason provided>");
		TextComponent l3 = new TextComponent(" by ");
		TextComponent l4 = new TextComponent(senderName != null && senderName.length() > 0 ? senderName : "<no user provided>");
		TextComponent l5 = new TextComponent(".");

		l1.setColor(ChatColor.GOLD);
		l3.setColor(ChatColor.GOLD);
		l4.setColor(ChatColor.AQUA);
		l5.setColor(ChatColor.GOLD);


		return new TextComponent[]{
				l1, l2, l3, l4, l5
		};
	}
}

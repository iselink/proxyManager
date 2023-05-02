package net.iselink.proxymanager.connectivity;

import net.iselink.proxymanager.connectivity.messages.Message;

public interface IManagement {

	/**
	 * Send broadcast message to all players across all proxies.
	 * If player name is null and showPlayerName is true, proxies will show SYSTEM name instead.
	 *
	 * @param text           Message to send.
	 * @param playerName     Name of the sender.
	 * @param showPlayerName Show name of the broadcaster.
	 */
	void broadcast(String text, String playerName, boolean showPlayerName);

	/**
	 * Process events from incoming message queue.
	 */
	void processEvents();

	/**
	 * Request kick a player from this or other proxies.
	 *
	 * @param player Name of the player.
	 * @param reason Reason for the kick (can be null).
	 * @param sender Who kicked user out (can be null).
	 */
	void requestPlayerKick(String player, String reason, String sender);

	void pingProxies(String sender);

	void transmitMessage(Message message);

	void kickAll(String executer, String reason);

	/**
	 * Send lookup request all proxies, where player N is connected.
	 *
	 * @param commandSenderName
	 * @param playerName
	 */
	void whereIsPlayer(String playerName, String commandSenderName);
}

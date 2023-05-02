package net.iselink.proxymanager.connectivity.connections;

import net.iselink.proxymanager.ProxyManagerPlugin;
import net.iselink.proxymanager.connectivity.messages.Message;
import redis.clients.jedis.JedisPool;

/**
 * Management connection implemented via redis's channels.
 */
public class RedisPubSubManagementConnection extends ManagementConnection {

	private JedisPool pool;

	public RedisPubSubManagementConnection(ProxyManagerPlugin proxyManagerPlugin) {
		super(proxyManagerPlugin);
		pool = new JedisPool("10.0.0.200");	//TODO: hardcoded value
	}

	@Override
	public void broadcast(String text, String playerName, boolean showPlayerName) {

	}


	@Override
	public void requestPlayerKick(String player, String reason, String sender) {

	}

	@Override
	public void pingProxies(String sender) {

	}

	@Override
	public void transmitMessage(Message message) {

	}

	@Override
	public void kickAll(String executer, String reason) {

	}

	@Override
	public void whereIsPlayer(String playerName, String commandSenderName) {

	}
}

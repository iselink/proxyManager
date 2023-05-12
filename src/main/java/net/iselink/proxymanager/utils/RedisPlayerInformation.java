package net.iselink.proxymanager.utils;

import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Utility class for serialisation of user info data into JSON.
 */
public class RedisPlayerInformation {

	private final String name;
	private final boolean forge_user;
	private final String server;
	private final String uuid;

	public RedisPlayerInformation(ProxiedPlayer player) {
		this.name = player.getName();
		this.forge_user = player.isForgeUser();
		this.server = player.getServer().getInfo().getName();
		if (player.getUniqueId() != null)
			this.uuid = player.getUniqueId().toString();
		else
			this.uuid = null;
	}

	public String toJson() {
		return new GsonBuilder().serializeNulls().create().toJson(this, RedisPlayerInformation.class);
	}
}

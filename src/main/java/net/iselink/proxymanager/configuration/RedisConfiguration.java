package net.iselink.proxymanager.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import redis.clients.jedis.Protocol;

public class RedisConfiguration {


	@Expose
	@SerializedName("host")
	private String host = null;

	@Expose
	@SerializedName("port")
	private int port = Protocol.DEFAULT_PORT;

	@Expose
	@SerializedName("user")
	private String user = null;

	@Expose
	@SerializedName("password")
	private String password = null;

	public RedisConfiguration() {
	}


	public RedisConfiguration(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}
}

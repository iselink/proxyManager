package net.iselink.proxymanager;

import net.iselink.proxymanager.commands.*;
import net.iselink.proxymanager.configuration.Configuration;
import net.iselink.proxymanager.connectivity.connections.DummyManagementConnection;
import net.iselink.proxymanager.connectivity.connections.ManagementConnection;
import net.iselink.proxymanager.connectivity.connections.RedisPubSubManagementConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class ProxyManagerPlugin extends Plugin implements Listener {

	private static final Logger logger = LoggerFactory.getLogger(ProxyManagerPlugin.class);
	private ManagementConnection managementConnection = null;
	private PluginEventListener eventListener = null;
	private Configuration configuration = null;
	private JedisPool redisConnection = null;

	@Override
	public void onEnable() {
		// Plugin startup logic
		try {
			initiateConfiguration();
		} catch (IOException e) {
			getSLF4JLogger().error("Error while loading configuration (saving new one) - check your filesystem and stack trace below.");
			e.printStackTrace();
			return;
		}

		configuration.printConfig(getLogger());

		eventListener = new PluginEventListener(this);

		//register itself for some event handlers
		getProxy().getPluginManager().registerListener(this, this);

		//register commands
		getProxy().getPluginManager().registerCommand(this, new BroadcastCommand(this));
		getProxy().getPluginManager().registerCommand(this, new KickCommand(this));
		getProxy().getPluginManager().registerCommand(this, new ProxyPingCommand(this));
		getProxy().getPluginManager().registerCommand(this, new KickAllCommand(this));
		getProxy().getPluginManager().registerCommand(this, new WhereIsCommand(this));


		//create management connection
		switch (configuration.getCommunicationMethod()) {
			case Dummy: {
				managementConnection = new DummyManagementConnection(this);
				break;
			}
			case Redis: {
				redisConnection = new JedisPool(
						configuration.getRedisConfiguration().getHost(),
						configuration.getRedisConfiguration().getPort(),
						configuration.getRedisConfiguration().getUser(),
						configuration.getRedisConfiguration().getPassword());
				managementConnection = new RedisPubSubManagementConnection(this, redisConnection);
				break;
			}
			default: {
				getLogger().warning("Unknown method: " + configuration.getCommunicationMethod());
				getLogger().warning("This is plugin error and you should report this (for now, created dummy connection instead).");
				managementConnection = new DummyManagementConnection(this);
				break;
			}
		}

		//pooling from management connection
		getProxy().getScheduler().schedule(this, () -> {
			managementConnection.processEvents();
		}, 1L, 100L, TimeUnit.MILLISECONDS);

		//refresh redis players every 20 seconds
		//by hardcoded value, every entry will expire after 120 seconds
		//which mean 6 attempts before this information is lost.
		//every refresh is rescheduled as async
		if (getConfiguration().isRedisSyncActivePlayer()) {
			getLogger().info("Enabled refreshing Redis...");
			getProxy().getScheduler().schedule(this, () -> {
				getProxy().getScheduler().runAsync(this, ()-> {
					for (ProxiedPlayer player : getProxy().getPlayers()) {
						try (Jedis jedis = redisConnection.getResource()) {
							//you can't do this on older version of redis
							jedis.expire(player.getName(), 120);
						}
					}
				});
			}, 20, TimeUnit.SECONDS);
		}

	}

	/**
	 * Load configuration from config file.
	 * If this file is missing, create new one.
	 */
	private void initiateConfiguration() throws IOException {
		File folder = getDataFolder();
		if (!folder.exists()) {
			folder.mkdir();
		}

		File configFile = new File(folder, "config.json");
		if (configFile.exists()) {
			//load it
			configuration = Configuration.loadFromFile(configFile);
		} else {
			//save new one and initiate new config
			configuration = new Configuration();
			configuration.initiateAsNewConfig();
			configuration.saveToFile(configFile);
			getLogger().info("Created a new config file - please stop proxy server and configure plugin now.");
		}

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public ManagementConnection getManagementConnection() {
		return managementConnection;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public JedisPool getRedisConnection() {
		return redisConnection;
	}
}

package net.iselink.proxymanager.configuration;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manage configuration of plugin (from JSON file).
 * All fields should be nicely named and explicitly annotated as exposed for finest control over config file.
 */
public class Configuration {

	@Expose
	@SerializedName("proxy_uuid")
	private UUID uuid = null;

	@Expose
	@SerializedName("communication_method")
	private CommunicationMethod communicationMethod = CommunicationMethod.Dummy;

	@Expose
	@SerializedName("redis")
	private RedisConfiguration redisConfiguration = null;

	@Expose
	@SerializedName("redis_sync_active_players")
	private boolean redisSyncActivePlayer = false;

	public static Configuration loadFromFile(File file) throws FileNotFoundException, IOException {
		try (FileReader reader = new FileReader(file)) {
			return new GsonBuilder()
					.excludeFieldsWithoutExposeAnnotation()
					.setPrettyPrinting()
					.create()
					.fromJson(reader, Configuration.class);
		}
	}

	public void saveToFile(File configFile) throws IOException {
		try (FileWriter writer = new FileWriter(configFile)) {
			new GsonBuilder()
					.excludeFieldsWithoutExposeAnnotation()
					.setPrettyPrinting()
					.serializeNulls()
					.create()
					.toJson(this, writer);
		}
	}

	/**
	 * Create a new config struct prefilled with null and not set values.
	 */
	public void initiateAsNewConfig() {
		communicationMethod = CommunicationMethod.Dummy;
		redisConfiguration = new RedisConfiguration();
		uuid = UUID.randomUUID();
	}

	/**
	 * Print configuration to logger.
	 */
	public void printConfig(Logger logger) {
		logger.info("Configuration dump:");
		logger.info("Communication type: " + communicationMethod);
		switch (communicationMethod) {
			case Redis:
				logger.info("Redis host: " + redisConfiguration.getHost());
				logger.info("Redis port: " + redisConfiguration.getPort());
				break;

		}
		if (redisSyncActivePlayer && (redisConfiguration == null || (redisConfiguration.getHost() == null || redisConfiguration.getHost().length() == 0))) {
			logger.warning("Redis active player sync is enabled, but no redis configuration is set!");
		}
	}

	public CommunicationMethod getCommunicationMethod() {
		return communicationMethod;
	}

	public RedisConfiguration getRedisConfiguration() {
		return redisConfiguration;
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isRedisSyncActivePlayer() {
		return redisSyncActivePlayer;
	}

	public enum CommunicationMethod {
		Dummy,
		Redis
	}
}

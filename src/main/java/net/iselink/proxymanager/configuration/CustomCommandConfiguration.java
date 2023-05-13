package net.iselink.proxymanager.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Custom command configuration.
 */
public class CustomCommandConfiguration {

	@Expose
	@SerializedName("name")
	private String name;

	@Expose
	@SerializedName("permission")
	private String permission;

	@Expose
	@SerializedName("command")
	private String command;


	public CustomCommandConfiguration() {
	}

	public String getName() {
		return name;
	}

	public String getPermission() {
		return permission;
	}

	public String getCommand() {
		return command;
	}
}

package net.iselink.proxymanager.commands;

/**
 * Enum of all permissions used on every command.
 */
public enum CommandPermissions {
	BROADCAST("broadcast"),
	KICK("kick"),
	KICK_ALL("kickall"),
	PING("ping"),
	PROXY_PING("proxyping"),
	WHERE_IS("whereis");

	private final String permission;

	CommandPermissions(String permission) {
		this.permission = "proxy." + permission;
	}

	public String getPermission() {
		return permission;
	}
}

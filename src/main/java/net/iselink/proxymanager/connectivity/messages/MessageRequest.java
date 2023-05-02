package net.iselink.proxymanager.connectivity.messages;

public abstract class MessageRequest extends Message {
	public MessageRequest(MessageType messageType) {
		super(messageType);
	}
}

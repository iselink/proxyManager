package net.iselink.proxymanager.connectivity.messages;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Base class for any message that has been sent/received via management connection.
 */
public abstract class Message {

	@Expose
	@SerializedName("message_type")
	private final MessageType	messageType;

	public Message(MessageType messageType) {
		this.messageType = messageType;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String selfSerialize() {
		return new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.create()
				.toJson(this, this.getClass());
	}

}

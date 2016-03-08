package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

class WebSocketBroadcastMessage implements IWebSocketPushMessage {
	private Object message;

	public WebSocketBroadcastMessage(Object message) {
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}
}

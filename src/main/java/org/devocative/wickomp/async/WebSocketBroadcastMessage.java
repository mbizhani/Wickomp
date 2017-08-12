package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

public class WebSocketBroadcastMessage implements IWebSocketPushMessage {
	private static final long serialVersionUID = -3604855190745718139L;
	private Object message;

	public WebSocketBroadcastMessage(Object message) {
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}
}

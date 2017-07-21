package org.devocative.wickomp.async.response;

import org.apache.wicket.Component;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

public class WebSocketComponentRenderResult implements IWebSocketPushMessage {
	private static final long serialVersionUID = -1361297918932533773L;

	private Component[] components;

	public WebSocketComponentRenderResult(Component... components) {
		this.components = components;
	}

	public Component[] getComponents() {
		return components;
	}
}

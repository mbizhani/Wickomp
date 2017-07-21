package org.devocative.wickomp.async.response;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

public class WebSocketJavascriptResult implements IWebSocketPushMessage {
	private static final long serialVersionUID = -917498722251819884L;

	private String script;

	public WebSocketJavascriptResult(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}
}

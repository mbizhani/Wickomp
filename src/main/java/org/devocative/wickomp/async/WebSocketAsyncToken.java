package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.api.registry.IKey;

public class WebSocketAsyncToken extends AsyncToken {
	private static final long serialVersionUID = 2447830252709170231L;

	private IKey key;
	private String sessionId;

	public IKey getKey() {
		return key;
	}

	public WebSocketAsyncToken setKey(IKey key) {
		this.key = key;
		return this;
	}

	public String getSessionId() {
		return sessionId;
	}

	public WebSocketAsyncToken setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}
}

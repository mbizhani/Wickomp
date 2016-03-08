package org.devocative.wickomp.async;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.api.registry.IKey;

class WebSocketAsyncToken extends AsyncToken {
	private String appKey;
	private IKey key;
	private String sessionId;

	public WebSocketAsyncToken setAppKey(String appKey) {
		this.appKey = appKey;
		return this;
	}

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

	public Application getApplication() {
		return Application.get(appKey);
	}
}

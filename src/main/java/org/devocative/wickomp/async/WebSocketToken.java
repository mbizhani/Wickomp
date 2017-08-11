package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.api.registry.IKey;

import java.io.Serializable;

public class WebSocketToken implements Serializable {
	private static final long serialVersionUID = -7356567905689793591L;

	private String applicationKey;
	private String sessionId;
	private IKey pageKey;

	public WebSocketToken(String applicationKey, String sessionId, IKey pageKey) {
		this.applicationKey = applicationKey;
		this.sessionId = sessionId;
		this.pageKey = pageKey;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public String getSessionId() {
		return sessionId;
	}

	public IKey getPageKey() {
		return pageKey;
	}
}

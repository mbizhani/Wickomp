package org.devocative.wickomp.async.processor;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.WebSocketAsyncResult;
import org.devocative.wickomp.async.WebSocketAsyncToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class AResponseProcessor {
	private static final Logger logger = LoggerFactory.getLogger(AResponseProcessor.class);

	private Application application;

	public AResponseProcessor(Application application) {
		this.application = application;
	}

	public abstract void processResponse(AsyncToken asyncToken, Serializable responsePayLoad, Exception error);

	protected void sendResponseByWS(AsyncToken asyncToken, Serializable responsePayLoad, Exception error) {
		if (asyncToken instanceof WebSocketAsyncToken) {
			WebSocketAsyncResult result = new WebSocketAsyncResult();
			result
				.setToken(asyncToken)
				.setResult(responsePayLoad)
				.setError(error);

			WebSocketAsyncToken wsat = (WebSocketAsyncToken) asyncToken;

			WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
			IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
			IWebSocketConnection connection = registry.getConnection(application, wsat.getSessionId(), wsat.getKey());
			if (connection != null && connection.isOpen()) {
				connection.sendMessage(result);
			} else {
				logger.warn("AsyncMediator.sendResponse: broken connection: tokenId={}, responsePayLoad={}",
					asyncToken.getId(), responsePayLoad);
			}
		}

	}
}

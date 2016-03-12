package org.devocative.wickomp.async;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncMediator {
	private static final Logger logger = LoggerFactory.getLogger(AsyncMediator.class);

	private static final Map<String, IAsyncRequestHandler> HANDLERS_MAP = new ConcurrentHashMap<>();
	private static Application application;

	public static void init(Application application) {
		AsyncMediator.application = application;
	}

	public static void registerHandler(String handlerId, IAsyncRequestHandler asyncHandler) {
		if (HANDLERS_MAP.containsKey(handlerId)) {
			throw new RuntimeException("Duplicate handlerId: " + handlerId);
		}
		HANDLERS_MAP.put(handlerId, asyncHandler);
		logger.info("AsyncMediator.registerHandler: {}", handlerId);
	}

	public synchronized static void sendRequest(String handlerId, AsyncToken asyncToken, Object requestPayLoad) {
		asyncToken.setHandlerId(handlerId);
		HANDLERS_MAP.get(handlerId).onRequest(asyncToken, requestPayLoad);

		/*AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
		if(target != null) {
			target.appendJavaScript("Wicket.WebSocket.send('{msg:\"\"}');");
		}*/
	}

	public static void sendResponse(AsyncToken asyncToken, Serializable responsePayLoad) {
		if (asyncToken instanceof WebSocketAsyncToken) {
			WebSocketAsyncResult result = new WebSocketAsyncResult();
			result.setToken(asyncToken)
				.setResult(responsePayLoad);

			WebSocketAsyncToken wsat = (WebSocketAsyncToken) asyncToken;

			WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(wsat.getApplication());
			IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
			IWebSocketConnection connection = registry.getConnection(wsat.getApplication(), wsat.getSessionId(), wsat.getKey());
			if (connection != null && connection.isOpen()) {
				connection.sendMessage(result);
			} else {
				logger.warn("AsyncMediator.sendResponse: broken connection: tokenId={}, responsePayLoad={}",
					asyncToken.getId(), responsePayLoad);
			}
		}

	}

	public static void broadcast(Object message) {
		if (application == null) {
			throw new RuntimeException("Call AsyncMediator.init() in Application.init()!");
		}

		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
		IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
		WebSocketPushBroadcaster broadcaster = new WebSocketPushBroadcaster(registry);
		broadcaster.broadcastAll(application, new WebSocketBroadcastMessage(message));
	}
}

package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncMediator {
	private static final Map<String, IAsyncRequestHandler> HANDLERS_MAP = new ConcurrentHashMap<>();

	public static void registerHandler(String handlerId, IAsyncRequestHandler asyncHandler) {
		if (HANDLERS_MAP.containsKey(handlerId)) {
			throw new RuntimeException("Duplicate handlerId: " + handlerId);
		}
		HANDLERS_MAP.put(handlerId, asyncHandler);
	}

	public synchronized static void sendRequest(String handlerId, AsyncToken asyncToken, Object requestPayLoad) {
		asyncToken.setHandler(handlerId);
		HANDLERS_MAP.get(handlerId).onRequest(asyncToken, requestPayLoad);
	}

	public static void sendResponse(AsyncToken asyncToken, Serializable responsePayLoad) {
		if (asyncToken instanceof WebSocketAsyncToken) {
			WebSocketAsyncResult result = new WebSocketAsyncResult();
			result.setToken(asyncToken)
				.setResult(responsePayLoad);

			WebSocketAsyncToken token = (WebSocketAsyncToken) asyncToken;
			ConnectedMessage message = new ConnectedMessage(token.getApplication(), token.getSessionId(), token.getKey());

			WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(token.getApplication());
			IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
			WebSocketPushBroadcaster broadcaster = new WebSocketPushBroadcaster(registry);
			broadcaster.broadcast(message, result);
		}

	}

	//TODO broadcast
}

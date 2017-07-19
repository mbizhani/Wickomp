package org.devocative.wickomp.async;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.devocative.wickomp.async.processor.ARequestProcessor;
import org.devocative.wickomp.async.processor.AResponseProcessor;
import org.devocative.wickomp.async.processor.SimpleRequestProcessor;
import org.devocative.wickomp.async.processor.SimpleResponseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncMediator {
	private static final Logger logger = LoggerFactory.getLogger(AsyncMediator.class);

	private static final Map<String, IAsyncRequestHandler> HANDLERS_MAP = new ConcurrentHashMap<>();
	private static Application application;

	private static ARequestProcessor requestProcessor;
	private static AResponseProcessor responseProcessor;

	public static void init(Application application) {
		AsyncMediator.application = application;

		requestProcessor = new SimpleRequestProcessor(HANDLERS_MAP);
		responseProcessor = new SimpleResponseProcessor(application);
	}

	public static void handleSessionExpiration(String user, String sessionId) {
		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
		IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
		Collection<IWebSocketConnection> connections = registry.getConnections(application, sessionId);
		logger.info("AsyncMediator.handleSessionExpiration: user={}, #webSockets={}", user, connections.size());
		for (IWebSocketConnection connection : connections) {
			connection.close(1, "-");
		}
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

		requestProcessor.processRequest(asyncToken, requestPayLoad);
	}

	public static void sendResponse(AsyncToken asyncToken, Serializable responsePayLoad) {
		//sendResponse(asyncToken, responsePayLoad, null);
		responseProcessor.processResponse(asyncToken, responsePayLoad, null);
	}

	public static void sendError(AsyncToken asyncToken, Exception error) {
		//sendResponse(asyncToken, null, error);
		responseProcessor.processResponse(asyncToken, null, error);
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

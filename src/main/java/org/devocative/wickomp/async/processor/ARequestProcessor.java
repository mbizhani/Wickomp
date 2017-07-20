package org.devocative.wickomp.async.processor;

import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class ARequestProcessor {
	protected static final Logger logger = LoggerFactory.getLogger(ARequestProcessor.class);

	private Map<String, IAsyncRequestHandler> handlersMap;

	// ------------------------------

	public ARequestProcessor(Map<String, IAsyncRequestHandler> handlersMap) {
		this.handlersMap = handlersMap;
	}

	// ------------------------------

	public abstract void processRequest(AsyncToken asyncToken, Object requestPayLoad);

	public abstract void shutdown();

	// ------------------------------

	protected void sendRequest(AsyncToken asyncToken, Object requestPayLoad) {
		String handlerId = asyncToken.getHandlerId();
		handlersMap.get(handlerId).onRequest(asyncToken, requestPayLoad);
	}
}

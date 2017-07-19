package org.devocative.wickomp.async.processor;

import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;

import java.util.Map;

public abstract class ARequestProcessor {
	protected Map<String, IAsyncRequestHandler> handlersMap;

	public ARequestProcessor(Map<String, IAsyncRequestHandler> handlersMap) {
		this.handlersMap = handlersMap;
	}

	public abstract void processRequest(AsyncToken asyncToken, Object requestPayLoad);
}

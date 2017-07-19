package org.devocative.wickomp.async.processor;

import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;

import java.util.Map;

public class SimpleRequestProcessor extends ARequestProcessor {
	public SimpleRequestProcessor(Map<String, IAsyncRequestHandler> handlersMap) {
		super(handlersMap);
	}

	@Override
	public void processRequest(AsyncToken asyncToken, Object requestPayLoad) {
		String handlerId = asyncToken.getHandlerId();
		handlersMap.get(handlerId).onRequest(asyncToken, requestPayLoad);
	}
}

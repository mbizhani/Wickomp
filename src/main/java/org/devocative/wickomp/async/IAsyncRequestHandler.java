package org.devocative.wickomp.async;

public interface IAsyncRequestHandler {
	void onRequest(AsyncToken asyncToken, Object requestPayLoad);
}

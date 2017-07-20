package org.devocative.wickomp.async.processor;

import org.devocative.wickomp.async.AsyncToken;

public class ObjectToQueue {
	private AsyncToken asyncToken;
	private Object payLoad;
	private Exception error;

	// ------------------------------

	public ObjectToQueue() {
	}

	public ObjectToQueue(AsyncToken asyncToken, Object payLoad) {
		this.asyncToken = asyncToken;
		this.payLoad = payLoad;
	}

	public ObjectToQueue(AsyncToken asyncToken, Object payLoad, Exception error) {
		this.asyncToken = asyncToken;
		this.payLoad = payLoad;
		this.error = error;
	}

	// ------------------------------

	public AsyncToken getAsyncToken() {
		return asyncToken;
	}

	public Object getPayLoad() {
		return payLoad;
	}

	public Exception getError() {
		return error;
	}
}

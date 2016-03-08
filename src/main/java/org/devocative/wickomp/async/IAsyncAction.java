package org.devocative.wickomp.async;

public interface IAsyncAction extends IAsyncResponseHandler {
	void sendAsyncRequest(String handler, Object requestPayLoad);
}

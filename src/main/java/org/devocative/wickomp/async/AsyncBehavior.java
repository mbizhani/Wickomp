package org.devocative.wickomp.async;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

import java.io.Serializable;

public class AsyncBehavior extends Behavior implements IAsyncAction {
	private WebSocketAsyncBehavior webSocketAsyncBehavior;
	private IAsyncResponseHandler asyncResponseHandler;

	public AsyncBehavior() {
		asyncResponseHandler = this;
	}

	public AsyncBehavior(IAsyncResponseHandler asyncResponseHandler) {
		this.asyncResponseHandler = asyncResponseHandler;
	}

	@Override
	public void bind(Component component) {
		webSocketAsyncBehavior = new WebSocketAsyncBehavior(asyncResponseHandler);
		component.add(webSocketAsyncBehavior);
	}

	@Override
	public void sendAsyncRequest(String handlerId, Object requestPayLoad) {
		AsyncMediator.sendRequest(handlerId, webSocketAsyncBehavior.getAsyncToken(), requestPayLoad);
	}

	@Override
	public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result) {
	}
}

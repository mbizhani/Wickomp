package org.devocative.wickomp.async;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

public class AsyncBehavior extends Behavior {
	private WebSocketAsyncBehavior webSocketAsyncBehavior;
	private IAsyncResponseHandler asyncResponseHandler;

	public AsyncBehavior(IAsyncResponseHandler asyncResponseHandler) {
		this.asyncResponseHandler = asyncResponseHandler;
	}

	@Override
	public void bind(Component component) {
		webSocketAsyncBehavior = new WebSocketAsyncBehavior(asyncResponseHandler);
		component.add(webSocketAsyncBehavior);
	}

	public AsyncToken getAsyncToken() {
		return webSocketAsyncBehavior.getAsyncToken();
	}
}

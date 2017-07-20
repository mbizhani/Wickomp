package org.devocative.wickomp.async;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

public class AsyncBehavior extends Behavior implements IAsyncAction {
	private static final long serialVersionUID = -3225863270756629087L;

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
	public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Object result) {
	}

	@Override
	public void onAsyncError(String handlerId, IPartialPageRequestHandler handler, Exception error) {
	}
}

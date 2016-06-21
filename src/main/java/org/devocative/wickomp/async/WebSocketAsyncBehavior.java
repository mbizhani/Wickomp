package org.devocative.wickomp.async;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.*;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebSocketAsyncBehavior extends WebSocketBehavior {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(WebSocketAsyncBehavior.class);

	private WebSocketAsyncToken asyncToken;
	private IAsyncResponseHandler responseHandler;

	public WebSocketAsyncBehavior(IAsyncResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public WebSocketAsyncToken getAsyncToken() {
		return asyncToken;
	}

	@Override
	public void afterRender(Component component) {
		asyncToken = new WebSocketAsyncToken()
			.setAppKey(Application.get().getApplicationKey())
			.setSessionId(WebSession.get().getId())
			.setKey(new PageIdKey(component.getPage().getPageId()));
	}

	@Override
	protected void onConnect(ConnectedMessage message) {
		logger.info("WebSocketAsyncBehavior.onConnect: msg={}", message);

		/*asyncToken = new WebSocketAsyncToken()
			.setAppKey(Application.get().getApplicationKey())
			.setKey(message.getKey())
			.setSessionId(message.getSessionId());*/
	}

	@Override
	protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
		if (message instanceof AsyncResult) {
			AsyncResult result = (AsyncResult) message;
			if (result.getToken().equals(asyncToken)) {
				if (result.getError() != null) {
					responseHandler.onAsyncError(asyncToken.getHandlerId(), handler, result.getError());
				} else {
					responseHandler.onAsyncResult(asyncToken.getHandlerId(), handler, result.getResult());
				}
			}
		}
	}

	@Override
	protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
		logger.debug("WebSocketAsyncBehavior.onMessage: {}", message);
	}

	@Override
	protected void onClose(ClosedMessage message) {
		logger.warn("WebSocketAsyncBehavior.onClose: {}", message);
	}

	@Override
	protected void onAbort(AbortedMessage message) {
		logger.warn("WebSocketAsyncBehavior.onAbort: {}", message);
	}
}

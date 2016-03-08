package org.devocative.wickomp.async;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.*;
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
	protected void onConnect(ConnectedMessage message) {
		logger.debug("WebSocketAsyncBehavior: WS onConnect: msg={}", message);

		asyncToken = new WebSocketAsyncToken()
			.setAppKey(Application.get().getApplicationKey())
			.setKey(message.getKey())
			.setSessionId(message.getSessionId());
	}

	@Override
	protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
		logger.debug("WebSocketAsyncBehavior: WS onPush: {}", message);

		if (message instanceof AsyncResult) {
			AsyncResult result = (AsyncResult) message;
			if (result.getToken().equals(asyncToken)) {
				responseHandler.onAsyncResult(handler, result.getResult());
			} else {
				logger.debug("!!! Wrong AsyncResult: {}", result);
			}
		} else {
			logger.debug("!!! Pushed Message: {}", message);
		}
	}

	@Override
	protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
		logger.debug("WebSocketAsyncBehavior: WS onMessage: {}", message);
	}

	@Override
	protected void onClose(ClosedMessage message) {
		logger.debug("WebSocketAsyncBehavior: WS onClose: {}", message);
	}

	@Override
	protected void onAbort(AbortedMessage message) {
		logger.debug("WebSocketAsyncBehavior: WS onAbort: {}", message);
	}
}

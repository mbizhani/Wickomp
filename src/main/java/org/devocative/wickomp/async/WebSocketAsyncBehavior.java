package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebSocketAsyncBehavior extends WebSocketBehavior {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(WebSocketAsyncBehavior.class);

	// ---------------

	@Override
	protected void onConnect(ConnectedMessage message) {
		logger.info("WebSocketAsyncBehavior.onConnect: msg={}", message);
	}

	@Override
	protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
		if (message instanceof WebSocketDelayedResponse) {
			WebSocketDelayedResponse response = (WebSocketDelayedResponse) message;
			if (response.isNew()) {
				response.call(handler);
				response.setNew(false);
			}
		}
	}

	@Override
	protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
		String text = message.getText();
		if (text.startsWith("W::")) {
			String rs = "W::R_" + text.substring(3);
			handler.push(rs);
		}
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

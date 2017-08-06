package org.devocative.wickomp.async;

import org.apache.wicket.Component;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.*;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import org.devocative.wickomp.async.response.WebSocketAsyncResult;
import org.devocative.wickomp.async.response.WebSocketComponentRenderResult;
import org.devocative.wickomp.async.response.WebSocketJavascriptResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebSocketAsyncBehavior extends WebSocketBehavior {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(WebSocketAsyncBehavior.class);

	private WebSocketAsyncToken asyncToken;
	private IAsyncResponseHandler responseHandler;

	// ------------------------------

	public WebSocketAsyncBehavior(IAsyncResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	// ------------------------------

	public WebSocketAsyncToken getAsyncToken() {
		return asyncToken;
	}

	// ---------------

	@Override
	public void afterRender(Component component) {
		asyncToken = new WebSocketAsyncToken()
			.setSessionId(WebSession.get().getId())
			.setKey(new PageIdKey(component.getPage().getPageId()));
	}

	// ---------------

	@Override
	protected void onConnect(ConnectedMessage message) {
		logger.info("WebSocketAsyncBehavior.onConnect: msg={}", message);
	}

	@Override
	protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
		if (message instanceof WebSocketAsyncResult) {
			AsyncResult result = (AsyncResult) message;
			if (result.getToken().equals(asyncToken)) {
				if (result.getError() != null) {
					responseHandler.onAsyncError(asyncToken.getHandlerId(), handler, result.getError());
				} else {
					try {
						responseHandler.onAsyncResult(asyncToken.getHandlerId(), handler, result.getResult());
					} catch (Exception e) {
						logger.warn("WebSocketAsyncBehavior.onAsyncResult", e);
						responseHandler.onAsyncError(asyncToken.getHandlerId(), handler, e);
					}
				}
			}
		} else if (message instanceof WebSocketJavascriptResult) {
			WebSocketJavascriptResult result = (WebSocketJavascriptResult) message;
			handler.appendJavaScript(result.getScript());
		} else if (message instanceof WebSocketComponentRenderResult) {
			WebSocketComponentRenderResult result = (WebSocketComponentRenderResult) message;
			handler.add(result.getComponents());
		} else if (!(message instanceof WebSocketBroadcastMessage)) {
			logger.warn("Unhandled push message type=[{}]: {}", message.getClass().getName(), message);
		}
	}

	@Override
	protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
		String text = message.getText();
		if ("W.PING".equals(text)) {
			handler.push("W.R_PING");
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

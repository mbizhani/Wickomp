package org.devocative.wickomp.async;

import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketDelayedResponse implements IWebSocketPushMessage {
	private static final long serialVersionUID = 1131076920625578700L;
	private static final Logger logger = LoggerFactory.getLogger(WebSocketDelayedResponse.class);

	private IAsyncResponse response;
	private Object result;
	private Exception exception;

	private boolean new_ = true;

	// ------------------------------

	public WebSocketDelayedResponse(IAsyncResponse response, Object result) {
		this.response = response;
		this.result = result;
	}

	public WebSocketDelayedResponse(IAsyncResponse response, Exception exception) {
		this.response = response;
		this.exception = exception;
	}

	// ------------------------------

	public void call(WebSocketRequestHandler handler) {
		if (exception != null) {
			response.onAsyncError(handler, exception);
		} else {
			try {
				response.onAsyncResult(handler, result);
			} catch (Exception e) {
				logger.error("WebSocketDelayedResponse.call", e);
				response.onAsyncError(handler, e);
			}
		}
	}

	// ---------------

	public boolean isNew() {
		return new_;
	}

	public void setNew(boolean value) {
		this.new_ = value;
	}
}

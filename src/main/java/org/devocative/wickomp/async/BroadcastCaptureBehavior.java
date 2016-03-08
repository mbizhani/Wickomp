package org.devocative.wickomp.async;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

public abstract class BroadcastCaptureBehavior<T> extends Behavior {
	private Class<T> messageType;

	public BroadcastCaptureBehavior(Class<T> messageType) {
		this.messageType = messageType;
	}

	@Override
	public void bind(Component component) {
		component.add(new WebSocketBehavior() {
			@Override
			protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
				if (message instanceof WebSocketBroadcastMessage) {
					WebSocketBroadcastMessage wsbm = (WebSocketBroadcastMessage) message;
					if (wsbm.getMessage() != null && messageType.isInstance(wsbm.getMessage())) {
						BroadcastCaptureBehavior.this.onMessage(handler, (T) wsbm.getMessage());
					}
				}
			}
		});
	}

	protected abstract void onMessage(IPartialPageRequestHandler handler, T message);
}

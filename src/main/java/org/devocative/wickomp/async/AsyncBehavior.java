package org.devocative.wickomp.async;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

public class AsyncBehavior extends Behavior {
	private static final long serialVersionUID = -3225863270756629087L;

	public AsyncBehavior() {
	}

	@Override
	public void bind(Component component) {
		WebSocketAsyncBehavior webSocketAsyncBehavior = new WebSocketAsyncBehavior();
		component.add(webSocketAsyncBehavior);
	}
}

package org.devocative.wickomp.async;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class WTextAsyncBehavior extends WebSocketBehavior {
	private static final long serialVersionUID = -6196868642769507632L;

	private static final Logger logger = LoggerFactory.getLogger(WTextAsyncBehavior.class);

	// ------------------------------

	private String appKey;
	private String sessionId;
	private int pageId;

	// ------------------------------

	@Override
	public void bind(Component component) {
		appKey = component.getApplication().getApplicationKey();
		pageId = component.getPage().getPageId();
	}

	@Override
	public void afterRender(Component component) {
		sessionId = WebSession.get().getId();
	}

	// ---------------

	public void push(String text) {
		Application app = Application.get(appKey);
		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(app);
		IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
		IWebSocketConnection connection = registry.getConnection(
			app,
			sessionId,
			new PageIdKey(pageId)
		);

		try {
			if (connection != null && connection.isOpen()) {
				connection.sendMessage(text);
			} else {
				logger.warn("WTextAsyncBehavior.push: broken connection text=[{}]", text);
			}
		} catch (IOException e) {
			throw new WicketRuntimeException(e);
		}
	}
}

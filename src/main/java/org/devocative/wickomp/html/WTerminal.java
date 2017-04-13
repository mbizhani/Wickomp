package org.devocative.wickomp.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.AbortedMessage;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.TextMessage;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.async.WTextAsyncBehavior;
import org.devocative.wickomp.wrcs.HeaderBehavior;
import org.devocative.wickomp.wrcs.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WTerminal extends WebMarkupContainer {
	private static final long serialVersionUID = -7449436253816645516L;

	private static Logger logger = LoggerFactory.getLogger(WTerminal.class);

	private static HeaderItem JS = Resource.getCommonJS("xterm/xterm.js");
	private static HeaderItem CSS = Resource.getCommonCSS("xterm/xterm.css");

	// ------------------------------

	private WTextAsyncBehavior textAsyncBehavior;

	// ------------------------------

	public WTerminal(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	// ------------------------------

	public void push(String message) {
		textAsyncBehavior.push(message);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(CSS);
		response.render(JS);
	}

	// ------------------------------

	protected abstract void onConnect();

	protected abstract void onMessage(String key, Integer specialKey);

	protected abstract void onClose();

	// ---------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		textAsyncBehavior = new WTextAsyncBehavior() {
			private static final long serialVersionUID = 2348707601617837513L;

			@Override
			protected void onConnect(ConnectedMessage message) {
				logger.info("WebSocketAsyncBehavior.onConnect: msg={}", message);
				WTerminal.this.onConnect();
			}

			@Override
			protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
				String text = message.getText();
				logger.debug("WebSocketAsyncBehavior.onMessage: {}", text);

				String key = null;
				Integer specialKey = null;
				try {
					String[] split = text.split(":", 2);
					if (split.length == 2) {
						if ("key".equals(split[0])) {
							key = split[1];
						} else if ("specialKey".equals(split[0])) {
							specialKey = Integer.parseInt(split[1]);
						}
					}

					logger.debug("Key=[{}], SpecialKey=[{}]", key, specialKey);

					WTerminal.this.onMessage(key, specialKey);
				} catch (Exception e) {
					logger.error("onMessage: ", e);
				}
			}

			@Override
			protected void onClose(ClosedMessage message) {
				logger.warn("WebSocketAsyncBehavior.onClose: {}", message);
				WTerminal.this.onClose();
			}

			@Override
			protected void onAbort(AbortedMessage message) {
				logger.warn("WebSocketAsyncBehavior.onAbort: {}", message);
			}
		};
		add(textAsyncBehavior);

		add(new HeaderBehavior("main/wTerminal.js"));
		add(new AttributeModifier("style", "direction:ltr;overflow-x:hidden;"));
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		WebUtil.writeJQueryCall(String.format("$('#%s').wTerminal();", getMarkupId()), false);
	}
}

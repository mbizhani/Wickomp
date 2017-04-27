package org.devocative.wickomp.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.AbortedMessage;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.TextMessage;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.async.WTextAsyncBehavior;
import org.devocative.wickomp.wrcs.HeaderBehavior;
import org.devocative.wickomp.wrcs.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public abstract class WTerminal extends WebMarkupContainer {
	private static final long serialVersionUID = -7449436253816645516L;

	private static final Logger logger = LoggerFactory.getLogger(WTerminal.class);
	private static final String PREFIX = "W.W_TERMINAL:";

	private static HeaderItem JS = Resource.getCommonJS("xterm/xterm.js");
	private static HeaderItem CSS = Resource.getCommonCSS("xterm/xterm.css");

	// ------------------------------

	private String clientTermId;
	private WTextAsyncBehavior textAsyncBehavior;

	// ------------------------------

	public WTerminal(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	// ------------------------------

	public void push(String message) {
		textAsyncBehavior.push(WebUtil.toJson(new ResponseMessage(message)));
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

		clientTermId = UUID.randomUUID().toString().replaceAll("[-]", "");
		logger.info("Creating WTerminal: clientTermId=[{}]", clientTermId);

		textAsyncBehavior = new WTextAsyncBehavior() {
			private static final long serialVersionUID = 2348707601617837513L;

			@Override
			protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
				String text = message.getText().trim();

				if (text.startsWith(PREFIX)) {
					try {
						RequestMessage rq = WebUtil.fromJson(text.substring(PREFIX.length()), RequestMessage.class);
						logger.debug("WTerminal Client Message: {}", rq);

						if (clientTermId.equals(rq.getCtid())) {
							if ("init".equals(rq.getCmd())) {
								WTerminal.this.onConnect();
							} else if ("key".equals(rq.getCmd())) {
								WTerminal.this.onMessage(rq.getValue(), null);
							} else if ("specialKey".equals(rq.getCmd())) {
								try {
									WTerminal.this.onMessage(null, Integer.parseInt(rq.getValue()));
								} catch (NumberFormatException e) {
									logger.error("Invalid specialKey: {}", rq.getValue());
								}
							}
						}
					} catch (Exception e) {
						logger.error("WTerminal.onMessage: ", e);
					}
				}
			}

			@Override
			protected void onClose(ClosedMessage message) {
				logger.warn("WTerminal.onClose: {}", message);
				WTerminal.this.onClose();
			}

			@Override
			protected void onAbort(AbortedMessage message) {
				logger.warn("WTerminal.onAbort: {}", message);
			}
		};
		add(textAsyncBehavior);

		add(new HeaderBehavior("main/wTerminal.js"));
		add(new AttributeModifier("style", "direction:ltr;overflow-x:hidden;"));
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			WebUtil.writeJQueryCall(String.format("$('#%s').wTerminal('%s');", getMarkupId(), clientTermId), false);
		}
	}

	// ------------------------------

	private static class RequestMessage {
		private String ctid;
		private String cmd;
		private String value;

		public String getCtid() {
			return ctid;
		}

		public void setCtid(String ctid) {
			this.ctid = ctid;
		}

		public String getCmd() {
			return cmd;
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.format("ctid=%s, cmd=%s, value=%s", getCtid(), getCmd(), getValue());
		}
	}

	private class ResponseMessage {
		private String text;

		public ResponseMessage(String text) {
			this.text = text;
		}

		public String getCtid() {
			return clientTermId;
		}

		public String getText() {
			return text;
		}
	}
}

package org.devocative.wickomp.html;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.AbortedMessage;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.TextMessage;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.async.WTextAsyncBehavior;
import org.devocative.wickomp.opt.Options;
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
	private static HeaderItem RESIZE_JS = Resource.getCommonJS("xterm/ResizeSensor.js");
	private static HeaderItem CSS = Resource.getCommonCSS("xterm/xterm.css");

	// ------------------------------

	private WTextAsyncBehavior textAsyncBehavior;
	private OTerminal oTerminal = new OTerminal();

	// ------------------------------

	public WTerminal(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	// ------------------------------

	public WTerminal setCharWidth(int charWidth) {
		oTerminal.charWidth = charWidth;
		return this;
	}

	public WTerminal setCharHeight(int charHeight) {
		oTerminal.charHeight = charHeight;
		return this;
	}

	// ---------------

	public void push(String message) {
		textAsyncBehavior.push(PREFIX + WebUtil.toJson(new ResponseMessage(message)));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(CSS);
		response.render(JS);
		response.render(RESIZE_JS);
	}

	// ------------------------------

	protected abstract void onConnect(int cols, int rows, int width, int height);

	protected abstract void onResize(int cols, int rows, int width, int height);

	protected abstract void onMessage(String key, Integer specialKey);

	protected abstract void onClose();

	// ---------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		oTerminal.clientTermId = UUID.randomUUID().toString().replaceAll("[-]", "");
		logger.info("Creating WTerminal: clientTermId=[{}]", oTerminal.clientTermId);

		textAsyncBehavior = new WTextAsyncBehavior() {
			private static final long serialVersionUID = 2348707601617837513L;

			@Override
			protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
				String text = message.getText().trim();

				if (text.startsWith(PREFIX)) {
					try {
						RequestMessage rq = WebUtil.fromJson(text.substring(PREFIX.length()), RequestMessage.class);
						logger.debug("WTerminal Client Message: {}", rq);

						if (oTerminal.clientTermId.equals(rq.getCtid())) {
							if ("init".equals(rq.getCmd())) {
								Size size = WebUtil.fromJson(rq.getValue(), Size.class);
								logger.info("WTerminal.onConnect: c=[{}] r=[{}] w=[{}] h=[{}]",
									size.cols, size.rows, size.cols * oTerminal.charWidth, size.rows * oTerminal.charHeight);
								WTerminal.this.onConnect(
									size.cols,
									size.rows,
									size.cols * oTerminal.charWidth,
									size.rows * oTerminal.charHeight);
							} else if ("resize".equals(rq.getCmd())) {
								Size size = WebUtil.fromJson(rq.getValue(), Size.class);
								logger.debug("WTerminal.onResize: c=[{}] r=[{}] w=[{}] h=[{}]",
									size.cols, size.rows, size.cols * oTerminal.charWidth, size.rows * oTerminal.charHeight);
								WTerminal.this.onResize(
									size.cols,
									size.rows,
									size.cols * oTerminal.charWidth,
									size.rows * oTerminal.charHeight);
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
		add(new AttributeAppender("style", ";direction:ltr;overflow-x:hidden;"));
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			WebUtil.writeJQueryCall(String.format("$('#%s').wTerminal(%s);", getMarkupId(), WebUtil.toJson(oTerminal)), true);
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

	private static class Size {
		private int cols;
		private int rows;

		public int getCols() {
			return cols;
		}

		public void setCols(int cols) {
			this.cols = cols;
		}

		public int getRows() {
			return rows;
		}

		public void setRows(int rows) {
			this.rows = rows;
		}
	}

	private class ResponseMessage {
		private String text;

		public ResponseMessage(String text) {
			this.text = text;
		}

		public String getCtid() {
			return oTerminal.clientTermId;
		}

		public String getText() {
			return text;
		}
	}

	private class OTerminal extends Options {
		private static final long serialVersionUID = 4476540410344233327L;

		private String clientTermId;
		private int charWidth = 10;
		private int charHeight = 18;

		public String getClientTermId() {
			return clientTermId;
		}

		public int getCharWidth() {
			return charWidth;
		}

		public int getCharHeight() {
			return charHeight;
		}
	}
}

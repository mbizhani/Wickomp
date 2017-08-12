package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.async.BroadcastCaptureBehavior;
import org.devocative.wickomp.async.WebSocketToken;
import org.devocative.wickomp.html.WAjaxLink;
import org.devocative.wickomp.html.window.WModalWindow;

public class WebSockPage extends BasePage {
	private static final long serialVersionUID = 6252801009951145937L;

	private Label label222;
	private Label label333;
	private NumberTextField<Integer> no;
	private WModalWindow wsModal;
	private WebSocketToken token;

	public WebSockPage() {
		token = WebUtil.createWSToken(this);

		label222 = new Label("label222", "1");
		label222.setOutputMarkupId(true);
		add(label222);

		label333 = new Label("label333", "-");
		label333.setOutputMarkupId(true);
		add(label333);

		wsModal = new WModalWindow("wsModal");
		add(wsModal);

		add(new AjaxLink("showTime") {
			private static final long serialVersionUID = -6319449976103566284L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				wsModal.setContent(new TimerPanel(wsModal.getContentId()));
				wsModal.show(target);
			}
		});

		add(new WAjaxLink("testWebUtilSendWS") {
			private static final long serialVersionUID = -4558794042037507951L;

			@Override
			public void onClick(AjaxRequestTarget ajaxRequestTarget) {
			}
		});

		/*add(new WebSocketBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConnect(ConnectedMessage message) {
				System.out.println("WebSockPage.onConnect");
			}

			@Override
			protected void onMessage(WebSocketRequestHandler handler, TextMessage message) {
				System.out.println("WebSockPage.onMessage: " + message);
			}

			@Override
			protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
				System.out.println("WebSockPage.onPush: " + message);

				if (message instanceof PushMessage) {
					PushMessage msg = (PushMessage) message;

					label222.setDefaultModelObject(msg.getNo());
					handler.add(label222);
					if (msg.getNo() % 5 == 0) {
						handler.appendJavaScript(String.format("console.log('%s');", msg.getNo()));
					}
				}
			}
		});*/

		add(new BroadcastCaptureBehavior<Integer>(Integer.class) {
			private static final long serialVersionUID = -3583030292765930785L;

			@Override
			protected void onMessage(IPartialPageRequestHandler handler, Integer message) {
				label222.setDefaultModelObject(message);
				handler.add(label222);
				if (message % 5 == 0) {
					handler.appendJavaScript(String.format("console.log('%s');", message));
				}
			}
		});
	}

	/*
	The following usage is also possible:


	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof WebSocketPushPayload) {
			WebSocketPushPayload wsEvent = (WebSocketPushPayload) event.getPayload();
			PushMessage msg = (PushMessage) wsEvent.getMessage();
			label222.setDefaultModelObject(msg.getNo());
			wsEvent.getHandler().add(label222);
			if (msg.getNo() % 5 == 0) {
				wsEvent.getHandlerId().appendJavaScript(String.format("console.log('%s');", msg.getNo()));
			}
		}
	}*/

	public class TimerPanel extends WPanel {
		private static final long serialVersionUID = -3394483728053039113L;
		private Label timeLabel;

		public TimerPanel(String id) {
			super(id);

			timeLabel = new Label("timeLabel", "-");
			timeLabel.setOutputMarkupId(true);
			add(timeLabel);
		}
	}
}

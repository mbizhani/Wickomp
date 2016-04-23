package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.async.BroadcastCaptureBehavior;
import org.devocative.wickomp.form.WAsyncAjaxButton;
import org.devocative.wickomp.html.WAsyncAjaxLink;
import org.devocative.wickomp.html.window.WModalWindow;

import java.io.Serializable;

public class WebSockPage extends BasePage {
	private Label label222;
	private Label label333;
	private NumberTextField<Integer> no;
	private WModalWindow wsModal;

	public WebSockPage() {
		label222 = new Label("label222", "1");
		label222.setOutputMarkupId(true);
		add(label222);

		label333 = new Label("label333", "-");
		label333.setOutputMarkupId(true);
		add(label333);

		Form form = new Form("form");
		form.add(no = new NumberTextField<>("no", new Model<>(1), Integer.class));
		/*form.add(new WAjaxButton("send") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
			}
		});*/
		form.add(new WAsyncAjaxButton("send") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				sendAsyncRequest("COUNTER", no.getModelObject());
			}

			@Override
			public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result) {
				label333.setDefaultModelObject(result);
				handler.add(label333);
			}
		});
		add(form);

		wsModal = new WModalWindow("wsModal");
		add(wsModal);

		add(new AjaxLink("showTime") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				wsModal.setContent(new TimerPanel(wsModal.getContentId()));
				wsModal.show(target);
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
		private Label timeLabel;

		public TimerPanel(String id) {
			super(id);

			timeLabel = new Label("timeLabel", "-");
			timeLabel.setOutputMarkupId(true);
			add(timeLabel);

			add(new WAsyncAjaxLink("time") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					sendAsyncRequest("NTP", null);
				}

				@Override
				public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result) {
					timeLabel.setDefaultModelObject(result);
					handler.add(timeLabel);
				}
			});
		}
	}
}

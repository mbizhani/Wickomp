package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.async.BroadcastCaptureBehavior;
import org.devocative.wickomp.form.WAsyncAjaxButton;

import java.io.Serializable;

public class WebSockPage extends BasePage {
	private Label label222;
	private Label label333;
	private NumberTextField<Integer> no;

	public WebSockPage() {
		label222 = new Label("label222", "1");
		label222.setOutputMarkupId(true);
		add(label222);

		label333 = new Label("label333", "-");
		label333.setOutputMarkupId(true);
		add(label333);

		Form form = new Form("form");
		form.add(no = new NumberTextField<>("no", new Model<>(1), Integer.class));
		form.add(new WAsyncAjaxButton("send") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				sendAsyncRequest("COUNTER", no.getModelObject());
			}

			@Override
			public void onAsyncResult(IPartialPageRequestHandler handler, Serializable result) {
				label333.setDefaultModelObject(result);
				handler.add(label333);
			}
		});
		add(form);

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
				wsEvent.getHandler().appendJavaScript(String.format("console.log('%s');", msg.getNo()));
			}
		}
	}*/
}

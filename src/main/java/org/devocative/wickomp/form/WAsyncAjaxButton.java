package org.devocative.wickomp.form;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.async.AsyncBehavior;
import org.devocative.wickomp.async.AsyncMediator;
import org.devocative.wickomp.async.IAsyncAction;
import org.devocative.wickomp.html.HTMLBase;

import java.io.Serializable;

public abstract class WAsyncAjaxButton extends WAjaxButton implements IAsyncAction {
	private AsyncBehavior asyncBehavior;

	public WAsyncAjaxButton(String id) {
		this(id, null, null);
	}

	public WAsyncAjaxButton(String id, IModel<String> caption) {
		this(id, caption, null);
	}

	public WAsyncAjaxButton(String id, IModel<String> caption, HTMLBase icon) {
		super(id, caption, icon);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(asyncBehavior = new AsyncBehavior(this));
	}

	public abstract void onAsyncResult(IPartialPageRequestHandler handler, Serializable result);

	@Override
	public void sendAsyncRequest(String handler, Object requestPayLoad) {
		AsyncMediator.sendRequest(handler, asyncBehavior.getAsyncToken(), requestPayLoad);
	}
}

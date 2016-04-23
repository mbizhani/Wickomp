package org.devocative.wickomp.form;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.async.AsyncBehavior;
import org.devocative.wickomp.async.IAsyncAction;
import org.devocative.wickomp.html.HTMLBase;

public abstract class WAsyncAjaxButton extends WAjaxButton implements IAsyncAction {
	private AsyncBehavior asyncBehavior;

	// ---------------------- CONSTRUCTORS

	public WAsyncAjaxButton(String id) {
		this(id, null, null);
	}

	public WAsyncAjaxButton(String id, IModel<String> caption) {
		this(id, caption, null);
	}

	// Main Constructor
	public WAsyncAjaxButton(String id, IModel<String> caption, HTMLBase icon) {
		super(id, caption, icon);
	}

	// ---------------------- PUBLIC METHODS

	@Override
	public void sendAsyncRequest(String handlerId, Object requestPayLoad) {
		asyncBehavior.sendAsyncRequest(handlerId, requestPayLoad);
	}

	// ---------------------- PROTECTED METHODS

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(asyncBehavior = new AsyncBehavior(this));
	}
}

package org.devocative.wickomp.html;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.async.AsyncBehavior;
import org.devocative.wickomp.async.IAsyncAction;

public abstract class WAsyncAjaxLink extends WAjaxLink implements IAsyncAction {
	private AsyncBehavior asyncBehavior;

	public WAsyncAjaxLink(String id) {
		this(id, null, null);
	}

	public WAsyncAjaxLink(String id, IModel<String> caption) {
		this(id, caption, null);
	}

	// Main Constructor
	public WAsyncAjaxLink(String id, IModel<String> caption, HTMLBase icon) {
		super(id, caption, icon);
	}

	@Override
	public void sendAsyncRequest(String handlerId, Object requestPayLoad) {
		asyncBehavior.sendAsyncRequest(handlerId, requestPayLoad);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(asyncBehavior = new AsyncBehavior(this));
	}
}

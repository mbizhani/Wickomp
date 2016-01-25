package org.devocative.wickomp.grid.column.link;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.devocative.wickomp.html.Anchor;
import org.devocative.wickomp.html.HTMLBase;

public abstract class OLinkColumn<T> extends OCallbackColumn<T> {
	private boolean targetBlank = false;

	protected OLinkColumn(IModel<String> text, HTMLBase linkContent) {
		super(text, linkContent);
	}

	protected OLinkColumn(IModel<String> text, String field) {
		super(text, field);
	}

	public OLinkColumn<T> setTargetBlank(boolean targetBlank) {
		this.targetBlank = targetBlank;
		return this;
	}

	public abstract void onClick(IModel<T> rowData);

	@Override
	protected void fillAnchor(Anchor anchor, T bean, String id, int colNo, String url) {
		anchor.setHref(url);
		if (targetBlank) {
			anchor.setTarget(Anchor.ETarget._blank);
		}
	}

	protected void sendResource(IResource resource) {
		sendResource(resource, null);
	}

	protected void sendResource(IResource resource, PageParameters pageParameters) {
		RequestCycle cycle = RequestCycle.get();
		cycle.scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(resource, pageParameters));
	}
}

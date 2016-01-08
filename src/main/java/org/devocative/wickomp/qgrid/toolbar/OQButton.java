package org.devocative.wickomp.qgrid.toolbar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.devocative.wickomp.opt.Options;

public abstract class OQButton<T> extends Options {
	private int index;
	private String url;

	public final void setIndex(int index) {
		this.index = index;
	}

	public final void setUrl(String url) {
		this.url = url;
	}

	public void onClick(WQGridInfo<T> gridInfo, IRequestParameters parameters) {
		throw new RuntimeException("Called while no impl!");
	}

	public void onClick(AjaxRequestTarget target, WQGridInfo<T> gridInfo, IRequestParameters parameters) {
		throw new RuntimeException("Called while no impl!");
	}

	public abstract String getHTMLContent(WQGridInfo<T> gridInfo);

	protected final String getCallbackURL() {
		return String.format("%s&cn=%s&tp=bt", url, index);
	}

	////////////////////////////////////// HELPER

	protected final void sendResource(IResource resource) {
		sendResource(resource, null);
	}

	protected final void sendResource(IResource resource, PageParameters pageParameters) {
		RequestCycle cycle = RequestCycle.get();
		cycle.scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(resource, pageParameters));
	}
}

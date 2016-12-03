package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.devocative.wickomp.grid.WBaseGrid;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.opt.Options;

public abstract class OButton<T> extends Options {
	private static final long serialVersionUID = 4808428345821267063L;

	private int index;
	private String url;
	private String gridHtmlId;
	private OColumnList<T> columnList;

	// ------------------------------

	public void init(String url, int index, String gridHtmlId, OColumnList<T> columnList) {
		this.url = url;
		this.index = index;
		this.gridHtmlId = gridHtmlId;
		this.columnList = columnList;
	}

	public String getGridHtmlId() {
		return gridHtmlId;
	}

	public OColumnList<T> getColumnList() {
		return columnList;
	}

	// ------------------------------

	public abstract String getHTMLContent();

	// ------------------------------

	protected final String getCallbackURL() {
		//return String.format("%s&cn=%s&tp=bt", url, index);
		return String.format("%s&%s=%s&%s=%s", url, WBaseGrid.URL_PARAM_COLUMN_NUMBER, index,
			WBaseGrid.URL_PARAM_CLICK_TYPE, WBaseGrid.CLICK_FROM_BUTTON);
	}

	protected final void sendResource(IResource resource) {
		sendResource(resource, null);
	}

	protected final void sendResource(IResource resource, PageParameters pageParameters) {
		RequestCycle cycle = RequestCycle.get();
		cycle.scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(resource, pageParameters));
	}
}

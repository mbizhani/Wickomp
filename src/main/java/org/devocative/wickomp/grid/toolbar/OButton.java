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

	protected static final String TOOLBAR_BUT_HTML_CLASS = "w-grid-tbar-but";

	private int index;
	private String url;
	private String gridHtmlId;

	private transient WBaseGrid<T> grid;

	// ------------------------------

	public final void init(String url, int index, String gridHtmlId) {
		this.url = url;
		this.index = index;
		this.gridHtmlId = gridHtmlId;
	}

	public final void setGrid(WBaseGrid<T> grid) {
		this.grid = grid;
	}

	// ------------------------------

	public abstract String getHTMLContent();

	// ------------------------------

	protected final String getGridHtmlId() {
		return gridHtmlId;
	}

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

	protected final OColumnList<T> getColumnList() {
		return grid.getOptions().getColumns();
	}

	protected final Integer getPageSize() {
		return grid.getPageSize();
	}

	protected final Integer getPageNum() {
		return grid.getPageNum();
	}
}

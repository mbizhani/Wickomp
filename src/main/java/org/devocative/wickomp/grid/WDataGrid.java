package org.devocative.wickomp.grid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.devocative.adroit.JsonUtil;
import org.devocative.wickomp.WCallbackComponent;
import org.devocative.wickomp.data.ODataSource;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WDataGrid<T extends Serializable> extends WCallbackComponent {
	private OGrid<T> options;
	private ODataSource<T> dataSource;
	private List<IModel<T>> pageData = new ArrayList<IModel<T>>();


	public WDataGrid(String id, OGrid<T> options, ODataSource<T> dataSource) {
		super(id, options);

		this.options = options;
		this.dataSource = dataSource;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// It should be called in onBeforeRender, not worked in onInitialize, causing StalePageException
		options.setUrl(getCallbackURL());
	}

	@Override
	protected void onRequest(IRequestParameters parameters) {
		int pageSize = parameters.getParameterValue("rows").toInt(1);
		int pageNum = parameters.getParameterValue("page").toInt(options.getPageSize());

		Integer rowNo = parameters.getParameterValue("rn").toOptionalInteger();
		Integer colNo = parameters.getParameterValue("cn").toOptionalInteger();

		if (rowNo != null && colNo != null) {
			IModel<T> rowModel = pageData.get(rowNo);
			OColumn<T> column = options.getColumns().getList().get(colNo);
			if (column instanceof OLinkColumn) {
				OLinkColumn<T> linkColumn = (OLinkColumn<T>) column;
				linkColumn.onClick(rowModel);
			} else if (column instanceof OAjaxLinkColumn) {
				WebApplication app = (WebApplication) getApplication();
				AjaxRequestTarget target = app.newAjaxRequestTarget(getPage());
				RequestCycle requestCycle = RequestCycle.get();
				requestCycle.scheduleRequestHandlerAfterCurrent(target);

				OAjaxLinkColumn<T> ajaxLinkColumn = (OAjaxLinkColumn<T>) column;
				ajaxLinkColumn.onClick(target, rowModel);
			}
		} else {
			List<T> data = dataSource.list(pageNum, pageSize);
			long count = dataSource.count();

			RGridPage result = new RGridPage();
			result.setRows(getPageData(data));
			result.setTotal(count);

			sendJSONResponse(JsonUtil.toJson(result));
		}
	}

	@Override
	protected String getJQueryFunction() {
		return "datagrid";
	}

	private List<RObject> getPageData(List<T> list) {
		pageData.clear();
		List<RObject> page = new ArrayList<RObject>();
		for (int rowNo = 0; rowNo < list.size(); rowNo++) {
			T bean = list.get(rowNo);
			pageData.add(dataSource.model(bean));

			RObject map = new RObject();
			for (int colNo = 0; colNo < options.getColumns().getList().size(); colNo++) {
				OColumn<T> column = options.getColumns().getList().get(colNo);
				if (column.onCellRender(bean, rowNo)) {
					String url = String.format("%s&rn=%s&cn=%s", getCallbackURL(), rowNo, colNo);
					map.addProperty(column.getField(), column.cellValue(bean, rowNo, colNo, url));
				}
			}
			page.add(map);
		}
		return page;
	}
}

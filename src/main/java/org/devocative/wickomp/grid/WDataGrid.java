package org.devocative.wickomp.grid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.WCallbackComponent;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.WDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.grid.toolbar.WGridInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WDataGrid<T> extends WCallbackComponent {
	private static final Logger logger = LoggerFactory.getLogger(WDataGrid.class);

	private OGrid<T> options;
	private WDataSource<T> dataSource;
	private List<WSortField> sortFieldList = new ArrayList<>();
	private List<IModel<T>> pageData = new ArrayList<>();

	public WDataGrid(String id, OGrid<T> options, WDataSource<T> dataSource) {
		super(id, options);

		this.options = options;
		this.dataSource = dataSource;
	}

	public OGrid<T> getOptions() {
		return options;
	}

	public WDataSource<T> getDataSource() {
		return dataSource;
	}

	public void loadData(AjaxRequestTarget target) {
		if (dataSource.isEnabled()) {
			target.appendJavaScript(String.format("$(\"#%1$s\").%2$s(\"options\")[\"url\"]=\"%3$s\";$(\"#%1$s\").%2$s(\"reload\");",
				getMarkupId(), getJQueryFunction(), getCallbackURL()));
		}
	}

	@Override
	protected String getJQueryFunction() {
		return "datagrid";
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// It should be called in onBeforeRender, not worked in onInitialize, causing StalePageException
		if (!dataSource.isEnabled()) {
			options.setUrl(null);
		} else {
			options.setUrl(getCallbackURL());
		}

		int i = 0;
		for (OColumn<T> column : options.getColumns().getList()) {
			if (column.getField() == null) {
				column
					.setField("f" + (i++))
					.setDummyField(true);
			}
		}

		if (options.getToolbar() != null) {
			for (OButton button : options.getToolbar()) {
				button.setUrl(getCallbackURL());
			}
		}
	}

	@Override
	protected void onRequest(IRequestParameters parameters) {
		int pageSize = parameters.getParameterValue("rows").toInt(options.getPageSize());
		int pageNum = parameters.getParameterValue("page").toInt(1);

		String sortList = parameters.getParameterValue("sort").toOptionalString();
		String orderList = parameters.getParameterValue("order").toOptionalString();

		String type = parameters.getParameterValue("tp").toString();
		Integer rowNo = parameters.getParameterValue("rn").toOptionalInteger();
		Integer colNo = parameters.getParameterValue("cn").toOptionalInteger();

		logger.debug("WDataGrid: onRequest.parameters: type={}, pageSize={}, pageNum={}, sort={}, order={}, rowNo={}, colNo={}",
			type, pageSize, pageNum, sortList, orderList, rowNo, colNo);

		if ("cl".equals(type)) {
			if (rowNo == null) {
				throw new RuntimeException("Null rowNo parameter!");
			}
			if (colNo == null) {
				throw new RuntimeException("Null colNo parameter!");
			}
			handleCellLinkClick(rowNo, colNo);
		} else if ("bt".equals(type)) {
			if (colNo == null) {
				throw new RuntimeException("Null button index parameter!");
			}
			handleToolbarButtonClick(colNo);
		} else {
			if (sortList != null && orderList != null) {
				updateSortFieldList(sortList.split(","), orderList.split(","));
			} else {
				sortFieldList.clear();
			}

			logger.debug("WDataGrid: SortFields = {}", sortFieldList);

			List<T> data = dataSource.list(pageNum, pageSize, sortFieldList);
			long count = dataSource.count();

			RGridPage result = new RGridPage();
			result.setRows(getPageData(data));
			result.setTotal(count);

			sendJSONResponse(JsonUtil.toJson(result));
		}
	}

	private void handleToolbarButtonClick(Integer colNo) {
		OButton<T> button = options.getToolbar().get(colNo);
		button.onClick(new WGridInfo<>(options.getColumns().getList(), dataSource, sortFieldList));
	}

	private void handleCellLinkClick(Integer rowNo, Integer colNo) {
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
	}

	private List<RObject> getPageData(List<T> list) {
		pageData.clear();
		List<RObject> page = new ArrayList<>();
		if (dataSource.isEnabled()) {
			for (int rowNo = 0; rowNo < list.size(); rowNo++) {
				T bean = list.get(rowNo);
				pageData.add(dataSource.model(bean));

				RObject map = new RObject();
				for (int colNo = 0; colNo < options.getColumns().getList().size(); colNo++) {
					OColumn<T> column = options.getColumns().getList().get(colNo);
					if (column.onCellRender(bean, rowNo)) {
						String url = String.format("%s&rn=%s&cn=%s&tp=cl", getCallbackURL(), rowNo, colNo);
						map.addProperty(column.getField(), column.cellValue(bean, rowNo, colNo, url));
					}
				}
				page.add(map);
			}
		}
		return page;
	}

	private void updateSortFieldList(String[] sortList, String[] orderList) {
		sortFieldList.clear();

		for (int i = 0; i < sortList.length; i++) {
			sortFieldList.add(new WSortField(sortList[i], orderList[i]));
		}
	}
}

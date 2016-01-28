package org.devocative.wickomp.grid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.WCallbackComponent;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.WGridDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.grid.toolbar.WGridInfo;
import org.devocative.wickomp.wrcs.EasyUIBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WBaseGrid<T> extends WCallbackComponent {
	private static final Logger logger = LoggerFactory.getLogger(WBaseGrid.class);

	private OBaseGrid<T> options;
	private WGridDataSource<T> dataSource;
	private List<WSortField> sortFieldList = new ArrayList<>();
	private Map<String, IModel<T>> pageData = new HashMap<>();

	public WBaseGrid(String id, OBaseGrid<T> options, WGridDataSource<T> dataSource) {
		super(id, options);

		this.dataSource = dataSource;
		this.options = options;

		add(new FontAwesomeBehavior());
		add(new EasyUIBehavior());
	}

	// ------------------------- ACCESSORS

	public OBaseGrid<T> getOptions() {
		return options;
	}

	public WGridDataSource<T> getDataSource() {
		return dataSource;
	}

	// ------------------------- METHODS

	public void loadData(AjaxRequestTarget target) {
		if (dataSource.isEnabled()) {
			target.appendJavaScript(String.format("$(\"#%1$s\").%2$s(\"options\")[\"url\"]=\"%3$s\";$(\"#%1$s\").%2$s(\"reload\");",
				getMarkupId(), getJQueryFunction(), getCallbackURL()));
		}
	}

	// ------------------------- INTERNAL METHODS

	@Override
	protected void onInitialize() {
		super.onInitialize();

		int i = 0;
		for (OColumn<T> column : options.getColumns().getList()) {
			if (column.getField() == null) {
				column
					.setField("f" + (i++))
					.setDummyField(true);
			}
		}
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
		options.setGridHTMLId(getMarkupId());

		if (options.getToolbarButtons() != null) {
			for (OButton button : options.getToolbarButtons()) {
				button.setUrl(getCallbackURL());
			}
		}
	}

	@Override
	protected void onRequest(IRequestParameters parameters) {

		if (!dataSource.isEnabled()) {
			return;
		}

		int pageSize = parameters.getParameterValue("rows").toInt(options.getPageSize());
		int pageNum = parameters.getParameterValue("page").toInt(1);
		String id = parameters.getParameterValue("id").toOptionalString();


		String sortList = parameters.getParameterValue("sort").toOptionalString();
		String orderList = parameters.getParameterValue("order").toOptionalString();

		String clickType = parameters.getParameterValue("tp").toString();
		Integer colNo = parameters.getParameterValue("cn").toOptionalInteger();

		logger.debug("WBaseGrid: onRequest.parameters: clickType={}, pageSize={}, pageNum={}, sort={}, order={}, id={}, colNo={}",
			clickType, pageSize, pageNum, sortList, orderList, id, colNo);

		if ("cl".equals(clickType)) {// click from cell (per row)
			if (id == null) {
				throw new RuntimeException("Null id parameter!");
			}
			if (colNo == null) {
				throw new RuntimeException("Null colNo parameter!");
			}
			handleCellLinkClick(id, colNo);
		} else if ("bt".equals(clickType)) {// click from button in toolbar
			if (colNo == null) {
				throw new RuntimeException("Null button index parameter!");
			}
			handleToolbarButtonClick(colNo, parameters);
		} else if (id != null) {
			handleRowsById(id);
		} else {
			if (sortList != null && orderList != null) {
				updateSortFieldList(sortList.split(","), orderList.split(","));
			} else {
				sortFieldList.clear();
			}

			logger.debug("WBaseGrid: SortFields = {}", sortFieldList);

			List<T> data = dataSource.list(pageNum, pageSize, sortFieldList);
			long count = dataSource.count();

			RGridPage result = new RGridPage();
			result.setRows(getPageData(data));
			result.setTotal(count);

			sendJSONResponse(JsonUtil.toJson(result));
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			List<OButton<T>> toolbarButtons = options.getToolbarButtons();
			if (toolbarButtons != null) {
				WGridInfo<T> info = new WGridInfo<>(options, dataSource, sortFieldList);
				StringBuilder builder = new StringBuilder();
				builder
					.append(String.format("<div id=\"%s-tb\">", getMarkupId()))
					.append("<table><tr>");
				for (OButton<T> button : toolbarButtons) {
					builder.append("<td>").append(button.getHTMLContent(info)).append("</td>");
				}
				builder.append("</tr></table></div>");

				getResponse().write(builder.toString());
			}
		}
	}

	protected void onBeanToRObject(T bean, RObject rObject) {
	}

	protected void handleRowsById(String id) {
	}

	private void handleToolbarButtonClick(Integer colNo, IRequestParameters parameters) {
		OButton<T> button = options.getToolbarButtons().get(colNo);
		button.onClick(new WGridInfo<>(options, dataSource, sortFieldList), parameters);
	}

	private void handleCellLinkClick(String id, Integer colNo) {
		IModel<T> rowModel = pageData.get(id);
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

				String id = String.valueOf(rowNo);
				if (options.getIdField() != null) {
					Object idValue = PropertyResolver.getValue(options.getIdField(), bean);
					if (idValue == null) {
						throw new RuntimeException(String.format("Null value for id: idField=[%s] bean=[%s]",
							options.getIdField(), bean));
					}
					id = idValue.toString();
				}
				pageData.put(id, dataSource.model(bean));

				RObject rObject = new RObject();
				for (int colNo = 0; colNo < options.getColumns().getList().size(); colNo++) {
					OColumn<T> column = options.getColumns().getList().get(colNo);
					if (column.onCellRender(bean, id)) {
						String url = String.format("%s&id=%s&cn=%s&tp=cl", getCallbackURL(), id, colNo);
						rObject.addProperty(column.getField(), column.cellValue(bean, id, colNo, url));
					}
				}
				onBeanToRObject(bean, rObject);
				page.add(rObject);
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
package org.devocative.wickomp.grid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.devocative.wickomp.IExceptionToMessageHandler;
import org.devocative.wickomp.WDefaults;
import org.devocative.wickomp.WJqCallbackComponent;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.RObjectList;
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

public abstract class WBaseGrid<T> extends WJqCallbackComponent {
	private static final long serialVersionUID = -2882882330275047801L;

	protected static final Logger logger = LoggerFactory.getLogger(WBaseGrid.class);

	private OBaseGrid<T> options;
	private IExceptionToMessageHandler exceptionMessageHandler = WDefaults.getExceptionToMessageHandler();

	private IDataSource<T> dataSource;
	private IGridDataSource<T> gridDataSource;
	private IGridAsyncDataSource<T> gridAsyncDataSource;
	private IGridFooterDataSource<T> footerDataSource;

	protected Integer pageSize, pageNum;
	protected List<WSortField> sortFieldList = new ArrayList<>();
	protected Map<String, IModel<T>> pageData = new HashMap<>();

	// ------------------------- CONSTRUCTORS

	public WBaseGrid(String id, OBaseGrid<T> options, IGridDataSource<T> gridDataSource) {
		super(id, options);

		this.dataSource = gridDataSource;
		this.gridDataSource = gridDataSource;
		this.options = options;
	}

	public WBaseGrid(String id, OBaseGrid<T> options, IGridAsyncDataSource<T> gridAsyncDataSource) {
		super(id, options);

		this.dataSource = gridAsyncDataSource;
		this.gridAsyncDataSource = gridAsyncDataSource;
		this.options = options;
	}

	// ------------------------- ACCESSORS

	public OBaseGrid<T> getOptions() {
		return options;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public List<WSortField> getSortFieldList() {
		return sortFieldList;
	}

	public WBaseGrid<T> setExceptionMessageHandler(IExceptionToMessageHandler exceptionMessageHandler) {
		this.exceptionMessageHandler = exceptionMessageHandler;
		return this;
	}

	public WBaseGrid<T> setFooterDataSource(IGridFooterDataSource<T> footerDataSource) {
		this.footerDataSource = footerDataSource;
		options.setShowFooter(true);
		return this;
	}

	// ------------------------- METHODS

	public WBaseGrid<T> loadData(AjaxRequestTarget target) {
		if (isEnabled()) {
			if (pageNum == null) {
				pageNum = 1;
			}

			if (pageSize == null) {
				pageSize = options.getPageSize();
			}

			RGridPage gridPage = getGridPage();

			String script = String.format(
				"$('#%1$s').%2$s('options')['url']=\"%3$s\";" +
					"$('#%1$s').%2$s('loadData', %4$s);",
				getMarkupId(), getJQueryFunction(), getCallbackURL(),
				WebUtil.toJson(gridPage));

			logger.debug("WBaseGrid.loadData(): {}", script);

			target.appendJavaScript(script);
		}
		return this;
	}

	public WBaseGrid<T> makeVisible(AjaxRequestTarget target) {
		if (!isVisible()) {
			setVisible(true);
			target.add(this);
		}
		return this;
	}

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list, long count) {
		return pushData(handler, list, count, null);
	}

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list, long count, List footer) {
		RGridPage gridPage = getGridPage(list, count);

		if (options.hasFooter()) {
			if (footer != null) {
				gridPage.setFooter(getGridFooter(footer));
			} else {
				gridPage.setFooter(new ArrayList<RObject>());
			}
		}

		String script = String.format("$('#%1$s').%2$s('loadData', %3$s);",
			getMarkupId(), getJQueryFunction(), WebUtil.toJson(gridPage));

		logger.debug("WBaseGrid.pushData(): {}", script);

		handler.appendJavaScript(script);

		return this;
	}

	public WBaseGrid<T> pushError(IPartialPageRequestHandler handler, Exception e) {
		RGridPage result = new RGridPage();
		result.setTotal((long) pageNum * pageSize);
		result.setRows(new RObjectList());
		result.setError(exceptionMessageHandler.handleMessage(this, e));

		String script = String.format("$('#%1$s').%2$s('loadData', %3$s);",
			getMarkupId(), getJQueryFunction(), WebUtil.toJson(result));

		logger.debug("WBaseGrid.pushError(): {}", script);

		handler.appendJavaScript(script);

		return this;
	}

	// ------------------------- INTERNAL METHODS

	@Override
	protected void onInitialize() {
		super.onInitialize();

		int i = 0;
		for (OColumn<T> column : options.getColumns().getAllColumns()) {
			if (column.getField() == null) {
				column
					.setField("f" + (i++))
					.setDummyField(true);
			}
		}

		options.getColumns().validate();

		add(new FontAwesomeBehavior());
		add(new EasyUIBehavior());
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// It should be called in onBeforeRender, not worked in onInitialize, causing StalePageException
		if (!isEnabled()) {
			options.setUrl(null);
		}
		/*
		The following is set by IHtmlId & ICallbackUrl

		else {
			options.setUrl(getCallbackURL());
		}
		options.setHtmlId(getMarkupId());
		*/

		if (options.getToolbarButtons() != null) {
			for (OButton button : options.getToolbarButtons()) {
				button.setUrl(getCallbackURL());
			}
		}
	}

	@Override
	protected void onRequest(IRequestParameters parameters) {

		pageSize = parameters.getParameterValue("rows").toInt(options.getPageSize());
		pageNum = parameters.getParameterValue("page").toInt(1);

		if (!isEnabled()) {
			return;
		}

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
		} else if (id != null && id.length() > 0) {
			handleRowsById(id);
		} else {
			if (sortList != null && orderList != null) {
				updateSortFieldList(sortList.split(","), orderList.split(","));
			} else {
				sortFieldList.clear();
			}

			logger.debug("WBaseGrid: SortFields = {}", sortFieldList);

			RGridPage result = getGridPage();
			sendJSONResponse(WebUtil.toJson(result));
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			List<OButton<T>> toolbarButtons = options.getToolbarButtons();
			if (toolbarButtons != null) {
				WGridInfo<T> info = new WGridInfo<>(options, gridDataSource, sortFieldList);
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

	protected final RGridPage getGridPage() {
		RGridPage result;
		try {
			if (gridDataSource != null) {
				List<T> data = gridDataSource.list(pageNum, pageSize, sortFieldList);

				long count;
				if (data.size() >= pageSize) {
					count = gridDataSource.count();
					if (data.size() > pageSize) {
						logger.warn("DataSource.list returns collection(size={}) greater than pageSize={}!",
							data.size(), pageSize);
					}
				} else {
					count = (pageNum - 1) * pageSize + data.size();
				}

				result = getGridPage(data, count);

				if (options.hasFooter() && footerDataSource != null) {
					result.setFooter(getGridFooter(footerDataSource.footer(data)));
				}
			} else if (gridAsyncDataSource != null) {
				gridAsyncDataSource.list(pageNum, pageSize, sortFieldList);

				result = getGridPage(null, pageNum * pageSize);
				if (options.hasFooter()) {
					result.setFooter(new ArrayList<RObject>());
				}
				result.setAsync(true);
			} else {
				throw new RuntimeException("No DataSource for grid: " + getId());
			}
		} catch (Exception e) {
			logger.warn("Grid.DataSource: id=" + getId(), e);
			result = new RGridPage();
			result.setTotal((long) pageNum * pageSize);
			result.setError(exceptionMessageHandler.handleMessage(this, e));
		}
		return result;
	}

	protected final RGridPage getGridPage(List<T> data, long count) {
		RGridPage result = new RGridPage();
		if (data != null) {
			result.setRows(createRObjectList(data));
		} else {
			result.setRows(new RObjectList());
		}
		result.setTotal(count);
		return result;
	}

	protected RObjectList createRObjectList(List<T> data) {
		pageData.clear();

		RObjectList objectList = new RObjectList();
		convertBeansToRObjects(data, objectList);
		return objectList;
	}

	protected List<RObject> getGridFooter(List<?> footerData) {
		List<RObject> footer = new ArrayList<>();

		if (footerData != null) {
			List<OColumn<T>> columns = options.getColumns().getVisibleColumns();
			for (Object bean : footerData) {
				RObject rObject = new RObject();
				for (int colNo = 0; colNo < columns.size(); colNo++) {
					OColumn<T> column = columns.get(colNo);
					if (column.isHasFooter()) {
						String url = String.format("%s&cn=%s&tp=cl", getCallbackURL(), colNo);
						rObject.addProperty(column.getField(), column.footerCellValue(bean, colNo, url));
					}
				}
				footer.add(rObject);
			}
		}

		return footer;
	}

	protected void handleRowsById(String id) {
	}

	protected void onAfterBeanToRObject(T bean, RObject rObject) {
	}

	protected final void convertBeansToRObjects(List<T> list, RObjectList page) {
		List<OColumn<T>> columns = options.getColumns().getAllColumns();

		for (int rowNo = 0; rowNo < list.size(); rowNo++) {
			T bean = list.get(rowNo);
			RObject rObject = new RObject();

			String id = String.valueOf(rowNo);
			if (options.getIdField() != null) {
				Object idValue = PropertyResolver.getValue(options.getIdField(), bean);
				if (idValue == null) {
					throw new RuntimeException(String.format("Null value for id: idField=[%s] bean=[%s]",
						options.getIdField(), bean));
				}
				id = idValue.toString();
				rObject.addProperty(options.getIdField(), id);
			}
			pageData.put(id, dataSource.model(bean));

			for (int colNo = 0; colNo < columns.size(); colNo++) {
				OColumn<T> column = columns.get(colNo);
				if (column.onCellRender(bean, id)) {
					String url = String.format("%s&id=%s&cn=%s&tp=cl", getCallbackURL(), id, colNo);
					rObject.addProperty(column.getField(), column.cellValue(bean, id, colNo, url));
				}
			}
			onAfterBeanToRObject(bean, rObject);
			page.addRObject(id, rObject);
		}
	}

	private void handleToolbarButtonClick(Integer colNo, IRequestParameters parameters) {
		OButton<T> button = options.getToolbarButtons().get(colNo);
		button.onClick(new WGridInfo<>(options, gridDataSource, sortFieldList), parameters);
	}

	private void handleCellLinkClick(String id, Integer colNo) {
		IModel<T> rowModel = pageData.get(id);
		OColumn<T> column = options.getColumns().getVisibleColumns().get(colNo);
		if (column instanceof OLinkColumn) {
			OLinkColumn<T> linkColumn = (OLinkColumn<T>) column;
			linkColumn.onClick(rowModel);
		} else if (column instanceof OAjaxLinkColumn) {
			AjaxRequestTarget target = createAjaxResponse();

			OAjaxLinkColumn<T> ajaxLinkColumn = (OAjaxLinkColumn<T>) column;
			try {
				ajaxLinkColumn.onClick(target, rowModel);
			} catch (Exception e) {
				ajaxLinkColumn.onException(target, e, rowModel);
			}

		}
	}

	private void updateSortFieldList(String[] sortList, String[] orderList) {
		sortFieldList.clear();

		for (int i = 0; i < sortList.length; i++) {
			sortFieldList.add(new WSortField(sortList[i], orderList[i]));
		}
	}
}

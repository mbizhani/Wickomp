package org.devocative.wickomp.grid;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.devocative.adroit.ObjectUtil;
import org.devocative.wickomp.IExceptionToMessageHandler;
import org.devocative.wickomp.WDefaults;
import org.devocative.wickomp.WJqCallbackComponent;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.RObjectList;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;
import org.devocative.wickomp.grid.toolbar.OAjaxLinkButton;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.grid.toolbar.OLinkButton;
import org.devocative.wickomp.opt.OStyle;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.HeaderBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.*;

public abstract class WBaseGrid<T> extends WJqCallbackComponent {
	public static final String URL_PARAM_PAGE_NO = "page";
	public static final String URL_PARAM_PAGE_SIZE = "rows";

	public static final String URL_PARAM_ID = "$id";
	public static final String URL_PARAM_CLICK_TYPE = "$tp";
	public static final String URL_PARAM_COLUMN_NUMBER = "$cn";
	public static final String URL_PARAM_COLUMN_REORDER = "$cr";

	public static final String CLICK_FROM_CELL = "cl";
	public static final String CLICK_FROM_BUTTON = "bt";

	// ------------------------------

	private static final long serialVersionUID = -2882882330275047801L;
	private static final String TOOLBAR_HTML_CLASS = "w-grid-tbar";

	protected static final Logger logger = LoggerFactory.getLogger(WBaseGrid.class);

	// ------------------------------

	private final OBaseGrid<T> options;
	private IExceptionToMessageHandler exceptionMessageHandler = WDefaults.getExceptionToMessageHandler();

	private IDataSource<T> dataSource;
	private IGridDataSource<T> gridDataSource;
	private IGridAsyncDataSource<T> gridAsyncDataSource;
	private IGridFooterDataSource<T> footerDataSource;

	private boolean hideToolbarFirstTime = true;
	private boolean automaticColumns = false;
	private boolean ignoreDataSourceCount = false;
	private boolean assertDuplicateKey = true;

	protected Integer pageSize, pageNum;
	protected final List<WSortField> sortFieldList = new ArrayList<>();
	protected final Map<String, IModel<T>> pageData = new HashMap<>();

	// ------------------------------ CONSTRUCTORS

	protected WBaseGrid(String id, OBaseGrid<T> options, IGridDataSource<T> gridDataSource) {
		super(id, options);

		this.dataSource = gridDataSource;
		this.gridDataSource = gridDataSource;
		this.options = options;
	}

	protected WBaseGrid(String id, OBaseGrid<T> options, IGridAsyncDataSource<T> gridAsyncDataSource) {
		super(id, options);

		this.dataSource = gridAsyncDataSource;
		this.gridAsyncDataSource = gridAsyncDataSource;
		this.options = options;
	}

	// ------------------------------ ACCESSORS

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

	public WBaseGrid<T> setHideToolbarFirstTime(boolean hideToolbarFirstTime) {
		this.hideToolbarFirstTime = hideToolbarFirstTime;
		return this;
	}

	public WBaseGrid<T> setAutomaticColumns(boolean automaticColumns) {
		this.automaticColumns = automaticColumns;
		return this;
	}

	public WBaseGrid<T> setIgnoreDataSourceCount(boolean ignoreDataSourceCount) {
		this.ignoreDataSourceCount = ignoreDataSourceCount;
		return this;
	}

	public WBaseGrid<T> setAssertDuplicateKey(boolean assertDuplicateKey) {
		this.assertDuplicateKey = assertDuplicateKey;
		return this;
	}

	// ------------------------------ METHODS

	public WBaseGrid<T> loadData(AjaxRequestTarget target) {
		if (isEnabledInHierarchy()) {
			if (options.getUrl() == null) {
				options.setUrl(getCallbackURL());
				target.appendJavaScript(String.format("$('#%s').%s('updateUrl',\"%s\");",
					getMarkupId(), getJQueryFunction(), getCallbackURL()));
			}

			target.appendJavaScript(String.format("$('#%s').%s('resetData');",
				getMarkupId(), getJQueryFunction()));
		} else {
			throw new WicketRuntimeException("WBaseGrid is disabled: " + getId());
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

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list) {
		return pushData(handler, list, -1, null);
	}

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list, long count) {
		return pushData(handler, list, count, null);
	}

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list, long count, List footer) {
		if (isEnabledInHierarchy()) {

			if (ignoreDataSourceCount) {
				/*
				 * 'ignoreDataSourceCount' of 'pushData' is different from getGridPage():
				 * 	1. no need to count++
				 * 	2. list size must be validated against 'pageSize' and extra records must be omitted
				 */
				count = (pageNum - 1) * pageSize + list.size();

				if (list.size() > pageSize) {
					list = list.subList(0, pageSize);
				}
			}

			RGridPage gridPage = createRGridPage(list, count);

			if (options.hasFooter()) {
				if (footer != null) {
					gridPage.setFooter(getGridFooter(footer));
				} else {
					gridPage.setFooter(new ArrayList<>());
				}
			}

			String script = createClientScript(gridPage);

			logger.debug("WBaseGrid.pushData(): {}", script);

			handler.appendJavaScript(script);
		} else {
			throw new WicketRuntimeException("WBaseGrid is disabled: " + getId());
		}

		return this;
	}

	public WBaseGrid<T> pushError(IPartialPageRequestHandler handler, Exception e) {
		if (isEnabledInHierarchy()) {
			RGridPage gridPage = new RGridPage();
			gridPage.setTotal((long) pageNum * pageSize);
			gridPage.setRows(new RObjectList());
			gridPage.setError(exceptionMessageHandler.handleMessage(this, e));

			String script = createClientScript(gridPage);

			logger.debug("WBaseGrid.pushError(): {}", script);

			handler.appendJavaScript(script);
		} else {
			throw new WicketRuntimeException("WBaseGrid is disabled: " + getId());
		}

		return this;
	}

	// ------------------------------ INTERNAL METHODS

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (options.getColumns() == null) {
			options.setColumns(new OColumnList<>());
		}

		int i = 0;
		for (OColumn<T> column : options.getColumns().getAllColumns()) {
			if (column.getField() == null) {
				column
					.setField("f" + (i++))
					.setDummyField(true);
			}
		}

		options.getColumns().validate();
		pageNum = 1;
		pageSize = options.getPageSize();

		add(new FontAwesomeBehavior());
		add(new HeaderBehavior("main/wGrid.js").setNeedEasyUI(true));

		if (gridDataSource == null && gridAsyncDataSource == null) {
			throw new WicketRuntimeException("WBaseGrid without datasource: " + getId());
		}

		if (gridDataSource != null && gridAsyncDataSource != null) {
			throw new WicketRuntimeException("WBaseGrid has both dataSource & asyncDataSource: " + getId());
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// It should be called in onBeforeRender, not worked in onInitialize, causing StalePageException
		if (!isEnabledInHierarchy()) {
			options.setUrl(null);
		}
		/*
		The following is set by IHtmlId & ICallbackUrl

		else {
			options.setUrl(getCallbackURL());
		}
		options.setHtmlId(getMarkupId());
		*/

		for (int i = 0; i < options.getToolbarButtons().size(); i++) {
			OButton<T> button = options.getToolbarButtons().get(i);
			button.init(getCallbackURL(), i, options.getHtmlId());
		}
	}

	@Override
	protected void onRequest() {
		try {
			processRequest();
		} catch (Exception e) {
			logger.warn("Grid.onRequest: id={}", getId(), e);

			RGridPage result = new RGridPage();
			result.setError(exceptionMessageHandler.handleMessage(this, e));

			sendJSONResponse(WebUtil.toJson(result));
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			createToolbar();
		}
	}

	// ---------------

	protected final RGridPage getGridPage() {
		RGridPage result = null;
		if (gridDataSource != null) {
			List<T> data = gridDataSource.list(pageNum, pageSize, sortFieldList);

			long count;
			if (data.size() < pageSize || ignoreDataSourceCount) {
				count = (pageNum - 1) * pageSize + data.size();
				//TODO the plus-one must be detected by requesting one more item from data source!
				if (ignoreDataSourceCount && data.size() == pageSize) {
					count++;
				}
			} else {
				count = gridDataSource.count();
			}

			result = createRGridPage(data, count);

			if (options.hasFooter() && footerDataSource != null) {
				result.setFooter(getGridFooter(footerDataSource.footer(data)));
			}
		} else {
			gridAsyncDataSource.asyncList(pageNum, pageSize, sortFieldList);
		}
		return result;
	}

	protected final RGridPage createRGridPage(List<T> data, long count) {
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
			List<OColumn<T>> columns = options.getColumns().getAllColumns();
			for (Object bean : footerData) {
				RObject rObject = new RObject();
				for (int colNo = 0; colNo < columns.size(); colNo++) {
					OColumn<T> column = columns.get(colNo);
					if (column.isHasFooter() && column.isVisible()) {
						String url = String.format("%s&%s=%s&%s=%s", getCallbackURL(), URL_PARAM_COLUMN_NUMBER, colNo,
							URL_PARAM_CLICK_TYPE, CLICK_FROM_CELL);
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

	protected void onColumnReorder(List<String> columns) {
	}

	protected final void convertBeansToRObjects(List<T> list, RObjectList page) {
		if (automaticColumns && !list.isEmpty()) {
			publishDynamicColumns(list.get(0));
		}

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

			if (options.getRowStyler() != null) {
				OStyle rowStyle = options.getRowStyler().doStyle(bean, id);
				rObject.addProperty("$style", rowStyle);
			}

			for (int colNo = 0; colNo < columns.size(); colNo++) {
				OColumn<T> column = columns.get(colNo);
				if (column.onCellRender(bean, id)) {
					String url = String.format("%s&%s=%s&%s=%s&%s=%s", getCallbackURL(), URL_PARAM_ID, id,
						URL_PARAM_COLUMN_NUMBER, colNo, URL_PARAM_CLICK_TYPE, CLICK_FROM_CELL);
					rObject.addProperty(column.getField(), column.cellValue(bean, id, colNo, url));

					if (column.getCellStyler() != null) {
						OStyle cellStyle = column.getCellStyler().doStyle(bean, id);
						rObject.addProperty(column.getField() + "$style", cellStyle);
					}
				}
			}
			onAfterBeanToRObject(bean, rObject);

			try {
				page.addRObject(id, rObject);
			} catch (WDuplicateKeyException e) {
				logger.error("WGrid (id={}) duplicate key, msg={}", getId(), e.getMessage());

				if (assertDuplicateKey) {
					throw e;
				}
			}
		}
	}

	// ---------------

	private void processRequest() {
		if (!isEnabledInHierarchy()) {
			return;
		}

		IRequestParameters getParams = getRequest().getQueryParameters();
		IRequestParameters postParams = getRequest().getPostParameters();

		/*
		NOTE: EasyUI POST params = rows, page, id, sort, order
		 */

		final int pageSize = postParams.getParameterValue(URL_PARAM_PAGE_SIZE).toInt(options.getPageSize());
		final int pageNum = postParams.getParameterValue(URL_PARAM_PAGE_NO).toInt(1);

		/*
		NOTE: idByPost vs idByGet
		idByPost: sent by EasyUI TreeGrid for expanded node
		idByGet:  sent by a custom link in the cell
		 */
		final String idByGet = getParams.getParameterValue(URL_PARAM_ID).toOptionalString();
		final String idByPost = postParams.getParameterValue("id").toOptionalString();

		final String sortList = postParams.getParameterValue("sort").toOptionalString();
		final String orderList = postParams.getParameterValue("order").toOptionalString();

		final String clickType = getParams.getParameterValue(URL_PARAM_CLICK_TYPE).toString();
		final Integer colNo = getParams.getParameterValue(URL_PARAM_COLUMN_NUMBER).toOptionalInteger();
		final String columnReorder = getParams.getParameterValue(URL_PARAM_COLUMN_REORDER).toOptionalString();

		logger.debug("WBaseGrid.onRequest:\n\tpageSize=[{}], pageNum=[{}], sort=[{}], order=[{}], idByPost=[{}]\n\tclickType=[{}] idByGet=[{}] colNo=[{}]",
			pageSize, pageNum, sortList, orderList, idByPost, clickType, idByGet, colNo);

		if (CLICK_FROM_CELL.equals(clickType)) {// click from cell (per row)
			if (idByGet == null) {
				throw new RuntimeException("Null id parameter!");
			}
			if (colNo == null) {
				throw new RuntimeException("Null colNo parameter!");
			}
			handleCellLinkClick(idByGet, colNo);
		} else if (CLICK_FROM_BUTTON.equals(clickType)) {// click from button in toolbar
			if (colNo == null) {
				throw new RuntimeException("Null button index parameter!");
			}
			handleToolbarButtonClick(colNo);
		} else if (columnReorder != null) {
			logger.debug("Column Reorder: {}", columnReorder);
			String[] columns = columnReorder.split("[,]");
			onColumnReorder(Arrays.asList(columns));
			sendEmptyResponse();
		} else if (idByPost != null && idByPost.length() > 0) {
			handleRowsById(idByPost);
		} else {
			this.pageSize = pageSize;
			this.pageNum = pageNum;

			if (sortList != null && orderList != null) {
				updateSortFieldList(sortList.split(","), orderList.split(","));
			} else {
				sortFieldList.clear();
			}

			logger.debug("WBaseGrid: SortFields = {}", sortFieldList);

			RGridPage result = getGridPage();
			if (gridDataSource != null) {
				sendJSONResponse(WebUtil.toJson(result));
			} else {
				sendJSONResponse("");
			}
		}
	}

	private void createToolbar() {
		List<OButton<T>> toolbarButtons = options.getToolbarButtons();
		if (!toolbarButtons.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			builder
				.append(String.format("<div id=\"%s-tb\" class=\"%s\"", getMarkupId(), TOOLBAR_HTML_CLASS));
			if (hideToolbarFirstTime) {
				builder
					.append(" style=\"visibility:hidden\"");
			}
			builder.append("><table><tr>");
			for (OButton<T> button : toolbarButtons) {
				button.setGrid(this);
				builder.append("<td>").append(button.getHTMLContent()).append("</td>");
				//button.setGrid(null);
			}
			builder
				.append("</tr></table></div>");

			getResponse().write(builder.toString());
		}
	}

	private void handleToolbarButtonClick(Integer colNo) {
		OButton<T> button = options.getToolbarButtons().get(colNo);
		button.setGrid(this);

		//try {
		if (button instanceof OLinkButton) {
			OLinkButton<T> linkButton = (OLinkButton<T>) button;
			linkButton.onClick();
		} else if (button instanceof OAjaxLinkButton) {
			AjaxRequestTarget target = createAjaxResponse();

			OAjaxLinkButton<T> ajaxLinkButton = (OAjaxLinkButton<T>) button;
			ajaxLinkButton.onClick(target);
		} else {
			throw new RuntimeException("Invalid request from button click");
		}
		/*} finally {
			button.setGrid(null);
		}*/
	}

	private void handleCellLinkClick(String id, Integer colNo) {
		IModel<T> rowModel = pageData.get(id);
		OColumn<T> column = options.getColumns().getAllColumns().get(colNo);
		if (column instanceof OLinkColumn) {
			OLinkColumn<T> linkColumn = (OLinkColumn<T>) column;
			linkColumn.onClick(rowModel);
		} else if (column instanceof OAjaxLinkColumn) {
			AjaxRequestTarget target = createAjaxResponse();

			OAjaxLinkColumn<T> ajaxLinkColumn = (OAjaxLinkColumn<T>) column;
			try {
				ajaxLinkColumn.onClick(target, rowModel);
			} catch (Exception e) {
				logger.error("handleCellLinkClick", e);
				ajaxLinkColumn.onException(target, e, rowModel);
			}

		} else {
			throw new RuntimeException("Invalid request from cell click: " + column.getField());
		}
	}

	private void updateSortFieldList(String[] sortList, String[] orderList) {
		sortFieldList.clear();

		final OColumnList<T> columns = options.getColumns();

		for (int i = 0; i < sortList.length; i++) {
			final String field = sortList[i];
			final String sortField = columns.findByField(field).getSortField();
			sortFieldList.add(new WSortField(sortField, orderList[i]));
		}
	}

	private String createClientScript(RGridPage gridPage) {
		StringBuilder result = new StringBuilder();

		if (automaticColumns) {
			result.append(String.format("$('#%s').%s('updateColumns', {columns:%s});",
				getMarkupId(), getJQueryFunction(), WebUtil.toJson(options.getColumns())));
		}

		result.append(String.format("$('#%s').%s('loadData', %s);",
			getMarkupId(), getJQueryFunction(), WebUtil.toJson(gridPage)));

		// NOTE: setting URL must be set after loadData, otherwise it sends a request by loadData and fetches data twice
		if (options.getUrl() == null) {
			options.setUrl(getCallbackURL());
			result.append(String.format("$('#%s').%s('updateUrl', \"%s\");",
				getMarkupId(), getJQueryFunction(), getCallbackURL()));
		}

		return result.toString();
	}

	private void publishDynamicColumns(T sample) {
		OColumnList<T> columns = options.getColumns();
		columns.clear();

		if (sample instanceof Map) {
			Map map = (Map) sample;
			for (Object key : map.keySet()) {
				String prop = key.toString();
				columns.add(new OPropertyColumn<>(new Model<>(prop), prop));
			}
		} else {
			PropertyDescriptor[] descriptors = ObjectUtil.getPropertyDescriptors(sample, false);
			for (PropertyDescriptor pd : descriptors) {
				String prop = pd.getName();

				if ("class".equals(prop)) {
					continue;
				}

				columns.add(new OPropertyColumn<>(new Model<>(prop), prop));
			}
		}
	}

}

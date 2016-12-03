package org.devocative.wickomp.grid;

import org.apache.wicket.WicketRuntimeException;
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
import org.devocative.wickomp.grid.toolbar.OAjaxLinkButton;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.grid.toolbar.OLinkButton;
import org.devocative.wickomp.wrcs.EasyUIBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WBaseGrid<T> extends WJqCallbackComponent {
	/*
	NOTE: this field can't be changed!!!
	the easyui grid sends this "id" as parameter,
	and especially in the treegrid it is used for requesting children nodes of a clicked node!
	 */
	public static final String URL_PARAM_ID = "id";
	public static final String URL_PARAM_CLICK_TYPE = "$tp";
	public static final String URL_PARAM_COLUMN_NUMBER = "$cn";

	public static final String CLICK_FROM_CELL = "cl";
	public static final String CLICK_FROM_BUTTON = "bt";

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

	// ------------------------------ CONSTRUCTORS

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

	// ------------------------------ METHODS

	public WBaseGrid<T> loadData(AjaxRequestTarget target) {
		if (isEnabledInHierarchy()) {
			RGridPage gridPage = getGridPage();

			if (gridDataSource != null) {
				String script = createClientScript(gridPage);

				logger.debug("WBaseGrid.loadData(): {}", script);

				target.appendJavaScript(script);
			}
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

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list, long count) {
		return pushData(handler, list, count, null);
	}

	public WBaseGrid<T> pushData(IPartialPageRequestHandler handler, List<T> list, long count, List footer) {
		if (isEnabledInHierarchy()) {
			RGridPage gridPage = getGridPage(list, count);

			if (options.hasFooter()) {
				if (footer != null) {
					gridPage.setFooter(getGridFooter(footer));
				} else {
					gridPage.setFooter(new ArrayList<RObject>());
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
		add(new EasyUIBehavior());

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
			button.init(getCallbackURL(), i, options.getHtmlId(), options.getColumns());
		}
	}

	@Override
	protected void onRequest(IRequestParameters parameters) {

		pageSize = parameters.getParameterValue("rows").toInt(options.getPageSize());
		pageNum = parameters.getParameterValue("page").toInt(1);

		if (!isEnabledInHierarchy()) {
			return;
		}

		String id = parameters.getParameterValue(URL_PARAM_ID).toOptionalString();
		String sortList = parameters.getParameterValue("sort").toOptionalString(); //TODO $
		String orderList = parameters.getParameterValue("order").toOptionalString(); // TODO $

		String clickType = parameters.getParameterValue(URL_PARAM_CLICK_TYPE).toString();
		Integer colNo = parameters.getParameterValue(URL_PARAM_COLUMN_NUMBER).toOptionalInteger();

		logger.debug("WBaseGrid: onRequest.parameters: clickType={}, pageSize={}, pageNum={}, sort={}, order={}, id={}, colNo={}",
			clickType, pageSize, pageNum, sortList, orderList, id, colNo);

		if (CLICK_FROM_CELL.equals(clickType)) {// click from cell (per row)
			if (id == null) {
				throw new RuntimeException("Null id parameter!");
			}
			if (colNo == null) {
				throw new RuntimeException("Null colNo parameter!");
			}
			handleCellLinkClick(id, colNo);
		} else if (CLICK_FROM_BUTTON.equals(clickType)) {// click from button in toolbar
			if (colNo == null) {
				throw new RuntimeException("Null button index parameter!");
			}
			handleToolbarButtonClick(colNo);
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
			if (gridDataSource != null) {
				sendJSONResponse(WebUtil.toJson(result));
			} else {
				sendJSONResponse("");
			}
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			List<OButton<T>> toolbarButtons = options.getToolbarButtons();
			if (!toolbarButtons.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				builder
					.append(String.format("<div id=\"%s-tb\">", getMarkupId()))
					.append("<table><tr>");
				for (OButton<T> button : toolbarButtons) {
					builder.append("<td>").append(button.getHTMLContent()).append("</td>");
				}
				builder.append("</tr></table></div>");

				getResponse().write(builder.toString());
			}
		}
	}

	// ---------------

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
			} else {
				gridAsyncDataSource.list(pageNum, pageSize, sortFieldList);

				result = getGridPage(null, pageNum * pageSize);
				if (options.hasFooter()) {
					result.setFooter(new ArrayList<RObject>());
				}
				result.setAsync(true);
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
					String url = String.format("%s&%s=%s&%s=%s&%s=%s", getCallbackURL(), URL_PARAM_ID, id,
						URL_PARAM_COLUMN_NUMBER, colNo, URL_PARAM_CLICK_TYPE, CLICK_FROM_CELL);
					rObject.addProperty(column.getField(), column.cellValue(bean, id, colNo, url));
				}
			}
			onAfterBeanToRObject(bean, rObject);
			page.addRObject(id, rObject);
		}
	}

	// ---------------

	private void handleToolbarButtonClick(Integer colNo) {
		OButton<T> button = options.getToolbarButtons().get(colNo);
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
				ajaxLinkColumn.onException(target, e, rowModel);
			}

		} else {
			throw new RuntimeException("Invalid request from cell click: " + column.getField());
		}
	}

	private void updateSortFieldList(String[] sortList, String[] orderList) {
		sortFieldList.clear();

		for (int i = 0; i < sortList.length; i++) {
			sortFieldList.add(new WSortField(sortList[i], orderList[i]));
		}
	}

	private String createClientScript(RGridPage gridPage) {
		StringBuilder result = new StringBuilder();

		if (options.getUrl() == null) {
			options.setUrl(getCallbackURL());
			result.append(String.format("$('#%s').%s('options')['url']=\"%s\";",
				getMarkupId(), getJQueryFunction(), getCallbackURL()));
		}

		result.append(String.format("$('#%s').%s('loadData', %s);",
			getMarkupId(), getJQueryFunction(), WebUtil.toJson(gridPage)));

		return result.toString();
	}
}

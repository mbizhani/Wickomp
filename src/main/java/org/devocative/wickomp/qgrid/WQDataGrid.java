package org.devocative.wickomp.qgrid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.util.string.Strings;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.WCallbackComponent;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.WDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.grid.RGridPage;
import org.devocative.wickomp.opt.OHorizontalAlign;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.qgrid.column.OQColumn;
import org.devocative.wickomp.qgrid.toolbar.OQButton;
import org.devocative.wickomp.qgrid.toolbar.WQGridInfo;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.JqWidgetsBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WQDataGrid<T> extends WCallbackComponent {
	private static final Logger logger = LoggerFactory.getLogger(WQDataGrid.class);

	private OQGrid<T> options;
	private WDataSource<T> dataSource;
	private List<IModel<T>> pageData = new ArrayList<>();

	public WQDataGrid(String id, OQGrid<T> options, WDataSource<T> dataSource) {
		super(id, options);

		this.options = options;
		this.dataSource = dataSource;

		add(new JqWidgetsBehavior());
		add(new FontAwesomeBehavior());
	}

	// ------------------------- ACCESSORS

	public OQGrid<T> getOptions() {
		return options;
	}

	public WDataSource<T> getDataSource() {
		return dataSource;
	}

	// ------------------------- METHODS

	public void loadData(AjaxRequestTarget target) {
		if (dataSource.isEnabled()) {
			target.appendJavaScript(String.format("$(\"#%1$s\").%2$s('updatebounddata');",
				getMarkupId(), getJQueryFunction()));
		}
	}

	// ------------------------- INTERNAL METHODS
	@Override
	protected String getJQueryFunction() {
		return "jqxGrid";
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		int i = 0;
		for (OQColumn<T> column : options.getColumns()) {
			if (column.getDataField() == null) {
				column
					.setDataField("f" + (i++))
					.setDummyField(true);
			}
		}
		options.updateDataSourceFields();

		if (OLayoutDirection.RTL.equals(getUserPreference().getLayoutDirection())) {
			options.setRtl(true);
			for (OQColumn<T> column : options.getColumns()) {
				column.setCellClassName("mb-rtl-cell");
				if (column.getCellsAlign() == null) {
					column.setCellsAlign(OHorizontalAlign.Right);
				}
				if (column.getAlign() == null) {
					column.setAlign(OHorizontalAlign.Right);
				}
			}
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		// It should be called in onBeforeRender, not worked in onInitialize, causing StalePageException
		options.setUrl(getCallbackURL());
		options.setGridHTMLId(getMarkupId());
		options.setLanguage(getLocale().getLanguage());

		if (options.getToolbarButtons() != null) {
			for (OQButton button : options.getToolbarButtons()) {
				button.setUrl(getCallbackURL());
			}
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			List<OQButton<T>> toolbarButtons = options.getToolbarButtons();
			if (toolbarButtons != null) {
				WQGridInfo<T> info = new WQGridInfo<>(options, dataSource, null); //TODO
				String direction = getUserPreference().getLayoutDirection().toString();
				StringBuilder builder = new StringBuilder();
				builder
					.append(String.format("<div style=\"direction:%s;display:none;\" id=\"%s-tb\">", direction, getMarkupId()))
					.append("<table><tr>");
				for (OQButton<T> button : toolbarButtons) {
					builder.append("<td>").append(button.getHTMLContent(info)).append("</td>");
				}
				builder.append("</tr></table></div>");

				getResponse().write(builder.toString());
			}
		}
	}


	@Override
	protected void onRequest(IRequestParameters parameters) {
		int pageSizeParam = parameters.getParameterValue("pagesize").toInt(10);
		int pageNumParam = parameters.getParameterValue("pagenum").toInt(0);

		String sortFieldParam = parameters.getParameterValue("sortdatafield").toOptionalString();
		String sortOrderParam = parameters.getParameterValue("sortorder").toOptionalString();

		String clickType = parameters.getParameterValue("tp").toString();
		Integer rowNo = parameters.getParameterValue("rn").toOptionalInteger();
		Integer colNo = parameters.getParameterValue("cn").toOptionalInteger();


		logger.debug("WQDataGrid: onRequest.parameters: type={}, pageSize={}, pageNum={}, sort={}, order={}, rowNo={}, colNo={}",
			clickType, pageSizeParam, pageNumParam, sortFieldParam, sortOrderParam, rowNo, colNo);

		if ("cl".equals(clickType)) {// click from cell (per row)
			if (rowNo == null) {
				throw new RuntimeException("Null rowNo parameter!");
			}
			if (colNo == null) {
				throw new RuntimeException("Null colNo parameter!");
			}
		} else if ("bt".equals(clickType)) {// click from button in toolbar
			if (colNo == null) {
				throw new RuntimeException("Null button index parameter!");
			}
			handleToolbarButtonClick(colNo, parameters);
		} else {

			RGridPage result = new RGridPage();

			if (dataSource.isEnabled()) {
				List<WSortField> sortFieldList = new ArrayList<>();
				if (!Strings.isEmpty(sortFieldParam) && !Strings.isEmpty(sortOrderParam)) {
					sortFieldList.add(new WSortField(sortFieldParam, sortOrderParam));
				}
				List<T> data = dataSource.list(pageNumParam + 1, pageSizeParam, sortFieldList);
				long count = dataSource.count();
				result.setRows(getPageData(data));
				result.setTotal(count);
			}

			sendJSONResponse(JsonUtil.toJson(result));
		}
	}

	private List<RObject> getPageData(List<T> list) {
		pageData.clear();
		List<RObject> page = new ArrayList<>();
		for (int rowNo = 0; rowNo < list.size(); rowNo++) {
			T bean = list.get(rowNo);
			pageData.add(dataSource.model(bean));

			RObject map = new RObject();
			for (int colNo = 0; colNo < options.getColumns().size(); colNo++) {
				OQColumn<T> column = options.getColumns().get(colNo);
				if (column.onCellRender(bean, rowNo)) {
					String url = String.format("%s&rn=%s&cn=%s&tp=cl", getCallbackURL(), rowNo, colNo);
					map.addProperty(column.getDataField(), column.cellValue(bean, rowNo, colNo, url));
				}
			}
			page.add(map);
		}
		return page;
	}

	private void handleToolbarButtonClick(Integer colNo, IRequestParameters parameters) {
		OQButton<T> button = options.getToolbarButtons().get(colNo);
		button.onClick(new WQGridInfo<>(options, dataSource, null), parameters); //TODO
	}
}

package org.devocative.wickomp.qgrid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.opt.OComponent;
import org.devocative.wickomp.qgrid.column.OQColumn;
import org.devocative.wickomp.qgrid.toolbar.OQButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OQGrid<T> extends OComponent {
	private Boolean altRows = true;
	private Boolean autoHeight; // default = false
	private List<OQColumn<T>> columns;
	private Boolean columnsReorder = true;
	private Boolean columnsResize = true;
	private Boolean enableTooltips = true;
	private Boolean groupable = true;
	private Boolean pageable = true;
	private OQPagerMode pagerMode = OQPagerMode.Default;
	private Integer pageSize;
	private List<Integer> pageSizeOptions;
	private OQSelectionMode selectionMode = OQSelectionMode.SingleRow;
	private Boolean sortable = true;
	private Boolean rtl;
	private Boolean virtualMode = true;

	//----------
	private String gridHTMLId;
	private String language = "en";
	private OQDataAdapter<T> source = new OQDataAdapter<>();
	private List<OQButton<T>> toolbarButtons;

	public OQGrid() {
		pageSizeOptions = Arrays.asList(10, 20, 30, 40, 50);
		pageSize = pageSizeOptions.get(0);
	}

	// ----------------------- ACCESSORS

	public Boolean getAltRows() {
		return altRows;
	}

	public OQGrid<T> setAltRows(Boolean altRows) {
		this.altRows = altRows;
		return this;
	}

	public Boolean getAutoHeight() {
		return autoHeight;
	}

	public OQGrid<T> setAutoHeight(Boolean autoHeight) {
		this.autoHeight = autoHeight;
		return this;
	}

	public List<OQColumn<T>> getColumns() {
		return columns;
	}

	public OQGrid<T> setColumns(List<OQColumn<T>> columns) {
		this.columns = columns;
		updateDataSourceFields();
		return this;
	}

	public Boolean getColumnsReorder() {
		return columnsReorder;
	}

	public OQGrid<T> setColumnsReorder(Boolean columnsReorder) {
		this.columnsReorder = columnsReorder;
		return this;
	}

	public Boolean getColumnsResize() {
		return columnsResize;
	}

	public OQGrid<T> setColumnsResize(Boolean columnsResize) {
		this.columnsResize = columnsResize;
		return this;
	}

	public Boolean getEnableTooltips() {
		return enableTooltips;
	}

	public OQGrid<T> setEnableTooltips(Boolean enableTooltips) {
		this.enableTooltips = enableTooltips;
		return this;
	}

	public Boolean getGroupable() {
		return groupable;
	}

	public OQGrid<T> setGroupable(Boolean groupable) {
		this.groupable = groupable;
		return this;
	}

	public Boolean getPageable() {
		return pageable;
	}

	public OQGrid<T> setPageable(Boolean pageable) {
		this.pageable = pageable;
		return this;
	}

	public OQPagerMode getPagerMode() {
		return pagerMode;
	}

	public OQGrid<T> setPagerMode(OQPagerMode pagerMode) {
		this.pagerMode = pagerMode;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public OQGrid<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public List<Integer> getPageSizeOptions() {
		return pageSizeOptions;
	}

	public OQGrid<T> setPageSizeOptions(List<Integer> pageSizeOptions) {
		this.pageSizeOptions = pageSizeOptions;
		return this;
	}

	public OQSelectionMode getSelectionMode() {
		return selectionMode;
	}

	public OQGrid<T> setSelectionMode(OQSelectionMode selectionMode) {
		this.selectionMode = selectionMode;
		return this;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public OQGrid<T> setSortable(Boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	public Boolean getRtl() {
		return rtl;
	}

	public OQGrid<T> setRtl(Boolean rtl) {
		this.rtl = rtl;
		return this;
	}

	public Boolean getVirtualMode() {
		return virtualMode;
	}

	public OQGrid<T> setVirtualMode(Boolean virtualMode) {
		this.virtualMode = virtualMode;
		return this;
	}

	// ----------------------- METHODS

	@JsonRawValue
	public String getLocalization() {
		return String.format("getLocalization('%s')", language);
	}

	@JsonRawValue
	public String getSource() {
		return String.format("new $.jqx.dataAdapter(%s)", JsonUtil.toJson(source));
	}

	@JsonRawValue
	public String getRenderGridRows() {
		return "function (obj) {return obj.data;}";
	}

	public Boolean getShowToolbar() {
		return toolbarButtons != null;
	}

	@JsonRawValue
	public String getRenderToolbar() {
		return String.format("function(toolbar){toolbar.append($('#%s-tb').css('display', ''));}", gridHTMLId);
	}

	public OQGrid<T> setGridHTMLId(String gridHTMLId) {
		this.gridHTMLId = gridHTMLId;
		source.setGridHTMLId(gridHTMLId);
		return this;
	}

	public OQGrid<T> setLanguage(String language) {
		this.language = language;
		return this;
	}

	public OQGrid<T> setUrl(String url) {
		//this.url = url;
		source.setUrl(url);
		return this;
	}

	public void updateDataSourceFields() {
		source.createDataFields(columns);
	}

	@JsonIgnore
	public List<OQButton<T>> getToolbarButtons() {
		return toolbarButtons;
	}

	public OQGrid<T> addToolbarButton(OQButton<T> button) {
		if (toolbarButtons == null) {
			toolbarButtons = new ArrayList<>();
		}

		button.setIndex(toolbarButtons.size());
		toolbarButtons.add(button);
		return this;
	}

}

package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.IHtmlId;
import org.devocative.wickomp.opt.IStyler;
import org.devocative.wickomp.opt.OComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OBaseGrid<T> extends OComponent implements IHtmlId, ICallbackUrl {
	private static final long serialVersionUID = 1095555452051726851L;

	// ------------------------------ JSON FIELDS

	private Boolean autoRowHeight;
	private Boolean checkOnSelect;
	private OColumnList<T> columns;
	private List<T> data;
	private String idField;
	private Boolean multiSort;
	private Boolean pagination;
	private List<Integer> pageList;
	private Integer pageSize;
	private String returnField;
	private Boolean rowNumbers;
	private Boolean selectOnCheck;
	private Boolean showFooter;
	private Boolean singleSelect;
	private Boolean striped;
	private String titleField;
	private String url;

	// ------------------------------ EXTRA FIELDS

	protected Boolean asyncLoadingEnabled;
	protected Boolean autoTooltip;
	protected Boolean callbackOnColumnReorder;
	protected Boolean columnReorder;
	protected String noResultMessage;
	protected List<OPagingButtons> pagingBarLayout;
	protected List<String> reorderColumns;
	protected Boolean selectionIndicator;
	protected String selectionJSHandler;
	protected Boolean selectionDblClick;
	protected String gridId;

	protected IStyler<T> rowStyler; //no-json
	private List<OButton<T>> toolbar = new ArrayList<>(); // no-json
	protected String htmlId; //no-json

	// ------------------------------

	public OBaseGrid() {
		pageList = Arrays.asList(10, 20, 30, 40, 50);
		pageSize = pageList.get(0);
	}

	// ------------------------------ ACCESSORS for JSON

	public Boolean getAutoRowHeight() {
		return autoRowHeight;
	}

	public OBaseGrid<T> setAutoRowHeight(Boolean autoRowHeight) {
		this.autoRowHeight = autoRowHeight;
		return this;
	}

	public Boolean getCheckOnSelect() {
		return checkOnSelect;
	}

	public OBaseGrid<T> setCheckOnSelect(Boolean checkOnSelect) {
		this.checkOnSelect = checkOnSelect;
		return this;
	}

	public OColumnList<T> getColumns() {
		return columns;
	}

	public OBaseGrid<T> setColumns(OColumnList<T> columns) {
		this.columns = columns;
		return this;
	}

	public List<T> getData() {
		return data;
	}

	public OBaseGrid<T> setData(List<T> data) {
		this.data = data;
		return this;
	}

	public String getIdField() {
		return idField;
	}

	public OBaseGrid<T> setIdField(String idField) {
		this.idField = idField;
		return this;
	}

	public Boolean getMultiSort() {
		return multiSort;
	}

	public OBaseGrid<T> setMultiSort(Boolean multiSort) {
		this.multiSort = multiSort;
		return this;
	}

	public Boolean getPagination() {
		return pagination;
	}

	public OBaseGrid<T> setPagination(Boolean pagination) {
		this.pagination = pagination;
		return this;
	}

	public List<Integer> getPageList() {
		return pageList;
	}

	public OBaseGrid<T> setPageList(List<Integer> pageList) {
		this.pageList = pageList;
		if (pageList != null && pageList.size() > 0) {
			this.pageSize = pageList.get(0);
		}
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public String getReturnField() {
		return returnField;
	}

	public OBaseGrid<T> setReturnField(String returnField) {
		this.returnField = returnField;
		return this;
	}

	@JsonProperty("rownumbers")
	public Boolean getRowNumbers() {
		return rowNumbers;
	}

	public OBaseGrid<T> setRowNumbers(Boolean rowNumbers) {
		this.rowNumbers = rowNumbers;
		return this;
	}

	public Boolean getSelectOnCheck() {
		return selectOnCheck;
	}

	public OBaseGrid<T> setSelectOnCheck(Boolean selectOnCheck) {
		this.selectOnCheck = selectOnCheck;
		return this;
	}

	public Boolean getShowFooter() {
		return showFooter;
	}

	public OBaseGrid<T> setShowFooter(Boolean showFooter) {
		this.showFooter = showFooter;
		return this;
	}

	public Boolean getSingleSelect() {
		return singleSelect;
	}

	public OBaseGrid<T> setSingleSelect(Boolean singleSelect) {
		this.singleSelect = singleSelect;
		return this;
	}

	public Boolean getStriped() {
		return striped;
	}

	public OBaseGrid<T> setStriped(Boolean striped) {
		this.striped = striped;
		return this;
	}

	public String getTitleField() {
		return titleField;
	}

	public OBaseGrid<T> setTitleField(String titleField) {
		this.titleField = titleField;
		return this;
	}

	public String getToolbar() {
		return !toolbar.isEmpty() ? String.format("#%s-tb", htmlId) : null;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	// ------------------------------ EXTRA FIELDS

	public Boolean getAsyncLoadingEnabled() {
		return asyncLoadingEnabled;
	}

	public OBaseGrid<T> setAsyncLoadingEnabled(Boolean asyncLoadingEnabled) {
		this.asyncLoadingEnabled = asyncLoadingEnabled;
		return this;
	}

	public Boolean getAutoTooltip() {
		return autoTooltip;
	}

	public OBaseGrid<T> setAutoTooltip(Boolean autoTooltip) {
		this.autoTooltip = autoTooltip;
		return this;
	}

	public Boolean getCallbackOnColumnReorder() {
		return callbackOnColumnReorder;
	}

	public OBaseGrid<T> setCallbackOnColumnReorder(Boolean callbackOnColumnReorder) {
		this.callbackOnColumnReorder = callbackOnColumnReorder;
		return this;
	}

	public Boolean getColumnReorder() {
		return columnReorder;
	}

	public OBaseGrid<T> setColumnReorder(Boolean columnReorder) {
		this.columnReorder = columnReorder;
		return this;
	}

	public String getNoResultMessage() {
		return noResultMessage;
	}

	public OBaseGrid<T> setNoResultMessage(String noResultMessage) {
		this.noResultMessage = noResultMessage;
		return this;
	}

	public List<OPagingButtons> getPagingBarLayout() {
		return pagingBarLayout;
	}

	public OBaseGrid<T> setPagingBarLayout(List<OPagingButtons> pagingBarLayout) {
		this.pagingBarLayout = pagingBarLayout;
		return this;
	}

	public List<String> getReorderColumns() {
		return reorderColumns;
	}

	public OBaseGrid<T> setReorderColumns(List<String> reorderColumns) {
		this.reorderColumns = reorderColumns;
		return this;
	}

	public Boolean getSelectionIndicator() {
		return selectionIndicator;
	}

	public OBaseGrid<T> setSelectionIndicator(boolean selectionIndicator) {
		this.selectionIndicator = selectionIndicator;
		return this;
	}

	@JsonRawValue
	public String getSelectionJSHandler() {
		return selectionJSHandler;
	}

	public OBaseGrid<T> setSelectionJSHandler(String selectionJSHandler) {
		this.selectionJSHandler = selectionJSHandler;
		return this;
	}

	public Boolean getSelectionDblClick() {
		return selectionDblClick;
	}

	public OBaseGrid<T> setSelectionDblClick(boolean selectionDblClick) {
		this.selectionDblClick = selectionDblClick;
		return this;
	}

	public String getGridId() {
		return gridId;
	}

	public OBaseGrid<T> setGridId(String gridId) {
		this.gridId = gridId;
		return this;
	}

	// --------------- PUBLIC METHODS

	@JsonIgnore
	public String getHtmlId() {
		return htmlId;
	}

	@Override
	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}

	@JsonIgnore
	public IStyler<T> getRowStyler() {
		return rowStyler;
	}

	public OBaseGrid<T> setRowStyler(IStyler<T> rowStyler) {
		this.rowStyler = rowStyler;
		return this;
	}

	@JsonIgnore
	public List<OButton<T>> getToolbarButtons() {
		return toolbar;
	}

	public OBaseGrid<T> addToolbarButton(OButton<T> button) {
		toolbar.add(button);
		return this;
	}

	public boolean hasFooter() {
		return getShowFooter() != null && getShowFooter();
	}
}

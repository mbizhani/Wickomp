package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.IHtmlId;
import org.devocative.wickomp.opt.OComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OBaseGrid<T> extends OComponent implements IHtmlId, ICallbackUrl {
	private static final long serialVersionUID = 1095555452051726851L;

	// ---------------------- JSON FIELDS
	private Boolean autoRowHeight = false;
	private Boolean checkOnSelect;
	private OColumnList<T> columns;
	private List<T> data;
	private String idField;
	private Boolean multiSort = false;
	private Boolean pagination = true;
	private List<Integer> pageList;
	private Integer pageSize;
	private String returnField;
	private Boolean rowNumbers = true;
	private Boolean selectOnCheck;
	private Boolean showFooter;
	private Boolean singleSelect = true;
	private Boolean striped;
	private String titleField;
	private List<OButton<T>> toolbar;
	private String url;

	// ---------------------- MISC FIELDS
	protected String htmlId;
	protected boolean selectionIndicator = false;
	protected String selectionJSHandler;
	protected boolean selectionDblClick = true;

	// ---------------------- CONSTRUCTORS

	public OBaseGrid() {
		pageList = Arrays.asList(10, 20, 30, 40, 50);
		pageSize = pageList.get(0);
	}

	// ---------------------- ACCESSORS for JSON

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

	@JsonRawValue
	public String getLoadFilter() {
		return String.format("function(data){return handleLoaded('%s', data);}", htmlId);
	}

	public String getLoadMsg() {
		return "...";
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

	public OBaseGrid<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
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
		return toolbar != null ? String.format("#%s-tb", htmlId) : null;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	// ---------------------- ACCESSORS FOR JS EVENTS

	@JsonRawValue
	public String getOnLoadSuccess() {
		return getSelectionJSFunc(null);
	}

	@JsonRawValue
	public String getOnSelect() {
		return getSelectionJSFunc(null);
	}

	@JsonRawValue
	public String getOnUnselect() {
		return getSelectionJSFunc(null);
	}

	// ---------------------- PUBLIC METHODS

	@JsonIgnore
	public List<OButton<T>> getToolbarButtons() {
		return toolbar;
	}

	public OBaseGrid<T> addToolbarButton(OButton<T> button) {
		if (toolbar == null) {
			toolbar = new ArrayList<>();
		}

		button.setIndex(toolbar.size());
		toolbar.add(button);
		return this;
	}

	@JsonIgnore
	public String getHtmlId() {
		return htmlId;
	}

	@Override
	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}

	@JsonIgnore
	public boolean isSelectionIndicator() {
		return selectionIndicator;
	}

	public OBaseGrid<T> setSelectionIndicator(boolean selectionIndicator) {
		this.selectionIndicator = selectionIndicator;
		return this;
	}

	public OBaseGrid<T> setSelectionJSHandler(String selectionJSHandler) {
		this.selectionJSHandler = selectionJSHandler;
		return this;
	}

	public OBaseGrid<T> setSelectionDblClick(boolean selectionDblClick) {
		this.selectionDblClick = selectionDblClick;
		return this;
	}

	public boolean hasFooter() {
		return getShowFooter() != null && getShowFooter();
	}

	protected String getSelectionJSFunc(String anotherFunction) {
		if (selectionIndicator || anotherFunction != null) {
			StringBuilder builder = new StringBuilder();
			builder.append("function(data){");
			builder.append(selectionJSHandler == null ?
					String.format("handleSelectionIndicator('%s');", htmlId) :
					String.format("handleSelectionIndicator('%s', %s, %s);",
						htmlId, selectionJSHandler, selectionDblClick)
			);
			if (anotherFunction != null) {
				builder.append(anotherFunction);
			}
			return builder.append("}").toString();
		}
		return null;
	}
}

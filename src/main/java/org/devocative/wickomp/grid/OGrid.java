package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.opt.OComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OGrid<T> extends OComponent {
	private Boolean autoRowHeight = false;
	private OColumnList<T> columns;
	private List<T> data;
	private String groupField;
	private Boolean multiSort = false;
	private Boolean pagination = true;
	private List<Integer> pageList;
	private Integer pageSize;
	private Boolean rowNumbers = true;
	private Boolean singleSelect = true;
	private Boolean striped = true;
	private List<OButton<T>> toolbar;
	private String url;
	private OGridViewType view;

	public OGrid() {
		pageList = Arrays.asList(10, 20, 30, 40, 50);
		pageSize = pageList.get(0);
	}

	public Boolean getAutoRowHeight() {
		return autoRowHeight;
	}

	public OGrid<T> setAutoRowHeight(Boolean autoRowHeight) {
		this.autoRowHeight = autoRowHeight;
		return this;
	}

	public OColumnList<T> getColumns() {
		return columns;
	}

	public OGrid<T> setColumns(OColumnList<T> columns) {
		this.columns = columns;
		return this;
	}

	public List<T> getData() {
		return data;
	}

	public OGrid<T> setData(List<T> data) {
		this.data = data;
		return this;
	}

	public String getGroupField() {
		return groupField;
	}

	public OGrid<T> setGroupField(String groupField) {
		this.groupField = groupField;
		this.view = OGridViewType.GroupView;
		return this;
	}

	@JsonRawValue
	public String getGroupFormatter() {
		return getGroupField() != null ? "function(value,rows){return value;}" : null;
	}

	public Boolean getMultiSort() {
		return multiSort;
	}

	public OGrid<T> setMultiSort(Boolean multiSort) {
		this.multiSort = multiSort;
		return this;
	}

	public Boolean getPagination() {
		return pagination;
	}

	public OGrid<T> setPagination(Boolean pagination) {
		this.pagination = pagination;
		return this;
	}

	public List<Integer> getPageList() {
		return pageList;
	}

	public OGrid<T> setPageList(List<Integer> pageList) {
		this.pageList = pageList;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public OGrid<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	@JsonProperty("rownumbers")
	public Boolean getRowNumbers() {
		return rowNumbers;
	}

	public OGrid<T> setRowNumbers(Boolean rowNumbers) {
		this.rowNumbers = rowNumbers;
		return this;
	}

	public Boolean getSingleSelect() {
		return singleSelect;
	}

	public OGrid<T> setSingleSelect(Boolean singleSelect) {
		this.singleSelect = singleSelect;
		return this;
	}

	public Boolean getStriped() {
		return striped;
	}

	public OGrid<T> setStriped(Boolean striped) {
		this.striped = striped;
		return this;
	}

	public List<OButton<T>> getToolbar() {
		return toolbar;
	}

	public OGrid<T> addToolbarButton(OButton<T> button) {
		if (toolbar == null) {
			toolbar = new ArrayList<>();
		}

		button.setIndex(toolbar.size());
		toolbar.add(button);
		return this;
	}

	public String getUrl() {
		return url;
	}

	public OGrid<T> setUrl(String url) {
		this.url = url;
		return this;
	}

	@JsonRawValue
	public OGridViewType getView() {
		return view;
	}

	/*public OGrid<T> setView(OGridViewType view) {
		this.view = view;
		return this;
	}*/
}

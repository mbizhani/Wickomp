package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.opt.OComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OGrid<T extends Serializable> extends OComponent {
	private Boolean autoRowHeight = false;
	private OColumnList<T> columns;
	private boolean multiSort = false;
	private Boolean pagination = true;
	private List<Integer> pageList;
	private Integer pageSize;
	private Boolean rowNumbers = true;
	private Boolean singleSelect = true;
	private List<OButton<T>> toolbar;
	private String url;

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

	public boolean getMultiSort() {
		return multiSort;
	}

	public OGrid<T> setMultiSort(boolean multiSort) {
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
}

package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.toolbar.OButton;
import org.devocative.wickomp.opt.OComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OBaseGrid<T> extends OComponent {
	private Boolean autoRowHeight = false;
	private OColumnList<T> columns;
	private List<T> data;
	private String idField;
	private Boolean multiSort = false;
	private Boolean pagination = true;
	private List<Integer> pageList;
	private Integer pageSize;
	private Boolean rowNumbers = true;
	private Boolean singleSelect = true;
	private Boolean striped = true;
	private List<OButton<T>> toolbar;
	private String url;

	// ------
	private String gridHTMLId;

	public OBaseGrid() {
		pageList = Arrays.asList(10, 20, 30, 40, 50);
		pageSize = pageList.get(0);
	}

	public Boolean getAutoRowHeight() {
		return autoRowHeight;
	}

	public OBaseGrid<T> setAutoRowHeight(Boolean autoRowHeight) {
		this.autoRowHeight = autoRowHeight;
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
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public OBaseGrid<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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

	public String getToolbar() {
		return toolbar != null ? String.format("#%s-tb", gridHTMLId) : null;
	}

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

	public String getUrl() {
		return url;
	}

	public OBaseGrid<T> setUrl(String url) {
		this.url = url;
		return this;
	}

	// ---------------------- PUBLIC METHODS

	@JsonIgnore
	public String getGridHTMLId() {
		return gridHTMLId;
	}

	public void setGridHTMLId(String gridHTMLId) {
		this.gridHTMLId = gridHTMLId;
	}

}

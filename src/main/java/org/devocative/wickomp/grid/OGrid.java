package org.devocative.wickomp.grid;

import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.opt.OComponent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class OGrid<T extends Serializable> extends OComponent {
	private Boolean autoRowHeight = false;
	private OColumnList<T> columns;
	private Boolean pagination = true;
	private List<Integer> pageList;
	private Integer pageSize;
	private String url;

	public OGrid() {
		pageList = Arrays.asList(10, 20, 30, 40, 50);
		pageSize = pageList.get(0);
	}

	public Boolean getAutoRowHeight() {
		return autoRowHeight;
	}

	public void setAutoRowHeight(Boolean autoRowHeight) {
		this.autoRowHeight = autoRowHeight;
	}

	public OColumnList<T> getColumns() {
		return columns;
	}

	public void setColumns(OColumnList<T> columns) {
		this.columns = columns;
	}

	public Boolean getPagination() {
		return pagination;
	}

	public void setPagination(Boolean pagination) {
		this.pagination = pagination;
	}

	public List<Integer> getPageList() {
		return pageList;
	}

	public void setPageList(List<Integer> pageList) {
		this.pageList = pageList;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

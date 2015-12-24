package org.devocative.wickomp.grid.toolbar;

import org.devocative.wickomp.data.DataSource;
import org.devocative.wickomp.data.SortField;
import org.devocative.wickomp.grid.column.OColumn;

import java.io.Serializable;
import java.util.List;

public class GridInfo<T extends Serializable> {
	private List<OColumn<T>> columns;
	private DataSource<T> dataSource;
	private List<SortField> sortFieldList;

	public GridInfo(List<OColumn<T>> columns, DataSource<T> dataSource, List<SortField> sortFieldList) {
		this.columns = columns;
		this.dataSource = dataSource;
		this.sortFieldList = sortFieldList;
	}

	public List<OColumn<T>> getColumns() {
		return columns;
	}

	public DataSource<T> getDataSource() {
		return dataSource;
	}

	public List<SortField> getSortFieldList() {
		return sortFieldList;
	}
}

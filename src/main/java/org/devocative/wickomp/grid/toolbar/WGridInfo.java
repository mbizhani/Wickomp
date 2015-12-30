package org.devocative.wickomp.grid.toolbar;

import org.devocative.wickomp.data.WDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.grid.column.OColumn;

import java.io.Serializable;
import java.util.List;

public class WGridInfo<T extends Serializable> {
	private List<OColumn<T>> columns;
	private WDataSource<T> dataSource;
	private List<WSortField> sortFieldList;

	public WGridInfo(List<OColumn<T>> columns, WDataSource<T> dataSource, List<WSortField> sortFieldList) {
		this.columns = columns;
		this.dataSource = dataSource;
		this.sortFieldList = sortFieldList;
	}

	public List<OColumn<T>> getColumns() {
		return columns;
	}

	public WDataSource<T> getDataSource() {
		return dataSource;
	}

	public List<WSortField> getSortFieldList() {
		return sortFieldList;
	}
}

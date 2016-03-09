package org.devocative.wickomp.grid.toolbar;

import org.devocative.wickomp.grid.IGridDataSource;
import org.devocative.wickomp.grid.OBaseGrid;
import org.devocative.wickomp.grid.WSortField;

import java.util.List;

public class WGridInfo<T> {
	private OBaseGrid<T> options;
	private IGridDataSource<T> dataSource;
	private List<WSortField> sortFieldList;

	public WGridInfo(OBaseGrid<T> options, IGridDataSource<T> dataSource, List<WSortField> sortFieldList) {
		this.options = options;
		this.dataSource = dataSource;
		this.sortFieldList = sortFieldList;
	}

	public OBaseGrid<T> getOptions() {
		return options;
	}

	public IGridDataSource<T> getDataSource() {
		return dataSource;
	}

	public List<WSortField> getSortFieldList() {
		return sortFieldList;
	}
}

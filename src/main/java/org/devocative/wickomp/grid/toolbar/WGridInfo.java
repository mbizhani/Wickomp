package org.devocative.wickomp.grid.toolbar;

import org.devocative.wickomp.data.WGridDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.grid.OBaseGrid;

import java.util.List;

public class WGridInfo<T> {
	private OBaseGrid<T> options;
	private WGridDataSource<T> dataSource;
	private List<WSortField> sortFieldList;

	public WGridInfo(OBaseGrid<T> options, WGridDataSource<T> dataSource, List<WSortField> sortFieldList) {
		this.options = options;
		this.dataSource = dataSource;
		this.sortFieldList = sortFieldList;
	}

	public OBaseGrid<T> getOptions() {
		return options;
	}

	public WGridDataSource<T> getDataSource() {
		return dataSource;
	}

	public List<WSortField> getSortFieldList() {
		return sortFieldList;
	}
}

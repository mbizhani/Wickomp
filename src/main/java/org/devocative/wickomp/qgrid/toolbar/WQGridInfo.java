package org.devocative.wickomp.qgrid.toolbar;

import org.devocative.wickomp.data.WDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.qgrid.OQGrid;

import java.util.List;

public class WQGridInfo<T> {
	private OQGrid<T> options;
	private WDataSource<T> dataSource;
	private List<WSortField> sortFieldList;

	public WQGridInfo(OQGrid<T> options, WDataSource<T> dataSource, List<WSortField> sortFieldList) {
		this.options = options;
		this.dataSource = dataSource;
		this.sortFieldList = sortFieldList;
	}

	public OQGrid<T> getOptions() {
		return options;
	}

	public WDataSource<T> getDataSource() {
		return dataSource;
	}

	public List<WSortField> getSortFieldList() {
		return sortFieldList;
	}
}

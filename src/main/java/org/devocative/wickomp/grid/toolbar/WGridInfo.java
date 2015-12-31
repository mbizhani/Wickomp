package org.devocative.wickomp.grid.toolbar;

import org.devocative.wickomp.data.WDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.grid.OGrid;

import java.util.List;

public class WGridInfo<T> {
	private OGrid<T> options;
	private WDataSource<T> dataSource;
	private List<WSortField> sortFieldList;

	public WGridInfo(OGrid<T> options, WDataSource<T> dataSource, List<WSortField> sortFieldList) {
		this.options = options;
		this.dataSource = dataSource;
		this.sortFieldList = sortFieldList;
	}

	public OGrid<T> getOptions() {
		return options;
	}

	public WDataSource<T> getDataSource() {
		return dataSource;
	}

	public List<WSortField> getSortFieldList() {
		return sortFieldList;
	}
}

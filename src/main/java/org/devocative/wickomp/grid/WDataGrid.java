package org.devocative.wickomp.grid;

import org.devocative.wickomp.data.WGridDataSource;

public class WDataGrid<T> extends WBaseGrid<T> {
	public WDataGrid(String id, OGrid<T> options, WGridDataSource<T> dataSource) {
		super(id, options, dataSource);
	}

	@Override
	protected String getJQueryFunction() {
		return "datagrid";
	}
}

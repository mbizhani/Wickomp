package org.devocative.wickomp.grid;

public class WDataGrid<T> extends WBaseGrid<T> {
	private static final long serialVersionUID = 6072128227940068231L;

	public WDataGrid(String id, OGrid<T> options, IGridDataSource<T> dataSource) {
		super(id, options, dataSource);
	}

	public WDataGrid(String id, OGrid<T> options, IGridAsyncDataSource<T> dataSource) {
		super(id, options, dataSource);
	}

	@Override
	protected String getJQueryFunction() {
		return "wDataGrid";
	}
}

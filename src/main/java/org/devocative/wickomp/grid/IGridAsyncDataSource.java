package org.devocative.wickomp.grid;

import java.util.List;

public interface IGridAsyncDataSource<T> extends IDataSource<T> {
	/**
	 * This method returns a page of result
	 *
	 * @param pageIndex  page number during pagination process starts with 1
	 * @param pageSize   size of a page during pagination process
	 * @param sortFields list of fields for sorting the result (optional)
	 * @return list of result
	 */
	void list(long pageIndex, long pageSize, List<WSortField> sortFields);
}

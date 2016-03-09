package org.devocative.wickomp.grid;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public interface IGridDataSource<T> extends Serializable {
	/**
	 * This method returns a page of result
	 *
	 * @param pageIndex  page number during pagination process starts with 1
	 * @param pageSize   size of a page during pagination process
	 * @param sortFields list of fields for sorting the result (optional)
	 * @return list of result
	 */
	List<T> list(long pageIndex, long pageSize, List<WSortField> sortFields);

	long count();

	IModel<T> model(T object);
}

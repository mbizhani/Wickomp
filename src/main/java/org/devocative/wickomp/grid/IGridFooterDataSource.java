package org.devocative.wickomp.grid;

import java.util.List;

public interface IGridFooterDataSource<T> {
	List<?> footer(List<T> pagedData);
}

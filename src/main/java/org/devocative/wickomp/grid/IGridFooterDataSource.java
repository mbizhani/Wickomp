package org.devocative.wickomp.grid;

import java.io.Serializable;
import java.util.List;

public interface IGridFooterDataSource<T> extends Serializable {
	List<?> footer(List<T> pagedData);
}

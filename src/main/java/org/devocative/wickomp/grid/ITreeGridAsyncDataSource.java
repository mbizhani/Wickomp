package org.devocative.wickomp.grid;

import java.io.Serializable;
import java.util.List;

public interface ITreeGridAsyncDataSource<T> extends IGridAsyncDataSource<T> {
	void asyncListByParent(Serializable parentId, List<WSortField> sortFields);

	boolean hasChildren(T bean);
}

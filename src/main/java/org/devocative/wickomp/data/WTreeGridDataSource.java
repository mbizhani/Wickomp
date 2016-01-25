package org.devocative.wickomp.data;

import java.util.List;

public abstract class WTreeGridDataSource<T> extends WGridDataSource<T> {
	public abstract List<T> listByParent(String parentId);

	public abstract boolean hasChildren(T bean);
}

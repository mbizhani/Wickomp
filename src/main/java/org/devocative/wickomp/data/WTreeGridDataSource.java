package org.devocative.wickomp.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class WTreeGridDataSource<T> extends WGridDataSource<T> {
	public abstract List<T> listByParent(Serializable parentId, List<WSortField> sortFields);

	public abstract List<T> listByIds(Set<Serializable> ids, List<WSortField> sortFields);

	public abstract boolean hasChildren(T bean);
}

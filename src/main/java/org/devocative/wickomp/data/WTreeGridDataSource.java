package org.devocative.wickomp.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface WTreeGridDataSource<T> extends WGridDataSource<T> {
	List<T> listByParent(Serializable parentId, List<WSortField> sortFields);

	List<T> listByIds(Set<Serializable> ids, List<WSortField> sortFields);

	boolean hasChildren(T bean);
}

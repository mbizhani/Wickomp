package org.devocative.wickomp.data;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public interface ODataSource<T extends Serializable> extends Serializable {
	public List<T> list(long first, long size);

	public long count();

	public IModel<T> model(T object);
}

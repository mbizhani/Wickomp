package org.devocative.wickomp.data;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public abstract class DataSource<T extends Serializable> implements Serializable {
	private boolean enabled = true;

	public abstract List<T> list(long first, long size, List<SortField> sortFields);

	public abstract long count();

	public abstract IModel<T> model(T object);

	public boolean isEnabled() {
		return enabled;
	}

	public DataSource setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
}

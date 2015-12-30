package org.devocative.wickomp.data;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public abstract class WDataSource<T> implements Serializable {
	private boolean enabled = true;

	public abstract List<T> list(long first, long size, List<WSortField> sortFields);

	public abstract long count();

	public abstract IModel<T> model(T object);

	public boolean isEnabled() {
		return enabled;
	}

	public WDataSource setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
}

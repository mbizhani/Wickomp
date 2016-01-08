package org.devocative.wickomp.data;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public abstract class WDataSource<T> implements Serializable {
	private boolean enabled = true;

	/**
	 * This method returns a page of result
	 *
	 * @param pageIndex  page number during pagination process starts with 1
	 * @param pageSize   size of a page during pagination process
	 * @param sortFields list of fields for sorting the result (optional)
	 * @return list of result
	 */
	public abstract List<T> list(long pageIndex, long pageSize, List<WSortField> sortFields);

	public abstract long count();

	public abstract IModel<T> model(T object);

	public boolean isEnabled() {
		return enabled;
	}

	public WDataSource<T> setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
}

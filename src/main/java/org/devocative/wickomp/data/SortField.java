package org.devocative.wickomp.data;

import java.io.Serializable;

public class SortField implements Serializable {
	private String field;
	private String order;

	public SortField(String field, String order) {
		this.field = field;
		this.order = order;
	}

	public String getField() {
		return field;
	}

	public String getOrder() {
		return order;
	}

	public SortField setOrder(String order) {
		this.order = order;
		return this;
	}

	@Override
	public String toString() {
		return String.format("{%s : %s}", field, order);
	}
}

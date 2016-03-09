package org.devocative.wickomp.grid;

import java.io.Serializable;

public class WSortField implements Serializable {
	private String field;
	private String order;

	public WSortField(String field, String order) {
		this.field = field;
		this.order = order;
	}

	public String getField() {
		return field;
	}

	public String getOrder() {
		return order;
	}

	public WSortField setOrder(String order) {
		this.order = order;
		return this;
	}

	@Override
	public String toString() {
		return String.format("{%s : %s}", field, order);
	}
}

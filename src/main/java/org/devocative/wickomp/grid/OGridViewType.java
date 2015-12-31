package org.devocative.wickomp.grid;

public enum OGridViewType {
	GroupView("groupview");

	private String value;

	OGridViewType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}

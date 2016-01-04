package org.devocative.wickomp.opt;

public enum OLayoutDirection {
	RTL("rtl"), LTR("ltr");

	private String value;

	OLayoutDirection(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return value;
	}
}

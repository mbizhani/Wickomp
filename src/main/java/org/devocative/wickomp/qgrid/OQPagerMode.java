package org.devocative.wickomp.qgrid;

public enum OQPagerMode {
	Simple("simple"), Default("default");
//	Simple, Default;

	private String value;

	OQPagerMode(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return value;
	}
}

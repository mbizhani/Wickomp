package org.devocative.wickomp.opt;

public enum OHorizontalAlign {
	Right("right"), Center("center"), Left("left");

	private String value;

	OHorizontalAlign(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return value;
	}
}

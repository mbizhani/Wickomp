package org.devocative.wickomp.qgrid;

public enum OQSelectionMode {
	None("none"),
	SingleRow("singlerow"), MultipleRows("multiplerows"),
	SingleCell("singlecell"), MultipleCells("multiplecells");

	private String value;

	OQSelectionMode(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return value;
	}
}

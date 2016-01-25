package org.devocative.wickomp.grid;

public class OTreeGrid<T> extends OBaseGrid<T> {
	private Boolean animate = true;
	private Boolean lines;
	private String treeField;

	public Boolean getAnimate() {
		return animate;
	}

	public OTreeGrid<T> setAnimate(Boolean animate) {
		this.animate = animate;
		return this;
	}

	public Boolean getLines() {
		return lines;
	}

	public OTreeGrid<T> setLines(Boolean lines) {
		this.lines = lines;
		return this;
	}

	public String getTreeField() {
		return treeField;
	}

	public OTreeGrid<T> setTreeField(String treeField) {
		this.treeField = treeField;
		return this;
	}
}

package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OTreeGrid<T> extends OBaseGrid<T> {
	private Boolean animate = true;
	private Boolean lines;
	private String treeField;
	private String parentIdField;

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

	@JsonIgnore
	public String getParentIdField() {
		return parentIdField;
	}

	public OTreeGrid<T> setParentIdField(String parentIdField) {
		this.parentIdField = parentIdField;
		return this;
	}
}

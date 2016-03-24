package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OTreeGrid<T> extends OBaseGrid<T> {
	private Boolean animate = true;
	private Boolean lines;
	private String treeField;
	private Boolean showLines;

	// -----
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

	public Boolean getShowLines() {
		return showLines;
	}

	public OTreeGrid<T> setShowLines(Boolean showLines) {
		this.showLines = showLines;
		return this;
	}

	// -----

	@JsonIgnore
	public String getParentIdField() {
		return parentIdField;
	}

	public OTreeGrid<T> setParentIdField(String parentIdField) {
		this.parentIdField = parentIdField;
		return this;
	}

	@Override
	protected String getSelectionJSFunc(String anotherFunction) {
		if (selectionIndicator || anotherFunction != null) {
			StringBuilder builder = new StringBuilder();
			builder.append("function(row,data){");
			builder.append(selectionJSHandler == null ?
					String.format("handleSelectionIndicator('%s');", htmlId) :
					String.format("handleSelectionIndicator('%s', %s);", htmlId, selectionJSHandler)
			);
			if (anotherFunction != null) {
				builder.append(anotherFunction);
			}
			return builder.append("}").toString();
		}
		return null;
	}

}

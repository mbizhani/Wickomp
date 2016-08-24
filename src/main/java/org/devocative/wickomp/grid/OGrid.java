package org.devocative.wickomp.grid;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class OGrid<T> extends OBaseGrid<T> {
	private static final long serialVersionUID = -5090764152549218606L;

	private String groupField;
	private String groupStyle;
	private String groupStyler;
	private OGridViewType view;

	// ------------------------- ACCESSORS

	public String getGroupField() {
		return groupField;
	}

	public OGrid<T> setGroupField(String groupField) {
		this.groupField = groupField;
		this.view = OGridViewType.GroupView;
		return this;
	}

	@JsonRawValue
	public String getGroupFormatter() {
		return getGroupField() != null ? "function(value,rows){return value + ' (' + rows.length + ')';}" : null;
	}

	public String getGroupStyle() {
		return groupStyle;
	}

	public OGrid<T> setGroupStyle(String groupStyle) {
		this.groupStyle = groupStyle;
		return this;
	}

	@JsonRawValue
	public String getGroupStyler() {
		return groupStyler;
	}

	public OGrid<T> setGroupStyler(String groupStyler) {
		this.groupStyler = groupStyler;
		return this;
	}

	@JsonRawValue
	public OGridViewType getView() {
		return view;
	}
}

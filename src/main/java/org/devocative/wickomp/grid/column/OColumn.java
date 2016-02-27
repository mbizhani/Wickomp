package org.devocative.wickomp.grid.column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.formatter.OFormatter;
import org.devocative.wickomp.opt.OHorizontalAlign;
import org.devocative.wickomp.opt.Options;

public abstract class OColumn<T> extends Options {
	private OHorizontalAlign align;
	private String field;
	private Boolean resizable;
	private Boolean sortable;
	private IModel<String> title;

	// -----------------
	private boolean dummyField = false;
	protected OFormatter formatter;
	private boolean visible = true;

	public OColumn(IModel<String> title) {
		this(title, null);
	}

	public OColumn(IModel<String> title, String field) {
		this.title = title;
		this.field = field;
	}

	//----------------------- ACCESSORS

	public OHorizontalAlign getAlign() {
		return align;
	}

	public OColumn<T> setAlign(OHorizontalAlign align) {
		this.align = align;
		return this;
	}

	public String getField() {
		return field;
	}

	public OColumn<T> setField(String field) {
		this.field = field;
		return this;
	}

	public Boolean getResizable() {
		return resizable;
	}

	public OColumn<T> setResizable(Boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public OColumn<T> setSortable(Boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	public String getTitle() {
		return title != null ? title.getObject() : "";
	}

	public OColumn<T> setFormatter(OFormatter formatter) {
		this.formatter = formatter;
		return this;
	}

	//----------------------- PUBLIC METHODS

	@JsonIgnore
	public boolean isDummyField() {
		return dummyField;
	}

	public void setDummyField(boolean dummyField) {
		this.dummyField = dummyField;
	}

	@JsonIgnore
	public boolean isVisible() {
		return visible;
	}

	public OColumn<T> setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public boolean onCellRender(T bean, String id) {
		return true;
	}

	public abstract String cellValue(T bean, String id, int colNo, String url);
}

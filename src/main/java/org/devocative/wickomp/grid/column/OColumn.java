package org.devocative.wickomp.grid.column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.formatter.OFormatter;
import org.devocative.wickomp.opt.OHorizontalAlign;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.opt.Options;

public abstract class OColumn<T> extends Options {
	private static final long serialVersionUID = -8318681492794608789L;

	// ------------------------------ JSON FIELDS

	private OHorizontalAlign align;
	private String field;
	private Boolean resizable;
	private Boolean sortable;
	private IModel<String> title;
	private OSize width;

	// ---------------

	private String style;
	private String styleClass;

	// ------------------------------ MISC FIELDS

	private boolean dummyField = false;
	private boolean visible = true;
	private boolean hasFooter = false;
	protected OFormatter formatter;

	// ------------------------------ CONSTRUCTORS

	public OColumn(IModel<String> title) {
		this(title, null);
	}

	public OColumn(IModel<String> title, String field) {
		this.title = title;
		this.field = field;
	}

	// ------------------------------ ACCESSORS for JSON

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

	@JsonRawValue
	public String getStyler() {
		if (style != null || styleClass != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(" function(value,row,index){var r={};");
			if (style != null) {
				builder.append(String.format("r['style']='%s';", style));
			}
			if (styleClass != null) {
				builder.append(String.format("r['class']='%s';", styleClass));
			}
			builder.append("return r;}");
			return builder.toString();
		}
		return null;
	}

	public String getTitle() {
		return title != null ? title.getObject() : "";
	}

	public OSize getWidth() {
		return width;
	}

	public OColumn<T> setWidth(OSize width) {
		this.width = width;
		return this;
	}

	// ---------------

	public String getStyle() {
		return style;
	}

	public OColumn<T> setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public OColumn<T> setStyleClass(String styleClass) {
		this.styleClass = styleClass;
		return this;
	}

	// ------------------------------ PUBLIC ABSTRACT METHODS

	public abstract String cellValue(T bean, String id, int colNo, String url);

	public abstract String footerCellValue(Object bean, int colNo, String url);

	// ------------------------------ PUBLIC METHODS

	public OColumn<T> setFormatter(OFormatter formatter) {
		this.formatter = formatter;
		return this;
	}

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

	@JsonIgnore
	public boolean isHasFooter() {
		return hasFooter;
	}

	public OColumn<T> setHasFooter(boolean hasFooter) {
		this.hasFooter = hasFooter;
		return this;
	}

	public boolean onCellRender(T bean, String id) {
		return true;
	}
}

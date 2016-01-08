package org.devocative.wickomp.qgrid.column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.formatter.OFormatter;
import org.devocative.wickomp.opt.OHorizontalAlign;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.opt.Options;

public abstract class OQColumn<T> extends Options {
	private OHorizontalAlign align;
	;
	private String cellClassName;
	private OHorizontalAlign cellsAlign;
	private String cellsRenderer;
	private String dataField;
	private IModel<String> text;
	private OSize width;

	private boolean dummyField = false;
	protected OFormatter formatter;

	public OQColumn() {
		this(null, null);
	}

	public OQColumn(IModel<String> text) {
		this(text, null);
	}

	// Main Constructor
	public OQColumn(IModel<String> text, String dataField) {
		this.dataField = dataField;
		this.text = text;
	}

	//----------------------- ACCESSORS

	public OHorizontalAlign getAlign() {
		return align;
	}

	public OQColumn setAlign(OHorizontalAlign align) {
		this.align = align;
		return this;
	}

	public String getCellClassName() {
		return cellClassName;
	}

	public OQColumn setCellClassName(String cellClassName) {
		this.cellClassName = cellClassName;
		return this;
	}

	public OHorizontalAlign getCellsAlign() {
		return cellsAlign;
	}

	public OQColumn setCellsAlign(OHorizontalAlign cellsAlign) {
		this.cellsAlign = cellsAlign;
		return this;
	}

	// "function(row, columnField, value, defaultHTML, columnProperties){return '?'}";
	@JsonRawValue
	public String getCellsRenderer() {
		return cellsRenderer;
	}

	public OQColumn setCellsRenderer(String cellsRenderer) {
		this.cellsRenderer = cellsRenderer;
		return this;
	}

	public String getDataField() {
		return dataField;
	}

	public OQColumn setDataField(String dataField) {
		this.dataField = dataField;
		return this;
	}

	public String getText() {
		return text.getObject();
	}

	public OQColumn setText(IModel<String> text) {
		this.text = text;
		return this;
	}

	public OSize getWidth() {
		return width;
	}

	public OQColumn setWidth(OSize width) {
		this.width = width;
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

	public OQColumn<T> setFormatter(OFormatter formatter) {
		this.formatter = formatter;
		return this;
	}

	public boolean onCellRender(T bean, int rowNo) {
		return true;
	}

	public abstract String cellValue(T bean, int rowNo, int colNo, String url);
}

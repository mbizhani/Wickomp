package org.devocative.wickomp.grid.column;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.opt.HAlign;
import org.devocative.wickomp.opt.Options;

import java.io.Serializable;

public abstract class OColumn<T extends Serializable> extends Options {
	private IModel<String> title;
	private String field;
	private Boolean resizable;
	private HAlign align;

	public OColumn(IModel<String> title) {
		this(title, null);
	}

	public OColumn(IModel<String> title, String field) {
		this.title = title;
		this.field = field;
	}

	public String getTitle() {
		return title.getObject();
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

	public HAlign getAlign() {
		return align;
	}

	public OColumn<T> setAlign(HAlign align) {
		this.align = align;
		return this;
	}

	public boolean onCellRender(T bean, int rowNo) {
		return true;
	}

	public abstract String cellValue(T bean, int rowNo, int colNo, String url);
}

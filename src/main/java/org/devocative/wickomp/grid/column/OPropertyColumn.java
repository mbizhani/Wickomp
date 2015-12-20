package org.devocative.wickomp.grid.column;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

public class OPropertyColumn<T extends Serializable> extends OColumn<T> {
	public OPropertyColumn(IModel<String> text, String property) {
		super(text, property);
	}

	@Override
	public String cellValue(T bean, int rowNo, int colNo, String url) {
		Object value = PropertyResolver.getValue(getField(), bean);
		return value != null ? value.toString() : null;
	}
}

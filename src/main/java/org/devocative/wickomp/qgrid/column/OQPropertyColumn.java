package org.devocative.wickomp.qgrid.column;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;

public class OQPropertyColumn<T> extends OQColumn<T> {
	public OQPropertyColumn(IModel<String> text, String property) {
		super(text, property);
	}

	@Override
	public String cellValue(T bean, int rowNo, int colNo, String url) {
		Object value = PropertyResolver.getValue(getDataField(), bean);
		if (value != null) {
			return formatter != null ? formatter.format(value) : value.toString();
		}
		return null;
	}
}

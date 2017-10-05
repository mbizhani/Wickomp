package org.devocative.wickomp.grid.column;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;

public class OPropertyColumn<T> extends OColumn<T> {
	private static final long serialVersionUID = -1837631056759898671L;

	private String property;

	// ------------------------------

	public OPropertyColumn(IModel<String> text, String property) {
		this(text, property, property);
	}

	// Main Constructor
	public OPropertyColumn(IModel<String> text, String field, String property) {
		super(text, field);

		this.property = property;
	}

	// ------------------------------

	@Override
	public String cellValue(T bean, String id, int colNo, String url) {
		Object value = PropertyResolver.getValue(property, bean);
		if (value != null) {
			try {
				return formatter != null ? formatter.format(value) : value.toString();
			} catch (Exception e) {
				throw new RuntimeException("Formatting Problem: field = " + property, e);
			}
		}
		return null;
	}

	@Override
	public String footerCellValue(Object bean, int colNo, String url) {
		Object value = PropertyResolver.getValue(property, bean);
		if (value != null) {
			try {
				return formatter != null ? formatter.format(value) : value.toString();
			} catch (Exception e) {
				throw new RuntimeException("Formatting Problem: field = " + property, e);
			}
		}
		return null;
	}
}

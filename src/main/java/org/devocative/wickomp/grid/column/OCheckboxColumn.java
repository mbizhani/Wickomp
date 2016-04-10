package org.devocative.wickomp.grid.column;

import org.apache.wicket.model.Model;

public class OCheckboxColumn<T> extends OColumn<T> {
	public OCheckboxColumn() {
		super(new Model<String>());

		setField("ck");
		setDummyField(true);
	}

	public boolean getCheckbox() {
		return true;
	}

	@Override
	public boolean onCellRender(T bean, String id) {
		return false;
	}

	@Override
	public String cellValue(T bean, String id, int colNo, String url) {
		return null;
	}

	@Override
	public String footerCellValue(Object bean, int colNo, String url) {
		throw new RuntimeException("Invalid footer for OCheckboxColumn");
	}
}

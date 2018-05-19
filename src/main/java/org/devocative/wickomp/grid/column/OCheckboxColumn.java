package org.devocative.wickomp.grid.column;

import org.apache.wicket.model.Model;

public class OCheckboxColumn<T> extends OColumn<T> {
	private static final long serialVersionUID = 8179373579392387730L;

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
}

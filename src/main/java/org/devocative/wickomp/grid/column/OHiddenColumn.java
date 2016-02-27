package org.devocative.wickomp.grid.column;

public class OHiddenColumn<T> extends OPropertyColumn<T> {
	public OHiddenColumn(String field) {
		super(null, field);

		setVisible(false);
	}
}

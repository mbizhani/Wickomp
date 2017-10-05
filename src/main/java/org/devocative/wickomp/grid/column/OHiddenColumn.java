package org.devocative.wickomp.grid.column;

public class OHiddenColumn<T> extends OPropertyColumn<T> {
	private static final long serialVersionUID = 4831642736281393840L;

	public OHiddenColumn(String property) {
		this(property, property);
	}

	// Main Constructor
	public OHiddenColumn(String field, String property) {
		super(null, field, property);

		setVisible(false);
	}
}

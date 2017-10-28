package org.devocative.wickomp.html.window;

public class WModalWindow extends WWindow {
	private static final long serialVersionUID = 8033724982656671879L;

	public WModalWindow(String id) {
		this(id, new OModalWindow());
	}

	// Main Constructor
	public WModalWindow(String id, OModalWindow options) {
		super(id, options);
	}

	// ------------------------------

	public OModalWindow getOptions() {
		return (OModalWindow) super.getOptions();
	}
}

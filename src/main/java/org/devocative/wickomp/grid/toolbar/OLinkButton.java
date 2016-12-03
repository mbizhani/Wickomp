package org.devocative.wickomp.grid.toolbar;

import org.devocative.wickomp.html.HTMLBase;

public abstract class OLinkButton<T> extends OButton<T> {
	private static final long serialVersionUID = -7438072264428914280L;

	protected HTMLBase html;

	// ------------------------------

	public OLinkButton(HTMLBase html) {
		this.html = html;
	}

	// ------------------------------

	@Override
	public String getHTMLContent() {
		return String.format("<a class=\"easyui-linkbutton\" plain=\"true\" href=\"%s\">%s</a>", getCallbackURL(), html.toString());
	}

	// ------------------------------

	public abstract void onClick();
}

package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.devocative.wickomp.html.HTMLBase;

public abstract class OAjaxLinkButton<T> extends OButton<T> {
	private static final long serialVersionUID = 3093338370990179435L;

	protected HTMLBase html;

	// ------------------------------

	public OAjaxLinkButton(HTMLBase html) {
		this.html = html;
	}

	// ------------------------------

	@Override
	public String getHTMLContent() {
		return String.format(
			"<a class=\"%s\" href=\"#\" onclick=\"Wicket.Ajax.get({u:'%s'})\">%s</a>",
			TOOLBAR_BUT_HTML_CLASS, getCallbackURL(), html.toString());
	}

	// ------------------------------

	public abstract void onClick(AjaxRequestTarget target);
}

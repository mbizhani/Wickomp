package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;
import org.devocative.wickomp.html.HTMLBase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
			"<a class=\"%s\" href=\"#\" onclick=\"Wicket.Ajax.get({u:'%s' + $('#%s').datagrid('options')['selectedAsUrl']})\">%s</a>",
			TOOLBAR_BUT_HTML_CLASS, getCallbackURL(), getGridHtmlId(), html.toString());
	}

	// ------------------------------

	public abstract void onClick(AjaxRequestTarget target);

	// ------------------------------

	protected final List<String> getSelectedRowsKeys() {
		List<StringValue> valueList = RequestCycle.get()
			.getRequest()
			.getRequestParameters()
			.getParameterValues("$selkey");

		if (valueList != null) {
			return valueList
				.stream()
				.map(StringValue::toString)
				.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}
}

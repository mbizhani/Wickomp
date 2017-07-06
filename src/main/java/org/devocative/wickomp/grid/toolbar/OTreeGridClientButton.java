package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.model.ResourceModel;
import org.devocative.wickomp.html.HTMLBase;

public class OTreeGridClientButton<T> extends OButton<T> {
	private static final long serialVersionUID = 5728174607489192106L;

	private HTMLBase expand, collapse;

	public OTreeGridClientButton(HTMLBase collapse) {
		this(null, collapse);
	}

	public OTreeGridClientButton(HTMLBase expand, HTMLBase collapse) {
		this.expand = expand;
		this.collapse = collapse;
	}

	@Override
	public String getHTMLContent() {
		StringBuilder builder = new StringBuilder();

		if (expand != null) {
			builder
				.append(String.format("<a class=\"%s\" title=\"", TOOLBAR_BUT_HTML_CLASS))
				.append(new ResourceModel("label.nodes.expand", "Expand All").getObject())
				.append("\" onclick=\"")
				.append(String.format("$('#%s').treegrid('expandAll');", getGridHtmlId()))
				.append("\">")
				.append(expand.toString())
				.append("</a>")
			;
		}

		builder
			.append(String.format("<a class=\"%s\" title=\"", TOOLBAR_BUT_HTML_CLASS))
			.append(new ResourceModel("label.nodes.collapse", "Collapse All").getObject())
			.append("\" onclick=\"")
			.append(String.format("$('#%s').treegrid('collapseAll');", getGridHtmlId()))
			.append("\">")
			.append(collapse.toString())
			.append("</a>")
		;

		return builder.toString();
	}
}

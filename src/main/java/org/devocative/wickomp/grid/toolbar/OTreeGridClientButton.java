package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.model.ResourceModel;
import org.devocative.wickomp.html.HTMLBase;

public class OTreeGridClientButton<T> extends OButton<T> {
	private HTMLBase expand, collapse;

	public OTreeGridClientButton(HTMLBase collapse) {
		this(null, collapse);
	}

	public OTreeGridClientButton(HTMLBase expand, HTMLBase collapse) {
		this.expand = expand;
		this.collapse = collapse;
	}

	@Override
	public String getHTMLContent(WGridInfo<T> gridInfo) {
		StringBuilder builder = new StringBuilder();

		if (expand != null) {
			builder
				.append("<a class=\"easyui-linkbutton\" plain=\"true\" title=\"")
				.append(new ResourceModel("label.nodes.expand", "Expand All").getObject())
				.append("\" onclick=\"")
				.append(String.format("$('#%s').treegrid('expandAll');", gridInfo.getOptions().getHtmlId()))
				.append("\">")
				.append(expand.toString())
				.append("</a>")
			;
		}

		builder
			.append("<a class=\"easyui-linkbutton\" plain=\"true\" title=\"")
			.append(new ResourceModel("label.nodes.collapse", "Collapse All").getObject())
			.append("\" onclick=\"")
			.append(String.format("$('#%s').treegrid('collapseAll');", gridInfo.getOptions().getHtmlId()))
			.append("\">")
			.append(collapse.toString())
			.append("</a>")
		;

		return builder.toString();
	}
}

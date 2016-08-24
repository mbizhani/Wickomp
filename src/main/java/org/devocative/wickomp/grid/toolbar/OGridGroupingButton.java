package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.model.ResourceModel;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.html.HTMLBase;

public class OGridGroupingButton<T> extends OButton<T> {
	private static final long serialVersionUID = -5807113340530435125L;

	private HTMLBase expand, collapse;

	public OGridGroupingButton() {
	}

	public OGridGroupingButton(HTMLBase expand, HTMLBase collapse) {
		this.expand = expand;
		this.collapse = collapse;
	}

	@Override
	public String getHTMLContent(WGridInfo<T> gridInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append("<table><tr>");
		builder.append(String.format("<td>%s: </td><td><select onchange=\"changeGridGroupField(this, '%s')\">",
			new ResourceModel("datagrid.groupField.select", "Group Field").getObject(), gridInfo.getOptions().getHtmlId()));

		builder.append(String.format("<option value=\"\">%s</option>", new ResourceModel("datagrid.groupField.none", "-- None --").getObject()));

		for (OColumn<T> column : gridInfo.getOptions().getColumns().getVisibleColumns()) {
			if (column instanceof OPropertyColumn) {
				builder.append(String.format("<option value=\"%s\">%s</option>", column.getField(), column.getTitle()));
			}
		}
		builder.append("</select></td>");

		if (expand != null) {
			builder.append("<td><a class=\"easyui-linkbutton\" plain=\"true\" title=\"")
				.append(new ResourceModel("label.nodes.expand", "Expand All").getObject())
				.append("\" onclick=\"")
				.append(String.format("expandAllGroups('%s');", gridInfo.getOptions().getHtmlId()))
				.append("\">")
				.append(expand.toString())
				.append("</a></td>")
			;
		}

		if (collapse != null) {
			builder.append("<td><a class=\"easyui-linkbutton\" plain=\"true\" title=\"")
				.append(new ResourceModel("label.nodes.expand", "Collapse All").getObject())
				.append("\" onclick=\"")
				.append(String.format("collapseAllGroups('%s');", gridInfo.getOptions().getHtmlId()))
				.append("\">")
				.append(collapse.toString())
				.append("</a></td>")
			;
		}

		builder.append("</tr></table>");
		return builder.toString();
	}
}

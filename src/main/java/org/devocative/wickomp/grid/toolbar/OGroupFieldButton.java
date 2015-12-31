package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.model.ResourceModel;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OPropertyColumn;

public class OGroupFieldButton<T> extends OButton<T> {
	@Override
	public String getHTMLContent(WGridInfo<T> gridInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%s: <select onchange=\"changeGridGroupField(this, '%s')\">",
			new ResourceModel("datagrid.groupField.select", "Group Field").getObject(), gridInfo.getOptions().getGridHTMLId()));

		builder.append(String.format("<option value=\"\">%s</option>", new ResourceModel("datagrid.groupField.none", "-- None --").getObject()));

		for (OColumn<T> column : gridInfo.getOptions().getColumns().getList()) {
			if (column instanceof OPropertyColumn) {
				builder.append(String.format("<option value=\"%s\">%s</option>", column.getField(), column.getTitle()));
			}
		}
		builder.append("</select>");
		return builder.toString();
	}
}

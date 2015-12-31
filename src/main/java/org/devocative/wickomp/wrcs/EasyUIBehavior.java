package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

public class EasyUIBehavior extends Behavior {
	private static HeaderItem CSS = Resource.getCommonCSS("easyui/themes/default/easyui.css");
	private static HeaderItem MAIN_JS = Resource.getCommonJS("easyui/jquery.easyui.min.js");
	private static HeaderItem GROUP_VIEW_JS = Resource.getCommonJS("easyui/ext/datagrid-groupview.js");
	private static HeaderItem MISC_JS = Resource.getCommonJS("easyui/ext/misc.js");

	public static void setCSS(HeaderItem CSS) {
		EasyUIBehavior.CSS = CSS;
	}

	public static void setMainJs(HeaderItem mainJs) {
		EasyUIBehavior.MAIN_JS = mainJs;
	}

	public static void setGroupViewJs(HeaderItem groupViewJs) {
		GROUP_VIEW_JS = groupViewJs;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(Resource.getJQueryReference());

		response.render(CSS);
		response.render(MAIN_JS);
		response.render(GROUP_VIEW_JS);
		response.render(MISC_JS);

		/*if (component instanceof WDataGrid) {
			OGrid grid = ((WDataGrid) component).getOptions();
			if (OGridViewType.GroupView.equals(grid.getView())) {
				response.render(GROUP_VIEW_JS);
			}
		}*/
	}
}

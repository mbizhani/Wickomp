package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.protocol.http.WebSession;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.opt.OUserPreference;

public class EasyUIBehavior extends Behavior {
	private static final long serialVersionUID = -1373990347234224329L;

	//	private static HeaderItem THEME_CSS = Resource.getCommonCSS("easyui/themes/metroBlue.css");
	private static HeaderItem THEME_CSS = Resource.getCommonCSS("easyui/themes/cbootstrap.css");
	private static HeaderItem MAIN_JS = Resource.getCommonJS("easyui/jquery.easyui.min.js");

	private static HeaderItem EXT_GROUP_VIEW_JS = Resource.getCommonJS("easyui/ext/datagrid-groupview.js");
	private static HeaderItem EXT_RTL_CSS = Resource.getCommonCSS("easyui/ext/rtl.css");
	private static HeaderItem EXT_RTL_JS = Resource.getCommonJS("easyui/ext/rtl.js");
	private static HeaderItem EXT_COLUMNS = Resource.getCommonJS("easyui/ext/columns-ext.js");
	private static HeaderItem EXT_COLUMNS_RTL = Resource.getCommonJS("easyui/ext/columns-ext-rtl.js");

	// ------------------------------

	public static void setThemeCSS(HeaderItem THEME_CSS) {
		EasyUIBehavior.THEME_CSS = THEME_CSS;
	}

	public static void setThemeName(String name) {
		EasyUIBehavior.THEME_CSS = Resource.getCommonCSS(String.format("easyui/themes/%s.css", name));
	}

	public static void setMainJs(HeaderItem mainJs) {
		EasyUIBehavior.MAIN_JS = mainJs;
	}

	public static void setExtGroupViewJs(HeaderItem extGroupViewJs) {
		EXT_GROUP_VIEW_JS = extGroupViewJs;
	}

	// ------------------------------

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		Resource.addJQueryReference(response);

		if (THEME_CSS != null) {
			response.render(THEME_CSS);
		}

		response.render(MAIN_JS);
		response.render(EXT_GROUP_VIEW_JS);

		OUserPreference userPreference = OUserPreference.DEFAULT;
		WebSession webSession = WebSession.get();
		if (webSession instanceof OUserPreference) {
			userPreference = (OUserPreference) webSession;
		}

		if (OLayoutDirection.RTL.equals(userPreference.getLayoutDirection())) {
			response.render(EXT_COLUMNS_RTL);

			response.render(EXT_RTL_CSS);
			response.render(EXT_RTL_JS);
		} else {
			response.render(EXT_COLUMNS);
		}
	}
}

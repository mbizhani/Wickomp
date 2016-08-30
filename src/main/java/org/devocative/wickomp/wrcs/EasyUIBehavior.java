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

	private static HeaderItem THEME_CSS = Resource.getCommonCSS("easyui/themes/metroBlue.css");
	private static HeaderItem MAIN_JS = Resource.getCommonJS("easyui/jquery.easyui.min.js");
	private static HeaderItem GROUP_VIEW_JS = Resource.getCommonJS("easyui/ext/datagrid-groupview.js");
	private static HeaderItem MISC_JS = Resource.getCommonJS("easyui/ext/misc.js");

	private static HeaderItem RTL_CSS = Resource.getCommonCSS("easyui/ext/rtl.css");
	private static HeaderItem RTL_JS = Resource.getCommonJS("easyui/ext/rtl.js");

	public static void setThemeCSS(HeaderItem THEME_CSS) {
		EasyUIBehavior.THEME_CSS = THEME_CSS;
	}

	public static void setMainJs(HeaderItem mainJs) {
		EasyUIBehavior.MAIN_JS = mainJs;
	}

	public static void setGroupViewJs(HeaderItem groupViewJs) {
		GROUP_VIEW_JS = groupViewJs;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		Resource.addJQueryReference(response);

		if (THEME_CSS != null) {
			response.render(THEME_CSS);
		}

		response.render(MAIN_JS);
		response.render(GROUP_VIEW_JS);
		response.render(MISC_JS);

		OUserPreference userPreference = OUserPreference.DEFAULT;
		WebSession webSession = WebSession.get();
		if (webSession instanceof OUserPreference) {
			userPreference = (OUserPreference) webSession;
		}

		if (OLayoutDirection.RTL.equals(userPreference.getLayoutDirection())) {
			response.render(RTL_CSS);
			response.render(RTL_JS);
		}
	}
}

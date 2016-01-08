package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

public class JqWidgetsBehavior extends Behavior {
	private static HeaderItem CSS = Resource.getCommonCSS("jqwidgets/styles/jqx.base.css");
	private static HeaderItem CHANGE_4_RTL = Resource.getCommonCSS("jqwidgets/styles/change4rtl.css");

	private static HeaderItem MAIN_JS = Resource.getCommonJS("jqwidgets/jqx-all.js");

	private static HeaderItem GLOBALIZE_JS = Resource.getCommonJS("jqwidgets/i18n/globalize.js");
	private static HeaderItem LOCALIZED_JS = Resource.getCommonJS("jqwidgets/i18n/localization.js");

	public static void setCSS(HeaderItem CSS) {
		JqWidgetsBehavior.CSS = CSS;
	}

	public static void setMainJs(HeaderItem mainJs) {
		JqWidgetsBehavior.MAIN_JS = mainJs;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(CSS);
		response.render(CHANGE_4_RTL);

		response.render(MAIN_JS);

		response.render(GLOBALIZE_JS);
		response.render(LOCALIZED_JS);
	}
}

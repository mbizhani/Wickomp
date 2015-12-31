package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

public class EasyUIBehavior extends Behavior {
	private static HeaderItem CSS = Resource.getCommonCSS("easyui/themes/default/easyui.css");
	private static HeaderItem JS = Resource.getCommonJS("easyui/jquery.easyui.min.js");

	public static void setCSS(HeaderItem CSS) {
		EasyUIBehavior.CSS = CSS;
	}

	public static void setJS(HeaderItem JS) {
		EasyUIBehavior.JS = JS;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(Resource.getJQueryReference());

		response.render(CSS);
		response.render(JS);
	}
}

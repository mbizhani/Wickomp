package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

public class FontAwesomeBehavior extends Behavior {
	private static HeaderItem CSS = Resource.getCommonCSS("fontawesome/css/font-awesome.min.css");

	public static void setCSS(HeaderItem CSS) {
		FontAwesomeBehavior.CSS = CSS;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(CSS);
	}
}

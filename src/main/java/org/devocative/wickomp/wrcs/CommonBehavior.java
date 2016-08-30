package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

public class CommonBehavior extends Behavior {
	private static final long serialVersionUID = 3591234663743513482L;

	private static HeaderItem CSS = Resource.getCommonCSS("form/common.css");

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(CSS);
	}
}

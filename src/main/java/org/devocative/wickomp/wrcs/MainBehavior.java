package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

public class MainBehavior extends EasyUIBehavior {
	private static final long serialVersionUID = 2448549737676497644L;

	private static HeaderItem MAIN_JS = Resource.getCommonJS("main/wickomp.js");


	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		response.render(MAIN_JS);
	}

}

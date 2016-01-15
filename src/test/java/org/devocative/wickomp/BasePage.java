package org.devocative.wickomp;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.html.WExternalLink;
import org.devocative.wickomp.html.menu.OMenuItem;
import org.devocative.wickomp.html.menu.WMenuBar;

import java.util.Arrays;

public abstract class BasePage extends WebPage {
	public BasePage() {
		add(new WMenuBar("menu",
				Arrays.asList(
					new OMenuItem("/", new Model<>("Home")),
					new OMenuItem(new Model<>("Edit"), Arrays.asList(
						new OMenuItem(new Model<>("Copy")),
						new OMenuItem(new Model<>("Paste"))
					))
				)
			)
		);
		add(new WExternalLink("label", new Model<>("AAA")));
		add(new WExternalLink("link", new Model<>("AAA"), "/test"));
	}
}

package org.devocative.wickomp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.html.WExternalLink;
import org.devocative.wickomp.html.WMessager;
import org.devocative.wickomp.html.menu.OMenuItem;
import org.devocative.wickomp.html.menu.WMenuBar;
import org.devocative.wickomp.html.window.WModalWindow;

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

		final WModalWindow window = new WModalWindow("modal") {
			@Override
			protected void onClose(AjaxRequestTarget target) {
				WMessager.show("Win", " Closed !@#$%^&*()_+}{[]'\";;::.,<>?//\\=+-", target);
			}
		};
		add(window);

		add(new AjaxLink("showModal") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				window.setContent(new Label(window.getContentId(), "<p>Hello Window</p><br/><br/><br/><br/>")
					.setEscapeModelStrings(false));
				window.show(target);
			}
		});
	}
}

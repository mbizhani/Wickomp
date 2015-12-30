package org.devocative.wickomp;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.devocative.wickomp.page.HomePage;


public class WicketApplication extends WebApplication {
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();
	}
}

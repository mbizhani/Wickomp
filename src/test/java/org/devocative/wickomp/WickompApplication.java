package org.devocative.wickomp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.devocative.wickomp.page.HomePage;


public class WickompApplication extends WebApplication {
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();

		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setCompressWhitespace(true);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

		getResourceSettings().getStringResourceLoaders().add(0, new BundleStringResourceLoader("org.devocative.wickomp.Test"));
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WickompWebSession(request);
	}
}

package org.devocative.wickomp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.devocative.wickomp.async.AsyncMediator;
import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;
import org.devocative.wickomp.page.HomePage;

import java.util.Timer;
import java.util.TimerTask;


public class WickompApplication extends WebApplication {
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();

		getMarkupSettings()
			.setStripWicketTags(true)
			.setStripComments(true)
			.setCompressWhitespace(true)
			.setDefaultMarkupEncoding("UTF-8");

		getResourceSettings().getStringResourceLoaders().add(0, new BundleStringResourceLoader("org.devocative.wickomp.Test"));

		AsyncMediator.init(this);

		Thread th = new Thread() {
			private int no = 0;

			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						no++;
						AsyncMediator.broadcast(no);
					}
				}, 5000, 2000);
			}
		};
		th.start();

		AsyncMediator.registerHandler("COUNTER", new IAsyncRequestHandler() {
			@Override
			public void onRequest(AsyncToken asyncToken, Object requestPayLoad) {
				WTimer.start(asyncToken, (Integer) requestPayLoad);
			}
		});
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WickompWebSession(request);
	}
}

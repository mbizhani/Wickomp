package org.devocative.wickomp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.devocative.wickomp.page.HomePage;
import org.devocative.wickomp.page.MountedPage;
import org.devocative.wickomp.service.DataService;
import org.devocative.wickomp.service.DbService;

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

		getRequestCycleListeners().add(new WickompRequestCycleListener());

		Thread th = new Thread() {
			private int no = 0;

			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						no++;
						WebUtil.wsBroadcast(WickompApplication.this, no);
					}
				}, 5000, 2000);
			}
		};
		th.start();

		/*AsyncMediator.registerHandler("NTP", (asyncToken, requestPayLoad) ->
				AsyncMediator.sendResponse(asyncToken, CalendarUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
		);

		AsyncMediator.registerHandler("COUNTER", (asyncToken, requestPayLoad) ->
				WTimer.start(asyncToken, (Integer) requestPayLoad)
		);*/

		mountPage("/mount", MountedPage.class);

		DbService.init();
		DataService.init();
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WickompWebSession(request);
	}

	@Override
	protected void onDestroy() {
		DbService.shutdown();
	}
}

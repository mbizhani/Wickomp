package org.devocative.wickomp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.devocative.adroit.ObjectBuilder;
import org.devocative.wickomp.async.AsyncMediator;
import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;
import org.devocative.wickomp.page.HomePage;
import org.devocative.wickomp.vo.PersonVO;

import java.io.Serializable;
import java.util.*;

public class WickompApplication extends WebApplication {

	private List<PersonVO> list;

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();

		list = PersonVO.list();

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

		AsyncMediator.registerHandler("GRID_PAGER", new IAsyncRequestHandler() {
			@Override
			public void onRequest(final AsyncToken token, Object requestPayLoad) {
				Map<String, Object> map = (Map<String, Object>) requestPayLoad;
				final long first = (long) map.get("first");
				long size = (long) map.get("size");

				int start = (int) ((first - 1) * size);
				int end = (int) (first * size);
				final List<PersonVO> result = list.subList(start, Math.min(end, list.size()));

				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						AsyncMediator.sendResponse(token, (Serializable) ObjectBuilder
								.createMap(new HashMap<String, Object>())
								.put("list", result)
								.put("count", list.size())
								.get()
						);

					}
				}.start();
			}
		});
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WickompWebSession(request);
	}
}

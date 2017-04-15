package org.devocative.wickomp;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.devocative.adroit.CalendarUtil;
import org.devocative.adroit.obuilder.ObjectBuilder;
import org.devocative.wickomp.async.AsyncMediator;
import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;
import org.devocative.wickomp.page.HomePage;
import org.devocative.wickomp.page.MountedPage;
import org.devocative.wickomp.vo.EmployeeVO;
import org.devocative.wickomp.vo.PersonVO;

import java.io.Serializable;
import java.util.*;

public class WickompApplication extends WebApplication {

	private List<PersonVO> personVOList;
	private List<EmployeeVO> employeeVOList;

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();

		personVOList = PersonVO.list();
		employeeVOList = EmployeeVO.list();

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

		AsyncMediator.registerHandler("NTP", new IAsyncRequestHandler() {
			@Override
			public void onRequest(AsyncToken asyncToken, Object requestPayLoad) {
				AsyncMediator.sendResponse(asyncToken, CalendarUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		});

		AsyncMediator.registerHandler("COUNTER", new IAsyncRequestHandler() {
			@Override
			public void onRequest(AsyncToken asyncToken, Object requestPayLoad) {
				WTimer.start(asyncToken, (Integer) requestPayLoad);
			}
		});

		AsyncMediator.registerHandler("T_GRID_PAGER", new IAsyncRequestHandler() {
			@Override
			public void onRequest(final AsyncToken token, Object requestPayLoad) {
				Map<String, Object> map = (Map<String, Object>) requestPayLoad;
				final long first = (long) map.get("first");
				long size = (long) map.get("size");

				int start = (int) ((first - 1) * size);
				int end = (int) (first * size);
				final List<EmployeeVO> result = employeeVOList.subList(start, Math.min(end, employeeVOList.size()));

				new Thread() {
					@Override
					public void run() {
						/*try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}*/
						AsyncMediator.sendResponse(token, (Serializable) ObjectBuilder
								.<String, Object>createDefaultMap()
								.put("list", result)
								.put("count", employeeVOList.size())
								.get()
						);
					}
				}.start();
			}
		});

		AsyncMediator.registerHandler("T_GRID_CHILDREN", new IAsyncRequestHandler() {
			@Override
			public void onRequest(final AsyncToken asyncToken, Object requestPayLoad) {
				final String parentId = (String) requestPayLoad;

				boolean hasChildren = true;
				try {
					hasChildren = Integer.parseInt(parentId) % 2 == 0;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				final List<EmployeeVO> result = new ArrayList<>();
				if (hasChildren) {
					for (int i = 1; i < 6; i++) {
						EmployeeVO emp = new EmployeeVO();
						emp.setEid(parentId + "." + i);
						emp.setName("E" + parentId + "." + i);
						emp.setAge((int) (Math.random() * 50));
						emp.setParentId(parentId);
						result.add(emp);
					}
				}

				new Thread() {
					@Override
					public void run() {
						/*try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}*/

						AsyncMediator.sendResponse(asyncToken, (Serializable) ObjectBuilder
							.<String, Object>createDefaultMap()
							.put("parentId", parentId)
							.put("list", result)
							.get());
					}
				}.start();
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
				final List<PersonVO> result = personVOList.subList(start, Math.min(end, personVOList.size()));

				long sum = 0;
				for (PersonVO personVO : result) {
					sum += personVO.getIncome();
				}

				final List footer = new ArrayList<>();
				PersonVO agg = new PersonVO();
				agg.setCol01("Sum");
				agg.setIncome(sum);
				footer.add(agg);

				new Thread() {
					@Override
					public void run() {
						/*try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}*/

						if (first == 3) {
							AsyncMediator.sendError(token, new RuntimeException("AsyncMediator.sendError :)"));
						} else {
							AsyncMediator.sendResponse(token, (Serializable) ObjectBuilder
									.<String, Object>createDefaultMap()
									.put("list", result)
									.put("count", personVOList.size())
									.put("footer", footer)
									.get()
							);
						}

					}
				}.start();
			}
		});

		mountPage("/mount", MountedPage.class);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WickompWebSession(request);
	}
}

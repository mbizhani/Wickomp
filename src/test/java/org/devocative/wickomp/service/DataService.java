package org.devocative.wickomp.service;

import org.devocative.adroit.obuilder.ObjectBuilder;
import org.devocative.wickomp.ITaskCallback;
import org.devocative.wickomp.vo.EmployeeVO;
import org.devocative.wickomp.vo.PersonVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataService {
	private static BlockingQueue<RequestVO> QUEUE;
	private static List<PersonVO> personVOList;
	private static List<EmployeeVO> employeeVOList;

	public static void init() {
		QUEUE = new ArrayBlockingQueue<>(5);
		personVOList = PersonVO.list();
		employeeVOList = EmployeeVO.list();

		new Thread(new ThreadGroup("DataService"), "DataService") {
			@Override
			public void run() {
				try {
					while (true) {
						RequestVO requestVO = QUEUE.take();

						Map<String, Object> map = requestVO.map;

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

						Map<String, Object> resultMap = ObjectBuilder
							.<String, Object>createDefaultMap()
							.put("list", result)
							.put("count", personVOList.size())
							.put("footer", footer)
							.get();

						requestVO.callback.onTaskResult(resultMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void processPerson(RequestVO vo) {
		QUEUE.offer(vo);
	}

	public static void processEmployee(RequestVO vo) {
		Map<String, Object> map = vo.map;

		final long first = (long) map.get("first");
		long size = (long) map.get("size");

		int start = (int) ((first - 1) * size);
		int end = (int) (first * size);
		List<EmployeeVO> result = employeeVOList.subList(start, Math.min(end, employeeVOList.size()));

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Map<String, Object> resp = ObjectBuilder
					.<String, Object>createDefaultMap()
					.put("list", result)
					.put("count", employeeVOList.size())
					.get();
				vo.callback.onTaskResult(resp);
			}
		}.start();
	}

	public static void processSubEmployee(RequestVO vo) {
		String parentId = vo.parentId;

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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Map<String, Object> resp = ObjectBuilder
					.<String, Object>createDefaultMap()
					.put("parentId", parentId)
					.put("list", result)
					.get();
				vo.callback.onTaskResult(resp);
			}
		}.start();
	}

	public static class RequestVO {
		public ITaskCallback callback;
		public Map<String, Object> map;
		public String parentId;

		public RequestVO(ITaskCallback callback, Map<String, Object> map) {
			this.callback = callback;
			this.map = map;
		}

		public RequestVO(ITaskCallback callback, String parentId) {
			this.callback = callback;
			this.parentId = parentId;
		}
	}
}

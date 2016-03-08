package org.devocative.wickomp;

import org.devocative.wickomp.async.AsyncMediator;
import org.devocative.wickomp.async.AsyncToken;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class WTimer extends TimerTask {
	private static final Map<AsyncToken, WTimer> MAP = new ConcurrentHashMap<>();

	public synchronized static void start(AsyncToken token, int no) {
		if (MAP.containsKey(token)) {
			MAP.get(token).reset(no);
		} else {
			WTimer timer = new WTimer(token, no);
			MAP.put(token, timer);
		}
		System.out.println("*** MAP SIZE: " + MAP.size());
	}

	private int no = 1;
	private AsyncToken token;

	private WTimer(AsyncToken token, int no) {
		this.token = token;
		this.no = no;

		new Timer().schedule(this, 1000, 2000);
	}

	@Override
	public void run() {
		System.out.println("WTimer.run: " + token);
		no++;

		AsyncMediator.sendResponse(token, no);
	}

	public void reset(int no) {
		this.no = no;
	}
}

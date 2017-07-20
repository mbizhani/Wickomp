package org.devocative.wickomp.async.processor;

import org.apache.wicket.Application;
import org.devocative.wickomp.async.AsyncToken;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueueResponseProcessor extends AResponseProcessor implements Runnable {
	private BlockingQueue<ObjectToQueue> queue;
	private AtomicBoolean running;

	// ------------------------------

	public QueueResponseProcessor(Application application) {
		super(application);

		queue = new LinkedBlockingQueue<>();
		running = new AtomicBoolean(true);

		Thread thread = new Thread(Thread.currentThread().getThreadGroup(), this);
		thread.setName("Wick-Async-Queue-Response-Processor");
		thread.start();

		logger.info("QueueResponseProcessor Started");
	}

	// ------------------------------

	@Override
	public void processResponse(AsyncToken asyncToken, Object responsePayLoad, Exception error) {
		if (running.get()) {
			queue.offer(new ObjectToQueue(asyncToken, responsePayLoad, error));
		} else {
			logger.error("QueueResponseProcessor.processResponse: Application is shutting down");
		}
	}

	@Override
	public void shutdown() {
		logger.info("QueueResponseProcessor.shutdown()");

		running.set(false);

		if (queue.isEmpty()) {
			queue.offer(new ObjectToQueue());
		}
	}

	// ---------------

	@Override
	public void run() {
		while (true) {
			try {
				ObjectToQueue obj = queue.take();
				if (running.get()) {
					sendResponseByWS(obj.getAsyncToken(), obj.getPayLoad(), obj.getError());
				} else {
					logger.info("QueueResponseProcessor is shutting down");
					break;
				}
			} catch (InterruptedException e) {
				logger.error("QueueResponseProcessor: queue.take()", e);
			}
		}

		logger.info("QueueResponseProcessor shut down!");
	}
}

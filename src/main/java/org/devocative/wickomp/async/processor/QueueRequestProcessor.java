package org.devocative.wickomp.async.processor;

import org.devocative.wickomp.async.AsyncToken;
import org.devocative.wickomp.async.IAsyncRequestHandler;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueueRequestProcessor extends ARequestProcessor implements Runnable {
	private BlockingQueue<ObjectToQueue> queue;
	private AtomicBoolean running;

	// ------------------------------

	public QueueRequestProcessor(Map<String, IAsyncRequestHandler> handlersMap) {
		super(handlersMap);

		queue = new LinkedBlockingQueue<>();
		running = new AtomicBoolean(true);

		Thread thread = new Thread(Thread.currentThread().getThreadGroup(), this);
		thread.setName("Wick-Async-Queue-Request-Processor");
		thread.start();

		logger.info("QueueRequestProcessor Started");
	}

	// ------------------------------

	@Override
	public void processRequest(AsyncToken asyncToken, Object requestPayLoad) {
		if (running.get()) {
			queue.offer(new ObjectToQueue(asyncToken, requestPayLoad));
		} else {
			//TODO throw exception
			logger.error("QueueRequestProcessor.processRequest: Application is shutting down");
		}
	}

	@Override
	public void shutdown() {
		logger.info("QueueRequestProcessor.shutdown()");

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
					sendRequest(obj.getAsyncToken(), obj.getPayLoad());
				} else {
					logger.info("QueueRequestProcessor is shutting down");
					break;
				}
			} catch (InterruptedException e) {
				logger.error("QueueRequestProcessor: queue.take()", e);
			}
		}

		logger.info("QueueRequestProcessor shut down!");
	}
}

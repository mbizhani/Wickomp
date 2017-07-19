package org.devocative.wickomp.async.processor;

import org.apache.wicket.Application;
import org.devocative.wickomp.async.AsyncToken;

import java.io.Serializable;

public class SimpleResponseProcessor extends AResponseProcessor {
	public SimpleResponseProcessor(Application application) {
		super(application);
	}

	@Override
	public void processResponse(AsyncToken asyncToken, Serializable responsePayLoad, Exception error) {
		sendResponseByWS(asyncToken, responsePayLoad, error);
	}
}

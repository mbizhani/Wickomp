package org.devocative.wickomp.async;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

import java.io.Serializable;

public interface IAsyncResponseHandler {
	void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result);
}

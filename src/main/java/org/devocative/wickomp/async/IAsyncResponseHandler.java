package org.devocative.wickomp.async;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

public interface IAsyncResponseHandler {
	void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Object result);

	void onAsyncError(String handlerId, IPartialPageRequestHandler handler, Exception error);
}

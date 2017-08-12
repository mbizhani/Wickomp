package org.devocative.wickomp.async;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

public interface IAsyncResponse {
	void onAsyncResult(IPartialPageRequestHandler handler, Object result);

	void onAsyncError(IPartialPageRequestHandler handler, Exception e);
}

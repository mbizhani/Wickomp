package org.devocative.wickomp.async;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

public interface IAsyncResponse<T> {
	void onAsyncResult(IPartialPageRequestHandler handler, T result);

	void onAsyncError(IPartialPageRequestHandler handler, Exception e);
}

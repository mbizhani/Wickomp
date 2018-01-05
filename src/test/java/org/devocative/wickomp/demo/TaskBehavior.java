package org.devocative.wickomp.demo;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.async.IAsyncResponse;
import org.devocative.wickomp.async.WebSocketDelayedResponse;
import org.devocative.wickomp.async.WebSocketToken;

public class TaskBehavior extends Behavior implements ITaskCallback {
	private static final long serialVersionUID = -4981513449187852486L;

	private WebSocketToken token;
	private IAsyncResponse response;

	public TaskBehavior(IAsyncResponse response) {
		this.response = response;
	}

	@Override
	public void beforeRender(Component component) {
		if (token == null) {
			token = WebUtil.createWSToken(component);
		}
	}

	@Override
	public void onTaskResult(Object result) {
		WebUtil.wsPush(token, new WebSocketDelayedResponse(response, result));
	}
}

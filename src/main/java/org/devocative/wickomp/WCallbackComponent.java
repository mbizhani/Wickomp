package org.devocative.wickomp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.devocative.wickomp.opt.OComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WCallbackComponent extends WComponent {
	private static final Logger logger = LoggerFactory.getLogger(WCallbackComponent.class);

	private AbstractAjaxBehavior callbackAjaxBehavior;
	private boolean isAutoJSRender = true;

	protected WCallbackComponent(String id, OComponent options) {
		super(id, options);

		callbackAjaxBehavior = new AbstractAjaxBehavior() {
			@Override
			public void onRequest() {
				RequestCycle requestCycle = RequestCycle.get();
				WCallbackComponent.this.onRequest(requestCycle.getRequest().getRequestParameters());
			}
		};

		add(callbackAjaxBehavior);
	}

	protected abstract void onRequest(IRequestParameters parameters);

	protected String getCallbackURL() {
		return callbackAjaxBehavior.getCallbackUrl().toString();
	}

	protected void setIsAutoJSRender(boolean isAutoJSRender) {
		this.isAutoJSRender = isAutoJSRender;
	}

	protected void sendJSONResponse(String json) {
		logger.debug("JSON Response: {}", json);
		RequestCycle requestCycle = RequestCycle.get();
		requestCycle.replaceAllRequestHandlers(new TextRequestHandler("text/json", "UTF-8", json));
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isAutoJSRender) {
			String script = getJQueryCall();
			AjaxRequestTarget ajaxRequestTarget = getRequestCycle().find(AjaxRequestTarget.class);
			if (ajaxRequestTarget == null) {
				getResponse().write(String.format("\n<script>\n$(function(){%s});\n</script>\n", script));
			} else {
				ajaxRequestTarget.appendJavaScript(script);
			}
		}
	}
}

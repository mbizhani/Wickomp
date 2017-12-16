package org.devocative.wickomp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.OComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WJqCallbackComponent extends WJqComponent {
	private static final long serialVersionUID = -1595120467139571552L;

	private static final Logger logger = LoggerFactory.getLogger(WJqCallbackComponent.class);

	private AbstractAjaxBehavior callbackAjaxBehavior;
	private boolean isAutoJSRender = true;

	// ------------------------------

	protected WJqCallbackComponent(String id, OComponent options) {
		super(id, options);

		callbackAjaxBehavior = new AbstractAjaxBehavior() {
			private static final long serialVersionUID = 1339255377058391445L;

			@Override
			public void onRequest() {
				WJqCallbackComponent.this.onRequest();
			}
		};

		add(callbackAjaxBehavior);
	}

	// ------------------------------

	protected abstract void onRequest();

	// ------------------------------

	protected String getCallbackURL() {
		return callbackAjaxBehavior.getCallbackUrl().toString();
	}

	protected void setIsAutoJSRender(boolean isAutoJSRender) {
		this.isAutoJSRender = isAutoJSRender;
	}

	// ------------------------------

	protected void sendJSONResponse(String json) {
		logger.debug("JSON Response: {}", json);
		RequestCycle requestCycle = RequestCycle.get();
		requestCycle.scheduleRequestHandlerAfterCurrent(new TextRequestHandler("text/json", "UTF-8", json));
	}

	protected void sendEmptyResponse() {
		logger.debug("Empty Response");
		RequestCycle requestCycle = RequestCycle.get();
		requestCycle.scheduleRequestHandlerAfterCurrent(new TextRequestHandler("<ajax-response/>"));
	}

	protected AjaxRequestTarget createAjaxResponse() {
		WebApplication app = (WebApplication) getApplication();
		AjaxRequestTarget target = app.newAjaxRequestTarget(getPage());
		RequestCycle requestCycle = RequestCycle.get();
		requestCycle.scheduleRequestHandlerAfterCurrent(target);

		return target;
	}

	// ------------------------------

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		if (options instanceof ICallbackUrl) {
			((ICallbackUrl) options).setUrl(getCallbackURL());
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isAutoJSRender && isVisible()) {
			WebUtil.writeJQueryCall(getJQueryCall(), true);
		}
	}
}

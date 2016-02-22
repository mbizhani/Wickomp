package org.devocative.wickomp;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.resource.CoreLibrariesContributor;
import org.devocative.wickomp.opt.OUserPreference;
import org.devocative.wickomp.opt.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WComponent extends WebComponent {
	private static final Logger logger = LoggerFactory.getLogger(WComponent.class);

	private Options options;

	protected WComponent(String id, Options options) {
		super(id);
		this.options = options;

		setOutputMarkupId(true);
		setVersioned(false);
	}

	protected abstract String getJQueryFunction();

	protected String getJQueryCall() {
		String opt = JsonUtil.toJson(options);
		String format = String.format("$(\"#%s\").%s(%s);", getMarkupId(), getJQueryFunction(), opt);
		logger.debug("JQueryCall: {}", format);
		return format;
	}

	protected final OUserPreference getUserPreference() {
		WebSession session = WebSession.get();
		if (session instanceof OUserPreference) {
			return (OUserPreference) session;
		}
		return OUserPreference.DEFAULT;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (options == null) {
			throw new WicketRuntimeException("Options not defined!");
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(
			WebApplication.get().getJavaScriptLibrarySettings().getJQueryReference()
		));

		CoreLibrariesContributor.contributeAjax(getApplication(), response);
	}
}

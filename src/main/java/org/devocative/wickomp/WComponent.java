package org.devocative.wickomp;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.CoreLibrariesContributor;
import org.devocative.adroit.JsonUtil;
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

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (options == null)
			throw new RuntimeException("Options not defined!");
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(
				WebApplication.get().getJavaScriptLibrarySettings().getJQueryReference()
		));

		/*String js = String.format("%s/js/jqwidgets350/jqx-all.js", getRequest().getContextPath());
		response.render(JavaScriptHeaderItem.forUrl(js));

		String css = String.format("%s/js/jqwidgets350/styles/jqx.base.css", getRequest().getContextPath());
		response.render(CssHeaderItem.forUrl(css));*/

		CoreLibrariesContributor.contributeAjax(getApplication(), response);
	}
}

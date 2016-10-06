package org.devocative.wickomp.wrcs;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.JavaScriptLibrarySettings;

public abstract class Resource {
	public static final String WICKOMP_DEBUG_ENABLED_JS = "WickompDebugEnabled";

	public static HeaderItem getCommonJS(String path) {
		ResourceReference reference = new JavaScriptResourceReference(Resource.class, path);
		return JavaScriptHeaderItem.forReference(reference);
	}

	public static HeaderItem getCommonCSS(String path) {
		ResourceReference reference = new CssResourceReference(Resource.class, path);
		return CssHeaderItem.forReference(reference);
	}

	public static void addJQueryReference(IHeaderResponse response) {
		JavaScriptLibrarySettings settings = WebApplication.get().getJavaScriptLibrarySettings();

		response.render(JavaScriptHeaderItem.forReference(settings.getJQueryReference()));
		response.render(JavaScriptHeaderItem.forReference(settings.getWicketEventReference()));
		response.render(JavaScriptHeaderItem.forReference(settings.getWicketAjaxReference()));
		if (WebApplication.get().getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
			response.render(JavaScriptHeaderItem.forReference(settings.getWicketAjaxDebugReference()));
		}
		response.render(JavaScriptHeaderItem.forScript(String.format("var %s = false;", WICKOMP_DEBUG_ENABLED_JS), WICKOMP_DEBUG_ENABLED_JS));
	}
}

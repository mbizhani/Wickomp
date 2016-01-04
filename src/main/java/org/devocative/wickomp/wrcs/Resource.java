package org.devocative.wickomp.wrcs;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

public abstract class Resource {
	public static HeaderItem getCommonJS(String path) {
		ResourceReference reference = new JavaScriptResourceReference(Resource.class, path);
		return JavaScriptHeaderItem.forReference(reference);
	}

	public static HeaderItem getCommonCSS(String path) {
		ResourceReference reference = new CssResourceReference(Resource.class, path);
		return CssHeaderItem.forReference(reference);
	}

	public static void addJQueryReference(IHeaderResponse response) {
		IJavaScriptLibrarySettings settings = WebApplication.get().getJavaScriptLibrarySettings();

		response.render(JavaScriptHeaderItem.forReference(settings.getJQueryReference()));
		response.render(JavaScriptHeaderItem.forReference(settings.getWicketEventReference()));
		response.render(JavaScriptHeaderItem.forReference(settings.getWicketAjaxReference()));
		if (WebApplication.get().getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
			response.render(JavaScriptHeaderItem.forReference(settings.getWicketAjaxDebugReference()));
		}
	}

	public static ResourceReference getImageResourceReference(String path) {
		return new PackageResourceReference(Resource.class, path);
	}
}

package org.devocative.wickomp.resource;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public abstract class Resource {
	public static HeaderItem getCommonJS(String path) {
		ResourceReference reference = new JavaScriptResourceReference(Resource.class, path);
		return JavaScriptHeaderItem.forReference(reference);
	}

	public static HeaderItem getCommonCSS(String path) {
		ResourceReference reference = new CssResourceReference(Resource.class, path);
		return CssHeaderItem.forReference(reference);
	}

	public static HeaderItem getJQueryReference() {
		return JavaScriptHeaderItem.forReference(WebApplication.get()
			.getJavaScriptLibrarySettings().getJQueryReference());
	}

	public static ResourceReference getImageResourceReference(String path) {
		return new PackageResourceReference(Resource.class, path);
	}
}

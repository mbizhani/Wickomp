package org.devocative.wickomp.html;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.cycle.RequestCycle;

public class WExternalLink extends WebComponent {
	private IModel<String> label;
	private String href;

	private boolean contextRelative = true;

	public WExternalLink(String id, IModel<String> label) {
		this(id, label, null);
	}

	// Main Constructor
	public WExternalLink(String id, IModel<String> label, String href) {
		super(id);

		this.label = label;
		this.href = href;
	}

	public WExternalLink setContextRelative(boolean contextRelative) {
		this.contextRelative = contextRelative;
		return this;
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		if (href != null) {
			tag.setName("a");

			if (contextRelative) {
				if(isAbsolute()) {
					throw new RuntimeException("Relative link starts with protocol: " + href);
				}

				if (href.length() > 0 && href.charAt(0) == '/') {
					href = href.substring(1);
				}
				tag.put("href", UrlUtils.rewriteToContextRelative(href, RequestCycle.get()));
			} else {
				tag.put("href", href);
			}
		}
	}

	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		replaceComponentTagBody(markupStream, openTag, label.getObject());
	}

	private boolean isAbsolute() {
		return href.matches("^\\w+?[:][/][/].*");
	}
}

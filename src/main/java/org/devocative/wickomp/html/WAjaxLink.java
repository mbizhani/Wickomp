package org.devocative.wickomp.html;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;

public abstract class WAjaxLink extends AjaxLink {
	private IModel<String> caption;
	private HTMLBase icon;

	public WAjaxLink(String id) {
		this(id, null, null);
	}

	public WAjaxLink(String id, IModel<String> caption) {
		this(id, caption, null);
	}

	// Main Constructor
	public WAjaxLink(String id, IModel<String> caption, HTMLBase icon) {
		super(id);
		this.caption = caption;
		this.icon = icon;
	}

	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		if ("button".equalsIgnoreCase(openTag.getName()) && (caption != null || icon != null)) {
			String cap = "";
			if (caption != null) {
				cap = caption.getObject();
			}
			if (icon != null) {
				cap += " " + icon.toString();
			}
			replaceComponentTagBody(markupStream, openTag, cap);
		} else {
			super.onComponentTagBody(markupStream, openTag);
		}
	}
}

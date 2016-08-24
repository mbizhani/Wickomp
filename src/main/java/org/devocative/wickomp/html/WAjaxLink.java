package org.devocative.wickomp.html;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;

public abstract class WAjaxLink extends AjaxLink {
	private static final long serialVersionUID = 4596376143569260289L;

	private IModel<String> caption;
	private HTMLBase icon;
	private IModel<String> confirmationMessage;

	// ------------------------------

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

	// ------------------------------

	public WAjaxLink setConfirmationMessage(IModel<String> confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
		return this;
	}

	// ------------------------------

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

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		if (confirmationMessage != null) {
			AjaxCallListener myAjaxCallListener = new AjaxCallListener() {
				private static final long serialVersionUID = 1915387331347087734L;

				@Override
				public CharSequence getPrecondition(Component component) {
					return String.format("if(!confirm('%s')) return false;", confirmationMessage.getObject());
				}
			};
			attributes.getAjaxCallListeners().add(myAjaxCallListener);
		}
	}
}

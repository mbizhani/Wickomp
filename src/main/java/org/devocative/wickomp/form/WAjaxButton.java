package org.devocative.wickomp.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.html.WMessager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WAjaxButton extends Button {

	private IModel<String> confirmationMessage;
	private IModel<String> caption;
	private HTMLBase icon;

	public WAjaxButton(String id) {
		this(id, null, null);
	}

	public WAjaxButton(String id, IModel<String> caption) {
		this(id, caption, null);
	}

	// Main Constructor
	public WAjaxButton(String id, IModel<String> caption, HTMLBase icon) {
		super(id);
		this.caption = caption;
		this.icon = icon;
	}

	public WAjaxButton setConfirmationMessage(IModel<String> confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
		return this;
	}

	public WAjaxButton setCaption(IModel<String> caption) {
		this.caption = caption;
		return this;
	}

	public WAjaxButton setIcon(HTMLBase icon) {
		this.icon = icon;
		return this;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(newAjaxFormSubmitBehavior("click"));
	}

	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);

		tag.put("type", "submit");
		if (caption != null && "input".equalsIgnoreCase(tag.getName())) {
			tag.put("value", caption.getObject());
		}
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

	protected AjaxFormSubmitBehavior newAjaxFormSubmitBehavior(String event) {
		return new AjaxFormSubmitBehavior(event) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				try {
					WAjaxButton.this.onSubmit(target);
				} catch (Exception e) {
					WAjaxButton.this.onException(target, e);
				}
			}

			@Override
			protected void onAfterSubmit(AjaxRequestTarget target) {
				WAjaxButton.this.onAfterSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				WAjaxButton.this.onError(target, WMessager.collectMessages(WAjaxButton.this));
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);

				if (confirmationMessage != null) {
					AjaxCallListener myAjaxCallListener = new AjaxCallListener() {
						@Override
						public CharSequence getPrecondition(Component component) {
							return String.format("if(!confirm('%s')) return false;", confirmationMessage.getObject());
						}
					};
					attributes.getAjaxCallListeners().add(myAjaxCallListener);
				}

				WAjaxButton.this.updateAjaxAttributes(attributes);
			}
		};
	}

	protected void onSubmit(AjaxRequestTarget target) {
	}

	protected void onAfterSubmit(AjaxRequestTarget target) {
	}

	protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
	}

	protected void onException(AjaxRequestTarget target, Exception e) {
		if (e.getMessage() != null) {
			List<Serializable> error = new ArrayList<>();
			error.add(getString(e.getMessage(), null, e.getMessage()));
			onError(target, error);
		}
	}

	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	}
}

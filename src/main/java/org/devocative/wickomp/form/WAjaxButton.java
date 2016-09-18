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
import org.devocative.wickomp.IExceptionToMessageHandler;
import org.devocative.wickomp.WDefaults;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.html.HTMLBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class WAjaxButton extends Button {
	private static final long serialVersionUID = -7669995504729974927L;

	private static final Logger logger = LoggerFactory.getLogger(WAjaxButton.class);

	private HTMLBase icon;
	private IModel<String> confirmationMessage;
	private IExceptionToMessageHandler exceptionToMessageHandler = WDefaults.getExceptionToMessageHandler();

	// ---------------------- CONSTRUCTORS

	public WAjaxButton(String id) {
		this(id, null, null);
	}

	public WAjaxButton(String id, IModel<String> caption) {
		this(id, caption, null);
	}

	// Main Constructor
	public WAjaxButton(String id, IModel<String> caption, HTMLBase icon) {
		super(id, caption);
		this.icon = icon;
	}

	// ---------------------- ACCESSORS

	public WAjaxButton setConfirmationMessage(IModel<String> confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
		return this;
	}

	public WAjaxButton setCaption(IModel<String> caption) {
		setModel(caption);
		return this;
	}

	public WAjaxButton setIcon(HTMLBase icon) {
		this.icon = icon;
		return this;
	}

	public WAjaxButton setExceptionToMessageHandler(IExceptionToMessageHandler exceptionToMessageHandler) {
		this.exceptionToMessageHandler = exceptionToMessageHandler;
		return this;
	}

	// ---------------------- PUBLIC METHODS

	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		if ("button".equalsIgnoreCase(openTag.getName()) && (getModel() != null || icon != null)) {
			String cap = "";
			if (getModel() != null) {
				cap = getModelObject();
			}
			if (icon != null) {
				cap += " " + icon.toString();
			}
			replaceComponentTagBody(markupStream, openTag, cap);
		} else {
			super.onComponentTagBody(markupStream, openTag);
		}
	}

	// ---------------------- ABSTRACT METHODS

	protected abstract void onSubmit(AjaxRequestTarget target);

	// ---------------------- PROTECTED METHODS

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(newAjaxFormSubmitBehavior("click"));
	}

	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);

		tag.put("type", "submit");
		if (getModel() != null && "input".equalsIgnoreCase(tag.getName())) {
			tag.put("value", getModelObject());
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
					logger.warn("WAjaxButton.onSubmit: id=" + getId(), e);

					WAjaxButton.this.onException(target, e);
				}
			}

			@Override
			protected void onAfterSubmit(AjaxRequestTarget target) {
				WAjaxButton.this.onAfterSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				WAjaxButton.this.onError(target, WebUtil.collectAs(WAjaxButton.this, true));
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);

				// do not allow normal form submit to happen
				attributes.setPreventDefault(true);

				if (confirmationMessage != null) {
					AjaxCallListener myAjaxCallListener = new AjaxCallListener() {
						private static final long serialVersionUID = 8302847859100880597L;

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

	protected void onAfterSubmit(AjaxRequestTarget target) {
	}

	protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
	}

	protected void onException(AjaxRequestTarget target, Exception e) {
		if (e.getMessage() != null) {
			List<Serializable> error = new ArrayList<>();
			error.add(exceptionToMessageHandler.handleMessage(this, e));
			onError(target, error);
		}
	}

	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	}
}

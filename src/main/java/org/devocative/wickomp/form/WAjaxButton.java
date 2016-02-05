package org.devocative.wickomp.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.devocative.wickomp.html.WMessager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WAjaxButton extends Button {

	private String confirmationMessage;

	public WAjaxButton(String id) {
		super(id);
	}

	public WAjaxButton setConfirmationMessage(String confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
		return this;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(newAjaxFormSubmitBehavior("click"));
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
							return String.format("if(!confirm('%s')) return false;", confirmationMessage);
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

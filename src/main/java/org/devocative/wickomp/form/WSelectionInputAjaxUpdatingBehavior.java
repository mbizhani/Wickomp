package org.devocative.wickomp.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public abstract class WSelectionInputAjaxUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {

	private static final ResourceReference CHOICE_JS = new JavaScriptResourceReference(
		AjaxFormChoiceComponentUpdatingBehavior.class, "AjaxFormChoiceComponentUpdatingBehavior.js");

	private static final long serialVersionUID = 1L;

	public WSelectionInputAjaxUpdatingBehavior() {
		super("click");
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		response.render(JavaScriptHeaderItem.forReference(CHOICE_JS));
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);

		attributes.getAjaxCallListeners().add(new AjaxCallListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence getPrecondition(Component component) {
				return String.format("return Wicket.Choice.acceptInput('%s', attrs)",
					getFormComponent().getInputName());
			}
		});

		attributes.getDynamicExtraParameters().add(
			String.format("return Wicket.Choice.getInputValues('%s', attrs)",
				getFormComponent().getInputName()));
	}

	/**
	 * @see org.apache.wicket.behavior.AbstractAjaxBehavior#onBind()
	 */
	@Override
	protected void onBind() {
		super.onBind();

		if (getComponent() instanceof RadioGroup || getComponent() instanceof CheckGroup) {
			getComponent().setRenderBodyOnly(false);
		}
	}

	@Override
	protected void checkComponent(FormComponent<?> component) {
	}
}

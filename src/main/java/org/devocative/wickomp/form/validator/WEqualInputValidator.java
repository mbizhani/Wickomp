package org.devocative.wickomp.form.validator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.util.lang.Objects;

public class WEqualInputValidator extends AbstractFormValidator {
	private static final long serialVersionUID = -7997370920112382420L;

	private final FormComponent<?>[] components;

	public WEqualInputValidator(FormComponent<?> formComponent1, FormComponent<?> formComponent2) {
		components = new FormComponent[]{formComponent1, formComponent2};
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return components;
	}

	@Override
	public void validate(Form<?> form) {
		final FormComponent<?> formComponent1 = components[0];
		final FormComponent<?> formComponent2 = components[1];

		if (!Objects.equal(formComponent1.getConvertedInput(), formComponent2.getConvertedInput())) {
			error(formComponent2);
		}
	}
}
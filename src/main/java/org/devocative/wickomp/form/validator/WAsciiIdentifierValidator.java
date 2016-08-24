package org.devocative.wickomp.form.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.regex.Pattern;

public class WAsciiIdentifierValidator implements IValidator<String> {
	private static final long serialVersionUID = 7561575639423885307L;

	private static final Pattern PATTERN = Pattern.compile("^[A-Za-z]+?[A-Za-z0-9]*?$");

	@Override
	public void validate(IValidatable<String> validatable) {
		String value = validatable.getValue();

		if (!PATTERN.matcher(value).matches()) {
			validatable.error(
				new ValidationError(this)
					.setMessage("Invalid ASCII identifier value")
			);
		}
	}
}

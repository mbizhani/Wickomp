package org.devocative.wickomp.form.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.regex.Pattern;

public class WPatternValidator implements IValidator<String> {
	private static final long serialVersionUID = -4722280690698540525L;

	private static final int DEFAULT_FLAG = 0;
	private Pattern pattern;
	private String altKey;
	private String customMessage;

	// ------------------------------

	public WPatternValidator(String pattern) {
		this(pattern, DEFAULT_FLAG, null);
	}

	public WPatternValidator(String pattern, String altKey) {
		this(pattern, DEFAULT_FLAG, altKey);
	}

	public WPatternValidator(String pattern, int flags) {
		this(pattern, flags, null);
	}

	// Main constructor
	public WPatternValidator(String pattern, int flags, String altKey) {
		this.pattern = Pattern.compile(pattern, flags);
		this.altKey = altKey;
	}

	// ------------------------------

	public WPatternValidator setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
		return this;
	}

	// ------------------------------

	@Override
	public void validate(IValidatable<String> validatable) {
		String value = validatable.getValue();

		if (!pattern.matcher(value).matches()) {
			ValidationError error = altKey == null ?
				new ValidationError(this).setVariable("pattern", pattern.pattern()) :
				new ValidationError().addKey(altKey);

			if (customMessage == null) {
				error.setMessage(String.format("Value [%s] dose not match pattern [%s] ", value, pattern.pattern()));
			} else {
				error.setMessage(customMessage);
			}

			validatable.error(error);
		}
	}
}

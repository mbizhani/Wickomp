package org.devocative.wickomp.form.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.regex.Pattern;

/*
 * Based on https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
 */
public class WPasswordStrengthValidator implements IValidator<String> {
	private static final long serialVersionUID = 5187782035983173417L;

	private boolean enabled = true;
	private boolean digit = true;
	private boolean lowerCase = true;
	private boolean upperCase = true;
	private boolean specialChar = true;
	private boolean noWhiteSpace = true;
	private int minLength = 7;
	private Integer maxLength;

	// ------------------------------

	public boolean isEnabled() {
		return enabled;
	}

	public WPasswordStrengthValidator setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean isDigit() {
		return digit;
	}

	public WPasswordStrengthValidator setDigit(boolean digit) {
		this.digit = digit;
		return this;
	}

	public boolean isLowerCase() {
		return lowerCase;
	}

	public WPasswordStrengthValidator setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
		return this;
	}

	public boolean isUpperCase() {
		return upperCase;
	}

	public WPasswordStrengthValidator setUpperCase(boolean upperCase) {
		this.upperCase = upperCase;
		return this;
	}

	public boolean isSpecialChar() {
		return specialChar;
	}

	public WPasswordStrengthValidator setSpecialChar(boolean specialChar) {
		this.specialChar = specialChar;
		return this;
	}

	public boolean isNoWhiteSpace() {
		return noWhiteSpace;
	}

	public WPasswordStrengthValidator setNoWhiteSpace(boolean noWhiteSpace) {
		this.noWhiteSpace = noWhiteSpace;
		return this;
	}

	public int getMinLength() {
		return minLength;
	}

	public WPasswordStrengthValidator setMinLength(int minLength) {
		this.minLength = minLength;
		return this;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public WPasswordStrengthValidator setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	// ------------------------------

	@Override
	public void validate(IValidatable<String> validatable) {
		if (!isEnabled()) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("^");

		if (isDigit()) {
			builder.append("(?=.*[0-9])");
		}

		if (isLowerCase()) {
			builder.append("(?=.*[a-z])");
		}

		if (isUpperCase()) {
			builder.append("(?=.*[A-Z])");
		}

		if (isSpecialChar()) {
			builder.append("(?=.*([^\\s][\\W_]))");
		}

		if (isNoWhiteSpace()) {
			builder.append("(?=\\S+$)");
		}

		if (getMaxLength() == null) {
			builder.append(String.format(".{%d,}", getMinLength()));
		} else {
			builder.append(String.format(".{%d,%d}", getMinLength(), getMaxLength()));
		}

		builder.append("$");

		Pattern PATTERN = Pattern.compile(builder.toString());

		String value = validatable.getValue();

		if (!PATTERN.matcher(value).matches()) {
			validatable.error(
				new ValidationError(this)
					.setMessage("Invalid/Weak Password")
			);
		}
	}
}

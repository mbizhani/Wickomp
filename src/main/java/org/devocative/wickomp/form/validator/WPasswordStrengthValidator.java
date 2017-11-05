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

	private boolean digit = true;
	private boolean lowerCase = true;
	private boolean upperCase = true;
	private boolean specialChar = true;
	private boolean noWhiteSpace = true;
	private int minLength = 7;
	private Integer maxLength;

	// ------------------------------

	public WPasswordStrengthValidator hasDigit(boolean digit) {
		this.digit = digit;
		return this;
	}

	public WPasswordStrengthValidator hasLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
		return this;
	}

	public WPasswordStrengthValidator hasUpperCase(boolean upperCase) {
		this.upperCase = upperCase;
		return this;
	}

	public WPasswordStrengthValidator hasSpecialChar(boolean specialChar) {
		this.specialChar = specialChar;
		return this;
	}

	public WPasswordStrengthValidator hasNoWhiteSpace(boolean noWhiteSpace) {
		this.noWhiteSpace = noWhiteSpace;
		return this;
	}

	public WPasswordStrengthValidator setMinLength(int minLength) {
		this.minLength = minLength;
		return this;
	}

	public WPasswordStrengthValidator setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	// ------------------------------

	@Override
	public void validate(IValidatable<String> validatable) {
		StringBuilder builder = new StringBuilder();
		builder.append("^");
		if (digit) {
			builder.append("(?=.*[0-9])");
		}
		if (lowerCase) {
			builder.append("(?=.*[a-z])");
		}
		if (upperCase) {
			builder.append("(?=.*[A-Z])");
		}
		if (specialChar) {
			builder.append("(?=.*[@#$%^&+=])");
		}
		if (noWhiteSpace) {
			builder.append("(?=\\S+$)");
		}
		if (maxLength == null) {
			builder.append(String.format(".{%d,}", minLength));
		} else {
			builder.append(String.format(".{%d,%d}", minLength, maxLength));
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

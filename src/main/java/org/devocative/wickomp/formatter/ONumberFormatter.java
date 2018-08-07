package org.devocative.wickomp.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ONumberFormatter implements OFormatter {
	private static final long serialVersionUID = 1089316680659774877L;

	private String pattern;

	private ONumberFormatter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String format(Object value) {
		if (pattern.endsWith(".*")) {
			final String newPat = pattern.replaceAll("[.][*]", ".0###################");
			final String[] result = new DecimalFormat(newPat).format(value).split("[.]");
			BigDecimal decimal = new BigDecimal(result[1]);
			if (!decimal.equals(BigDecimal.ZERO)) {
				return String.format("%s.%s", result[0], result[1]);
			}
			return result[0];
		}

		return new DecimalFormat(pattern).format(value);
	}

	private static final ONumberFormatter INTEGER = new ONumberFormatter("#,###");
	private static final ONumberFormatter REAL = new ONumberFormatter("#,###.##");
	private static final ONumberFormatter ALL_DECIMAL = new ONumberFormatter("#,###.*");

	public static ONumberFormatter integer() {
		return INTEGER;
	}

	public static ONumberFormatter real() {
		return REAL;
	}

	public static ONumberFormatter allDecimal() {
		return ALL_DECIMAL;
	}

	public static ONumberFormatter of(String pattern) {
		return new ONumberFormatter(pattern);
	}
}

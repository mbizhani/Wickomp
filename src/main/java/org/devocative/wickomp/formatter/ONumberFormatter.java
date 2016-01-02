package org.devocative.wickomp.formatter;

import java.text.DecimalFormat;

public class ONumberFormatter implements OFormatter {
	private String pattern;

	public ONumberFormatter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String format(Object value) {
		return new DecimalFormat(pattern).format(value);
	}

	private static final ONumberFormatter INTEGER = new ONumberFormatter("#,###");
	private static final ONumberFormatter REAL = new ONumberFormatter("#,###.##");

	public static ONumberFormatter integer() {
		return INTEGER;
	}

	public static ONumberFormatter real() {
		return REAL;
	}
}

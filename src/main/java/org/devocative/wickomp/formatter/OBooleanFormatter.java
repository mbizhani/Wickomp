package org.devocative.wickomp.formatter;

import org.apache.wicket.model.ResourceModel;

public class OBooleanFormatter implements OFormatter {
	private static String stTrue, stFalse;

	@Override
	public String format(Object value) {
		if (stTrue == null) {
			stTrue = new ResourceModel("label.true", "True").getObject();
			stFalse = new ResourceModel("label.false", "False").getObject();
		}

		if (value instanceof Number) {
			Number no = (Number) value;
			return fromNumber(no);
		}

		if (value instanceof Boolean) {
			Boolean b = (Boolean) value;
			return fromBoolean(b);
		}

		if (value instanceof String) {
			try {
				int intValue = Integer.parseInt(value.toString());
				return fromNumber(intValue);
			} catch (NumberFormatException ignored) {
			}

			return fromBoolean(Boolean.valueOf(value.toString()));
		}
		return null;
	}

	private String fromBoolean(Boolean b) {
		return b ? stTrue : stFalse;
	}

	private String fromNumber(Number no) {
		return no.intValue() == 0 ? stFalse : stTrue;
	}

	private static final OBooleanFormatter BOOLEAN = new OBooleanFormatter();

	public static OBooleanFormatter bool() {
		return BOOLEAN;
	}
}

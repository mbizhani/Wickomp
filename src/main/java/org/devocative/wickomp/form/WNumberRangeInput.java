package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.RangeVO;

public class WNumberRangeInput extends WBaseRangeInput<Number> {
	private static final long serialVersionUID = -3084374441053594155L;

	private Class<? extends Number> type;
	private Integer precision;
	private Character thousandSeparator;

	public WNumberRangeInput(String id, Class<? extends Number> type) {
		this(id, null, type);
	}

	public WNumberRangeInput(String id, IModel<RangeVO<Number>> model, Class<? extends Number> type) {
		super(id, model);
		this.type = type;
	}

	public WNumberRangeInput setPrecision(Integer precision) {
		this.precision = precision;
		return this;
	}

	public WNumberRangeInput setThousandSeparator(Character thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
		return this;
	}

	@Override
	protected FormComponent<Number> createFormComponent(String id, IModel<Number> model) {
		WNumberInput input = new WNumberInput(id, model, type);

		if (precision != null) {
			input.setPrecision(precision);
		}

		if (thousandSeparator != null) {
			input.setThousandSeparator(thousandSeparator);
		}

		return input;
	}
}

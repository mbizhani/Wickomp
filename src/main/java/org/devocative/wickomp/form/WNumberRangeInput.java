package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.RangeVO;

public class WNumberRangeInput extends WBaseRangeInput<Number> {
	private Class<? extends Number> type;
	private Integer precision;
	private String thousandSeparator;

	public WNumberRangeInput(String id, Class<? extends Number> type) {
		this(id, null, type);
	}

	public WNumberRangeInput(String id, IModel<RangeVO<Number>> model, Class<? extends Number> type) {
		super(id, model);
		this.type = type;
	}

	public WNumberRangeInput setPrecision(Integer precision) {
		return this;
	}

	public WNumberRangeInput setThousandSeparator(String thousandSeparator) {
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

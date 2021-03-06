package org.devocative.wickomp.form.range;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.devocative.adroit.vo.IRange;
import org.devocative.adroit.vo.RangeVO;
import org.devocative.wickomp.WLabeledFormInputPanel;

import java.io.Serializable;

public abstract class WBaseRangeInput<T extends Serializable> extends WLabeledFormInputPanel<IRange<T>> {
	private static final long serialVersionUID = 8954208501398056478L;

	protected static final String LOWER_ID = "lower";
	protected static final String UPPER_ID = "upper";

	private FormComponent<T> lower, upper;

	// ------------------------------

	protected WBaseRangeInput(String id, IModel<IRange<T>> model) {
		super(id, model);
	}

	// ------------------------------

	protected abstract FormComponent<T> createFormComponent(String id, IModel<T> model);

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		lower = createFormComponent(LOWER_ID, new Model<>());
		upper = createFormComponent(UPPER_ID, new Model<>());

		add(lower);
		add(upper);

		for (Behavior b : getBehaviors()) {
			if (!(b instanceof IValidator)) {
				lower.add(b);
				upper.add(b);
			}
		}
	}

	@Override
	protected void onBeforeRender() {
		IRange<T> rangeVO = getModelObject();

		if (rangeVO != null) {
			lower.setModelObject(rangeVO.getLower());
			upper.setModelObject(rangeVO.getUpper());
		} else {
			lower.setModelObject(null);
			upper.setModelObject(null);
		}

		super.onBeforeRender();
	}

	protected IRange<T> createRangeObject() {
		return new RangeVO<>();
	}

	// ------------------------------

	@Override
	public void convertInput() {
		T lowerValue = lower.getConvertedInput();
		T upperValue = upper.getConvertedInput();

		if (lowerValue != null || upperValue != null) {
			IRange<T> rangeObject = createRangeObject();
			rangeObject
				.setLower(lowerValue)
				.setUpper(upperValue);
			setConvertedInput(rangeObject);
		}
	}

}

package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.vo.RangeVO;
import org.devocative.wickomp.WFormInputPanel;

import java.io.Serializable;

public abstract class WBaseRangeInput<T extends Serializable> extends WFormInputPanel<RangeVO<T>> {
	private Label label;

	protected FormComponent<T> lower, upper;

	protected WBaseRangeInput(String id, IModel<RangeVO<T>> model) {
		super(id, model);

		add(label = new Label("label"));
	}

	public WBaseRangeInput<T> setLabelVisible(boolean visible) {
		label.setVisible(visible);
		return this;
	}

	protected abstract FormComponent<T> createFormComponent(String id, IModel<T> model);

	@Override
	protected void onInitialize() {
		super.onInitialize();

		IModel<String> labelModel = getLabel();
		if (labelModel != null) {
			label.setDefaultModel(labelModel);
		} else {
			label.setVisible(false);
		}


		lower = createFormComponent("lower", new Model<T>());
		upper = createFormComponent("upper", new Model<T>());

		add(lower);
		add(upper);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		RangeVO<T> rangeVO = getModelObject();
		if (rangeVO != null) {
			lower.setModelObject(rangeVO.getLower());
			upper.setModelObject(rangeVO.getUpper());
		} else {
			lower.setModelObject(null);
			upper.setModelObject(null);
		}
	}

	@Override
	public void convertInput() {
		T lowerValue = lower.getConvertedInput();
		T upperValue = upper.getConvertedInput();

		if (lowerValue != null || upperValue != null) {
			setConvertedInput(new RangeVO<>(lowerValue, upperValue));
		}
	}

}

package org.devocative.wickomp.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.vo.RangeVO;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.opt.OCalendar;

import java.util.Date;

public class WDateRangeInput extends WFormInputPanel<RangeVO<Date>> {
	private WDateInput lower, upper;

	public WDateRangeInput(String id) {
		this(id, null);
	}

	public WDateRangeInput(String id, IModel<RangeVO<Date>> model) {
		super(id, model);

		lower = new WDateInput("lower", new Model<Date>());
		upper = new WDateInput("upper", new Model<Date>());

		add(lower);
		add(upper);
	}

	// ---------------------- ACCESSORS

	public WDateRangeInput setCalendar(OCalendar calendar) {
		lower.setCalendar(calendar);
		upper.setCalendar(calendar);
		return this;
	}

	public WDateRangeInput setTimePartVisible(boolean visible) {
		lower.setTimePartVisible(visible);
		upper.setTimePartVisible(visible);
		return this;
	}

	// ----------------------- INTERNAL METHODS

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		RangeVO<Date> modelObject = getModelObject();
		if (modelObject != null) {
			lower.setModelObject(modelObject.getLower());
			upper.setModelObject(modelObject.getUpper());
		}
	}

	@Override
	protected void convertInput() {
		Date lowerDate = lower.getConvertedInput();
		Date upperDate = upper.getConvertedInput();

		if (lowerDate != null || upperDate != null) {
			setConvertedInput(new RangeVO<>(lowerDate, upperDate));
		}
	}
}

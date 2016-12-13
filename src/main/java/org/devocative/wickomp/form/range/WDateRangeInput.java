package org.devocative.wickomp.form.range;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.IRange;
import org.devocative.wickomp.form.WDateInput;
import org.devocative.wickomp.opt.OCalendar;

import java.util.Date;

public class WDateRangeInput extends WBaseRangeInput<Date> {
	private static final long serialVersionUID = 6659364241951361411L;

	private OCalendar calendar;
	private Boolean timePartVisible;

	// ------------------------------

	public WDateRangeInput(String id) {
		this(id, null);
	}

	public WDateRangeInput(String id, IModel<IRange<Date>> model) {
		super(id, model);
	}

	// ------------------------------ ACCESSORS

	public WDateRangeInput setCalendar(OCalendar calendar) {
		this.calendar = calendar;
		return this;
	}

	public WDateRangeInput setTimePartVisible(boolean timePartVisible) {
		this.timePartVisible = timePartVisible;
		return this;
	}

	// ------------------------------ INTERNAL METHODS

	@Override
	protected FormComponent<Date> createFormComponent(String id, IModel<Date> model) {
		WDateInput input = new WDateInput(id, model);

		if (calendar != null) {
			input.setCalendar(calendar);
		}

		if (timePartVisible != null) {
			input.setTimePartVisible(timePartVisible);
		}

		return input;
	}
}

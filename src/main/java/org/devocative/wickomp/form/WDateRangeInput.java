package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.RangeVO;
import org.devocative.wickomp.opt.OCalendar;

import java.util.Date;

public class WDateRangeInput extends WBaseRangeInput<Date> {
	public WDateRangeInput(String id) {
		this(id, null);
	}

	public WDateRangeInput(String id, IModel<RangeVO<Date>> model) {
		super(id, model);
	}

	// ---------------------- ACCESSORS

	public WDateRangeInput setCalendar(OCalendar calendar) {
		((WDateInput) lower).setCalendar(calendar);
		((WDateInput) upper).setCalendar(calendar);
		return this;
	}

	public WDateRangeInput setTimePartVisible(boolean visible) {
		((WDateInput) lower).setTimePartVisible(visible);
		((WDateInput) upper).setTimePartVisible(visible);
		return this;
	}

	// ----------------------- INTERNAL METHODS

	@Override
	protected FormComponent<Date> createFormComponent(String id, IModel<Date> model) {
		return new WDateInput(id, model);
	}
}

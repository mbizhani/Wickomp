package org.devocative.wickomp.form.range;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.date.EUniCalendar;
import org.devocative.adroit.date.TimeFieldVO;
import org.devocative.adroit.vo.IRange;
import org.devocative.wickomp.form.WDateInput;

import java.util.Date;
import java.util.TimeZone;

public class WDateRangeInput extends WBaseRangeInput<Date> {
	private static final long serialVersionUID = 6659364241951361411L;

	private EUniCalendar calendar;
	private TimeZone timeZone;
	private Boolean timePartVisible;

	private final TimeFieldVO defaultFromTime = new TimeFieldVO(0, 0, 0, 0);
	private final TimeFieldVO defaultToTime = new TimeFieldVO(0, 0, 0, 0);

	// ------------------------------

	public WDateRangeInput(String id) {
		this(id, null);
	}

	public WDateRangeInput(String id, IModel<IRange<Date>> model) {
		super(id, model);
	}

	// ------------------------------ ACCESSORS

	public WDateRangeInput setCalendar(EUniCalendar calendar) {
		this.calendar = calendar;
		return this;
	}

	public WDateRangeInput setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		return this;
	}

	public WDateRangeInput setTimePartVisible(boolean timePartVisible) {
		this.timePartVisible = timePartVisible;
		return this;
	}

	public WDateRangeInput setFromDefaultTime(int hour, int minute, int second, int millisecond) {
		this.defaultFromTime.setTime(hour, minute, second, millisecond);

		return this;
	}

	public WDateRangeInput setFromDefaultTime(TimeFieldVO other) {
		this.defaultFromTime.setTime(other);

		return this;
	}

	public WDateRangeInput setToDefaultTime(int hour, int minute, int second, int millisecond) {
		this.defaultToTime.setTime(hour, minute, second, millisecond);

		return this;
	}

	public WDateRangeInput setToDefaultTime(TimeFieldVO other) {
		this.defaultToTime.setTime(other);

		return this;
	}

	// ------------------------------ INTERNAL METHODS

	@Override
	protected FormComponent<Date> createFormComponent(String id, IModel<Date> model) {
		WDateInput input = new WDateInput(id, model)
			.setTimeZone(timeZone);

		if (LOWER_ID.equals(id)) {
			input.setDefaultTime(defaultFromTime.getHour(), defaultFromTime.getMinute(), defaultFromTime.getSecond(), defaultFromTime.getMillisecond());
		} else {
			input.setDefaultTime(defaultToTime.getHour(), defaultToTime.getMinute(), defaultToTime.getSecond(), defaultToTime.getMillisecond());
		}

		if (calendar != null) {
			input.setCalendar(calendar);
		}

		if (timePartVisible != null) {
			input.setTimePartVisible(timePartVisible);
		}

		return input;
	}
}

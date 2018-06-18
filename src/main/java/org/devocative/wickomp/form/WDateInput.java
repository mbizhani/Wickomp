package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.date.DateTimeFieldVO;
import org.devocative.adroit.date.EUniCalendar;
import org.devocative.adroit.date.TimeFieldVO;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.Date;
import java.util.TimeZone;

public class WDateInput extends WLabeledFormInputPanel<Date> {
	private static final long serialVersionUID = -2348912362832020132L;

	private static final HeaderItem NUMERIC_JS = Resource.getCommonJS("form/autoNumeric.js");
	private static final HeaderItem DATE_CALC_JS = Resource.getCommonJS("form/date/dtjalali.js");
	private static final HeaderItem DATE_POPUP_JS = Resource.getCommonJS("form/date/wDateInput.js");

	// ------------------------------

	private WebMarkupContainer mainTable;
	private WebMarkupContainer calOpener;
	private TextField<Integer> year, month, day, hour, minute, second;

	private EUniCalendar calendar;
	private TimeZone timeZone;
	private boolean timePartVisible = false;

	private TimeFieldVO defaultTime = new TimeFieldVO(0, 0, 0, 0);

	// ------------------------------

	public WDateInput(String id) {
		this(id, null, null);
	}

	public WDateInput(String id, IModel<Date> model) {
		this(id, model, null);
	}

	public WDateInput(String id, EUniCalendar calendar) {
		this(id, null, calendar);
	}

	// Main Constructor
	public WDateInput(String id, IModel<Date> model, EUniCalendar calendar) {
		super(id, model);
		this.calendar = calendar;
	}

	// ------------------------------

	public WDateInput setCalendar(EUniCalendar calendar) {
		this.calendar = calendar;
		return this;
	}

	public WDateInput setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		return this;
	}

	public WDateInput setTimePartVisible(boolean timePartVisible) {
		this.timePartVisible = timePartVisible;
		return this;
	}

	public WDateInput setDefaultTime(int hour, int minute, int second, int millisecond) {
		this.defaultTime.setTime(hour, minute, second, millisecond);

		return this;
	}

	public WDateInput setDefaultTime(TimeFieldVO other) {
		this.defaultTime.setTime(other);

		return this;
	}

	// ---------------

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(NUMERIC_JS);
		response.render(DATE_POPUP_JS);
		response.render(DATE_CALC_JS);

		DateTimeFieldVO now = calendar.convertToFields(new Date(), getTimeZone());

		response.render(JavaScriptHeaderItem.forScript(
			String.format("var currentDate = %s;", WebUtil.toJson(now)),
			"CURRENT_DATE"));
	}

	@Override
	public void convertInput() {
		Date date = null;

		Integer yearValue = year.getConvertedInput();
		Integer monthValue = month.getConvertedInput();
		Integer dayValue = day.getConvertedInput();

		Integer hourValue = hour.getConvertedInput();
		Integer minuteValue = minute.getConvertedInput();
		Integer secondValue = second.getConvertedInput();

		if (yearValue != null && monthValue != null && dayValue != null) {

			if (timePartVisible) {
				hourValue = hourValue != null ? hourValue : defaultTime.getHour();
				minuteValue = minuteValue != null ? minuteValue : defaultTime.getMinute();
				secondValue = secondValue != null ? secondValue : defaultTime.getSecond();
			} else {
				hourValue = defaultTime.getHour();
				minuteValue = defaultTime.getMinute();
				secondValue = defaultTime.getSecond();
			}

			DateTimeFieldVO dateField = new DateTimeFieldVO(
				yearValue,
				monthValue,
				dayValue,

				hourValue,
				minuteValue,
				secondValue,
				defaultTime.getMillisecond());

			date = calendar.convertToDate(dateField, getTimeZone());
		}

		setConvertedInput(date);
	}

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		mainTable = new WebMarkupContainer("mainTable");
		mainTable.setOutputMarkupId(true);
		add(mainTable);

		mainTable.add(year = createTextField("year"));
		mainTable.add(month = createTextField("month"));
		mainTable.add(day = createTextField("day"));

		WebMarkupContainer timePart = new WebMarkupContainer("timePart");
		timePart.setVisible(timePartVisible);
		timePart.add(hour = createTextField("hour"));
		timePart.add(minute = createTextField("minute"));
		timePart.add(second = createTextField("second"));
		mainTable.add(timePart);

		calOpener = new WebMarkupContainer("calOpener");
		calOpener.setOutputMarkupId(true);
		mainTable.add(calOpener);


		add(new FontAwesomeBehavior());
		add(new CommonBehavior());
		setOutputMarkupId(true);

		// NOTE: the parent of popup-div must have relative position, so on parent scroll, popup-div also moves up/down!
		add(new AttributeModifier("style", "position:relative"));

		if (calendar == null) {
			calendar = getUserPreference().getCalendar();
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Date modelObject = getModelObject();

		if (modelObject != null) {
			DateTimeFieldVO dateFieldVO = calendar.convertToFields(modelObject, getTimeZone());

			if (dateFieldVO != null) {
				year.setModelObject(dateFieldVO.getYear());
				month.setModelObject(dateFieldVO.getMonth());
				day.setModelObject(dateFieldVO.getDay());

				hour.setModelObject(dateFieldVO.getHour());
				minute.setModelObject(dateFieldVO.getMinute());
				second.setModelObject(dateFieldVO.getSecond());
			} else {
				year.setModelObject(null);
				month.setModelObject(null);
				day.setModelObject(null);

				hour.setModelObject(null);
				minute.setModelObject(null);
				second.setModelObject(null);
			}
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible() && isEnabled()) {
			String calType = null;

			switch (calendar) {
				case Gregorian:
					calType = "gregorian";
					break;
				case Persian:
					calType = "jalali";
					break;
			}

			String script = String.format(
				"$('#%s').wDateInput('%s','%s','%s','%s', '%s');",
				calOpener.getMarkupId(), calType, year.getMarkupId(), month.getMarkupId(), day.getMarkupId(),
				mainTable.getMarkupId());

			WebUtil.writeJQueryCall(script, false);
		}
	}

	// ------------------------------

	private TextField<Integer> createTextField(String compId) {
		TextField<Integer> textField = new TextField<>(compId, new Model<>(), Integer.class);
		textField.setOutputMarkupId(true);
		return textField;
	}

	private TimeZone getTimeZone() {
		if (timePartVisible) {
			return timeZone != null ? timeZone : getUserTimeZone();
		} else {
			return TimeZone.getDefault();
		}
	}
}

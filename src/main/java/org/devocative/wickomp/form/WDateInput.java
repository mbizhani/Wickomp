package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.devocative.adroit.CalendarUtil;
import org.devocative.adroit.vo.DateFieldVO;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.Date;

public class WDateInput extends WFormInputPanel<Date> {
	public enum WCalendar {Gregorian, Persian}

	private static final HeaderItem NUMERIC_JS = Resource.getCommonJS("form/autoNumeric.js");
	private static final HeaderItem DATE_CALC_JS = Resource.getCommonJS("form/date/dtjalali.js");
	private static final HeaderItem DATE_JS = Resource.getCommonJS("form/date/date.js");
	private static final HeaderItem DATE_POPUP_JS = Resource.getCommonJS("form/date/dtpopup.js");

	private WebMarkupContainer timePart, calOpener;
	private TextField<Integer> year, month, day, hour, minute, second;
	private WCalendar calendar;

	public WDateInput(String id, WCalendar calendar) {
		this(id, null, calendar);
	}

	// Main Constructor
	public WDateInput(String id, IModel<Date> model, WCalendar calendar) {
		super(id, model);
		this.calendar = calendar;

		add(year = createTextField("year"));
		add(month = createTextField("month"));
		add(day = createTextField("day"));

		year.add(new AttributeModifier("title", new ResourceModel("label.year", "Year")));
		month.add(new AttributeModifier("title", new ResourceModel("label.month", "Month")));
		day.add(new AttributeModifier("title", new ResourceModel("label.day", "Day")));

		timePart = new WebMarkupContainer("timePart");
		timePart.add(hour = createTextField("hour"));
		timePart.add(minute = createTextField("minute"));
		timePart.add(second = createTextField("second"));
		timePart.setVisible(false);
		add(timePart);

		hour.add(new AttributeModifier("title", new ResourceModel("label.hour", "Hour")));
		minute.add(new AttributeModifier("title", new ResourceModel("label.minute", "Minute")));
		second.add(new AttributeModifier("title", new ResourceModel("label.second", "Second")));

		calOpener = new WebMarkupContainer("calOpener");
		calOpener.setOutputMarkupId(true);
		add(calOpener);


		add(new FontAwesomeBehavior());
		add(new CommonBehavior());
		setOutputMarkupId(true);
	}

	// ---------------------- ACCESSORS

	public WDateInput setTimePartVisible(boolean visible) {
		timePart.setVisible(visible);
		return this;
	}

	// ----------------------- INTERNAL METHODS

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(Resource.getJQueryReference());
		response.render(NUMERIC_JS);
		response.render(DATE_JS);
		response.render(DATE_POPUP_JS);
		response.render(DATE_CALC_JS);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Date modelObject = getModelObject();

		if (modelObject != null) {
			DateFieldVO dateFieldVO = null;

			switch (calendar) {
				case Gregorian:
					dateFieldVO = CalendarUtil.getDateField(modelObject);
					break;
				case Persian:
					dateFieldVO = CalendarUtil.toPersianDateField(modelObject);
					break;
			}

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
	protected void convertInput() {
		Date date = null;

		Integer yearValue = year.getConvertedInput();
		Integer monthValue = month.getConvertedInput();
		Integer dayValue = day.getConvertedInput();

		Integer hourValue = hour.getConvertedInput();
		Integer minuteValue = minute.getConvertedInput();
		Integer secondValue = second.getConvertedInput();

		if (yearValue != null && monthValue != null && dayValue != null) {
			DateFieldVO dateField = new DateFieldVO(
				yearValue,
				monthValue,
				dayValue,

				hourValue != null ? hourValue : 0,
				minuteValue != null ? minuteValue : 0,
				secondValue != null ? secondValue : 0);

			switch (calendar) {

				case Gregorian:
					date = CalendarUtil.getDate(dateField);
					break;
				case Persian:
					date = CalendarUtil.toGregorian(dateField);
					break;
			}
		}

		setConvertedInput(date);
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		DateFieldVO now = null;
		String calType = null;

		switch (calendar) {
			case Gregorian:
				now = CalendarUtil.getDateField(new Date());
				calType = "gregorian";
				break;
			case Persian:
				now = CalendarUtil.toPersianDateField(new Date());
				calType = "jalali";
				break;
		}

		String script = String.format("handleDateEvents('%s', '%s', '%s', '%s');" +
				"$('#%s').showDtPopup('%s','%s','%s','%s');",
			getMarkupId(), now.getYear(), now.getMonth(), now.getDay(),
			calOpener.getMarkupId(), calType, year.getMarkupId(), month.getMarkupId(), day.getMarkupId());
		getResponse().write(String.format("<script>%s</script>", script));
	}

	private TextField<Integer> createTextField(String compId) {
		TextField<Integer> textField = new TextField<>(compId, new Model<Integer>(), Integer.class);
		textField.setOutputMarkupId(true);
		return textField;
	}
}
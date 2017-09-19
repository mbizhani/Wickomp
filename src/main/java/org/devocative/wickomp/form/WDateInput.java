package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.CalendarUtil;
import org.devocative.adroit.vo.DateFieldVO;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.opt.OCalendar;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.Date;

public class WDateInput extends WLabeledFormInputPanel<Date> {
	private static final long serialVersionUID = -2348912362832020132L;

	private static final HeaderItem NUMERIC_JS = Resource.getCommonJS("form/autoNumeric.js");
	private static final HeaderItem DATE_CALC_JS = Resource.getCommonJS("form/date/dtjalali.js");
	private static final HeaderItem DATE_POPUP_JS = Resource.getCommonJS("form/date/wDateInput.js");

	// ------------------------------

	private WebMarkupContainer mainTable;
	private WebMarkupContainer calOpener;
	private TextField<Integer> year, month, day, hour, minute, second;

	private OCalendar calendar;
	private boolean timePartVisible = false;

	// ------------------------------

	public WDateInput(String id) {
		this(id, null, null);
	}

	public WDateInput(String id, IModel<Date> model) {
		this(id, model, null);
	}

	public WDateInput(String id, OCalendar calendar) {
		this(id, null, calendar);
	}

	// Main Constructor
	public WDateInput(String id, IModel<Date> model, OCalendar calendar) {
		super(id, model);
		this.calendar = calendar;
	}

	// ------------------------------

	public WDateInput setCalendar(OCalendar calendar) {
		this.calendar = calendar;
		return this;
	}

	public WDateInput setTimePartVisible(boolean timePartVisible) {
		this.timePartVisible = timePartVisible;
		return this;
	}

	// ---------------

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(NUMERIC_JS);
		response.render(DATE_POPUP_JS);
		response.render(DATE_CALC_JS);

		DateFieldVO now = null;

		switch (calendar) {
			case Gregorian:
				now = CalendarUtil.getDateField(new Date());
				break;
			case Persian:
				now = CalendarUtil.toPersianDateField(new Date());
				break;
		}

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
}

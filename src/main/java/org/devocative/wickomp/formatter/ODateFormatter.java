package org.devocative.wickomp.formatter;

import org.apache.wicket.protocol.http.WebSession;
import org.devocative.adroit.date.EUniCalendar;
import org.devocative.wickomp.opt.OUserPreference;

import java.util.Date;

public class ODateFormatter implements OFormatter {
	private static final long serialVersionUID = -4787636025532239614L;

	private EUniCalendar calendar;
	private String pattern;

	// ------------------------------

	public ODateFormatter(EUniCalendar calendar, String pattern) {
		this.calendar = calendar;
		this.pattern = pattern;
	}

	// ------------------------------

	@Override
	public String format(Object value) {
		return calendar.convertToString((Date) value, pattern, getUserPreference().getTimeZone());
	}

	// ------------------------------

	private static final ODateFormatter PERSIAN_DATE = new ODateFormatter(EUniCalendar.Persian, "yyyy/MM/dd");
	private static final ODateFormatter PERSIAN_DATE_TIME = new ODateFormatter(EUniCalendar.Persian, "yyyy/MM/dd HH:mm:ss");

	private static final ODateFormatter GREGORIAN_DATE = new ODateFormatter(EUniCalendar.Gregorian, "yyyy/MM/dd");
	private static final ODateFormatter GREGORIAN_DATE_TIME = new ODateFormatter(EUniCalendar.Gregorian, "yyyy/MM/dd HH:mm:ss");

	private static final OFormatter MILLIS = value -> String.valueOf(((Date) value).getTime());

	// ------------------------------

	public static ODateFormatter prDate() {
		return PERSIAN_DATE;
	}

	public static ODateFormatter prDateTime() {
		return PERSIAN_DATE_TIME;
	}

	public static ODateFormatter grDate() {
		return GREGORIAN_DATE;
	}

	public static ODateFormatter grDateTime() {
		return GREGORIAN_DATE_TIME;
	}

	public static OFormatter millis() {
		return MILLIS;
	}

	// ---------------

	public static ODateFormatter getDateByUserPreference() {
		OUserPreference userPreference = getUserPreference();
		return new ODateFormatter(userPreference.getCalendar(), userPreference.getDatePattern());
	}

	public static ODateFormatter getDateTimeByUserPreference() {
		OUserPreference userPreference = getUserPreference();

		return new ODateFormatter(userPreference.getCalendar(), userPreference.getDateTimePattern());
	}

	// ------------------------------

	private static OUserPreference getUserPreference() {
		OUserPreference userPreference = OUserPreference.DEFAULT;

		WebSession webSession = WebSession.get();
		if (webSession instanceof OUserPreference) {
			userPreference = (OUserPreference) webSession;
		}

		return userPreference;
	}
}

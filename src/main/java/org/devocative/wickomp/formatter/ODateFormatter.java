package org.devocative.wickomp.formatter;

import org.apache.wicket.protocol.http.WebSession;
import org.devocative.adroit.date.EUniCalendar;
import org.devocative.wickomp.opt.OUserPreference;

import java.util.Date;
import java.util.TimeZone;

public class ODateFormatter implements OFormatter {
	private static final long serialVersionUID = -4787636025532239614L;

	private EUniCalendar calendar;
	private String pattern;
	private TimeZone timeZone;

	// ------------------------------

	private ODateFormatter(String pattern) {
		final OUserPreference preference = getUserPreference();

		this.calendar = preference.getCalendar();
		this.pattern = pattern;
		this.timeZone = preference.getTimeZone();
	}

	private ODateFormatter(EUniCalendar calendar, String pattern, TimeZone timeZone) {
		this.calendar = calendar;
		this.pattern = pattern;
		this.timeZone = timeZone;
	}

	// ------------------------------

	@Override
	public String format(Object value) {
		return calendar.convertToString((Date) value, pattern, timeZone);
	}

	// ------------------------------

	public static ODateFormatter date() {
		OUserPreference preference = getUserPreference();
		return new ODateFormatter(preference.getCalendar(), preference.getDatePattern(), preference.getTimeZone());
	}

	public static ODateFormatter dateTime() {
		OUserPreference preference = getUserPreference();
		return new ODateFormatter(preference.getCalendar(), preference.getDateTimePattern(), preference.getTimeZone());
	}

	public static OFormatter millis() {
		return MILLIS;
	}

	public static ODateFormatter of(String pattern) {
		return new ODateFormatter(pattern);
	}

	public static ODateFormatter of(EUniCalendar calendar, String pattern, TimeZone timeZone) {
		return new ODateFormatter(calendar, pattern, timeZone);
	}

	// ---------------

	//@Deprecated
	public static ODateFormatter getDateByUserPreference() {
		return date();
	}

	//@Deprecated
	public static ODateFormatter getDateTimeByUserPreference() {
		return dateTime();
	}

	// ------------------------------

	private static final OFormatter MILLIS = value -> String.valueOf(((Date) value).getTime());

	private static OUserPreference getUserPreference() {
		OUserPreference userPreference = OUserPreference.DEFAULT;

		WebSession webSession = WebSession.get();
		if (webSession instanceof OUserPreference) {
			userPreference = (OUserPreference) webSession;
		}

		return userPreference;
	}
}

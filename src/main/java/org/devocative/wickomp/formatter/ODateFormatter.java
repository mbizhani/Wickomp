package org.devocative.wickomp.formatter;

import org.apache.wicket.protocol.http.WebSession;
import org.devocative.adroit.CalendarUtil;
import org.devocative.wickomp.opt.OCalendar;
import org.devocative.wickomp.opt.OUserPreference;

import java.util.Date;

public class ODateFormatter implements OFormatter {
	private OCalendar calendar;
	private String pattern;

	public ODateFormatter(OCalendar calendar, String pattern) {
		this.calendar = calendar;
		this.pattern = pattern;
	}

	@Override
	public String format(Object value) {
		if (OCalendar.Persian.equals(calendar)) {
			return CalendarUtil.toPersian((Date) value, pattern);
		}
		return CalendarUtil.formatDate((Date) value, pattern);
	}

	private static final ODateFormatter PERSIAN_DATE = new ODateFormatter(OCalendar.Persian, "yyyy/MM/dd");
	private static final ODateFormatter PERSIAN_DATE_TIME = new ODateFormatter(OCalendar.Persian, "yyyy/MM/dd HH:mm:ss");

	private static final ODateFormatter GREGORIAN_DATE = new ODateFormatter(OCalendar.Gregorian, "yyyy/MM/dd");
	private static final ODateFormatter GREGORIAN_DATE_TIME = new ODateFormatter(OCalendar.Gregorian, "yyyy/MM/dd HH:mm:ss");

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

	public static ODateFormatter getDateByUserPreference() {
		WebSession webSession = WebSession.get();
		if(webSession instanceof OUserPreference) {
			OUserPreference userPreference = (OUserPreference) webSession;
			return new ODateFormatter(userPreference.getCalendar(), userPreference.getDatePattern());
		}
		throw new RuntimeException("No user preference for Date");
	}

	public static ODateFormatter getDateTimeByUserPreference() {
		WebSession webSession = WebSession.get();
		if(webSession instanceof OUserPreference) {
			OUserPreference userPreference = (OUserPreference) webSession;
			return new ODateFormatter(userPreference.getCalendar(), userPreference.getDateTimePattern());
		}
		throw new RuntimeException("No user preference for Date");
	}
}

package org.devocative.wickomp.opt;

import org.devocative.adroit.CalendarUtil;
import org.devocative.adroit.date.DateFieldVO;

import java.util.Date;

@Deprecated
public enum OCalendar {
	Gregorian, Persian;

	// ------------------------------

	public String convertToString(Date dt, String pattern) {
		switch (this) {
			case Gregorian:
				return CalendarUtil.formatDate(dt, pattern);

			case Persian:
				return CalendarUtil.toPersian(dt, pattern);
		}

		return null;
	}

	public Date convertToDate(DateFieldVO dateFieldVO) {
		switch (this) {
			case Gregorian:
				return CalendarUtil.getDate(dateFieldVO);
			case Persian:
				return CalendarUtil.toGregorian(dateFieldVO);
		}

		return null;
	}

	public DateFieldVO convertToFields(Date dt) {
		switch (this) {
			case Gregorian:
				return CalendarUtil.getDateField(dt);
			case Persian:
				return CalendarUtil.toPersianDateField(dt);
		}

		return null;
	}
}

package org.devocative.wickomp.opt;

import org.devocative.adroit.date.EUniCalendar;

import java.util.TimeZone;

public interface OUserPreference {
	EUniCalendar getCalendar();

	TimeZone getTimeZone();

	String getDatePattern();

	String getDateTimePattern();

	OLayoutDirection getLayoutDirection();

	OUserPreference DEFAULT = new OUserPreference() {
		@Override
		public EUniCalendar getCalendar() {
			return EUniCalendar.Gregorian;
		}

		@Override
		public TimeZone getTimeZone() {
			return TimeZone.getDefault();
		}

		@Override
		public String getDatePattern() {
			return "yyyy/MM/dd";
		}

		@Override
		public String getDateTimePattern() {
			return "yyyy/MM/dd HH:mm:ss";
		}

		@Override
		public OLayoutDirection getLayoutDirection() {
			return OLayoutDirection.LTR;
		}
	};

}

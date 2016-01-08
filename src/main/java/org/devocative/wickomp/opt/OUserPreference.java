package org.devocative.wickomp.opt;

public interface OUserPreference {
	OCalendar getCalendar();

	String getDatePattern();

	String getDateTimePattern();

	OLayoutDirection getLayoutDirection();

	OUserPreference DEFAULT = new OUserPreference() {
		@Override
		public OCalendar getCalendar() {
			return OCalendar.Gregorian;
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

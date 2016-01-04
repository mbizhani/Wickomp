package org.devocative.wickomp.opt;

public interface OUserPreference {
	OCalendar getCalendar();

	String getDatePattern();

	String getDateTimePattern();

	OLayoutDirection getLayoutDirection();
}

package org.devocative.wickomp.demo;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.devocative.adroit.date.EUniCalendar;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.opt.OUserPreference;

import java.util.Locale;
import java.util.TimeZone;

public class WickompWebSession extends WebSession implements OUserPreference {
	private static final long serialVersionUID = -667320819551579219L;

	public WickompWebSession(Request request) {
		super(request);

		setLocale(new Locale("fa", "IR"));
	}

	@Override
	public EUniCalendar getCalendar() {
		return EUniCalendar.Persian;
	}

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone("GMT+4");
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
		return OLayoutDirection.RTL;
	}

	@Override
	public void onInvalidate() {
		System.out.printf("Session Expired: %s\n", getId());
	}

	// ------------------------------

	public static WickompWebSession get() {
		return (WickompWebSession) WebSession.get();
	}
}

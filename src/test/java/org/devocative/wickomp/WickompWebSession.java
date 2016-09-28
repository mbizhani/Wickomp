package org.devocative.wickomp;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.devocative.wickomp.async.AsyncMediator;
import org.devocative.wickomp.opt.OCalendar;
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
	public OCalendar getCalendar() {
		return OCalendar.Persian;
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
		return OLayoutDirection.RTL;
	}

	@Override
	public void onInvalidate() {
		System.out.printf("Session Expired: %s\n", getId());

		AsyncMediator.handleSessionExpiration(null, getId());
	}
}

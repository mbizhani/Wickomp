package org.devocative.wickomp;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebSession;
import org.devocative.wickomp.opt.OUserPreference;

public class WPanel extends Panel {
	private static final long serialVersionUID = -5462515775771629113L;

	public WPanel(String id) {
		super(id);
	}

	protected final OUserPreference getUserPreference() {
		WebSession session = WebSession.get();
		if (session instanceof OUserPreference) {
			return (OUserPreference) session;
		}
		return OUserPreference.DEFAULT;
	}
}

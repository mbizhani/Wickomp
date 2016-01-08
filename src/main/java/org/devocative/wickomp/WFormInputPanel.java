package org.devocative.wickomp;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.devocative.wickomp.opt.OUserPreference;

public abstract class WFormInputPanel<T> extends FormComponentPanel<T> {
	public WFormInputPanel(String id, IModel<T> model) {
		super(id, model);
	}

	protected final OUserPreference getUserPreference() {
		WebSession session = WebSession.get();
		if (session instanceof OUserPreference) {
			return (OUserPreference) session;
		}
		return OUserPreference.DEFAULT;
	}
}

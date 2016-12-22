package org.devocative.wickomp.html.tab;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.opt.Options;

public class OTab extends Options {
	private static final long serialVersionUID = 1250995600095912956L;

	private IModel<String> title;
	private Boolean closable;

	// ------------------------------

	// Main Constructor
	public OTab(IModel<String> title, Boolean closable) {
		this.title = title;
		this.closable = closable;
	}

	// ------------------------------

	public IModel<String> getTitle() {
		return title;
	}

	public Boolean getClosable() {
		return closable;
	}

	public OTab setClosable(Boolean closable) {
		this.closable = closable;
		return this;
	}

	// ------------------------------

	@Override
	public String toString() {
		return title.getObject();
	}
}

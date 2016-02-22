package org.devocative.wickomp.html.window;

import org.devocative.wickomp.opt.OComponent;

public class OModalWindow extends OComponent {
	private Boolean collapsible = false;
	private Boolean maximizable = false;
	private Boolean minimizable = false;
	private boolean modal = true;
	private String title;

	public Boolean getCollapsible() {
		return collapsible;
	}

	public OModalWindow setCollapsible(Boolean collapsible) {
		this.collapsible = collapsible;
		return this;
	}

	public Boolean getMaximizable() {
		return maximizable;
	}

	public OModalWindow setMaximizable(Boolean maximizable) {
		this.maximizable = maximizable;
		return this;
	}

	public Boolean getMinimizable() {
		return minimizable;
	}

	public OModalWindow setMinimizable(Boolean minimizable) {
		this.minimizable = minimizable;
		return this;
	}

	public boolean isModal() {
		return modal;
	}

	public OModalWindow setModal(boolean modal) {
		this.modal = modal;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public OModalWindow setTitle(String title) {
		this.title = title;
		return this;
	}
}

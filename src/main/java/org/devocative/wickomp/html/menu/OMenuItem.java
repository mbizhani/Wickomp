package org.devocative.wickomp.html.menu;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.opt.Options;

import java.util.ArrayList;
import java.util.List;

public class OMenuItem extends Options {
	private static final long serialVersionUID = 1981474945554177773L;

	private String href;

	private IModel<String> label;

	private List<OMenuItem> subMenus;

	public OMenuItem(IModel<String> label) {
		this(null, label, new ArrayList<OMenuItem>());
	}

	public OMenuItem(IModel<String> label, List<OMenuItem> subMenus) {
		this(null, label, subMenus);
	}

	public OMenuItem(String href, IModel<String> label) {
		this(href, label, new ArrayList<OMenuItem>());
	}

	// Main Constructor
	public OMenuItem(String href, IModel<String> label, List<OMenuItem> subMenus) {
		this.href = href;
		this.label = label;
		this.subMenus = subMenus;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public IModel<String> getLabel() {
		return label;
	}

	public void setLabel(IModel<String> label) {
		this.label = label;
	}

	public List<OMenuItem> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<OMenuItem> subMenus) {
		this.subMenus = subMenus;
	}

	@Override
	public String toString() {
		return label.getObject();
	}
}

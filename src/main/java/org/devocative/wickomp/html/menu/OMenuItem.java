package org.devocative.wickomp.html.menu;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.opt.Options;

import java.util.ArrayList;
import java.util.List;

public class OMenuItem extends Options {
	private static final long serialVersionUID = 1981474945554177773L;

	private String href;

	private IModel<String> label;

	private String icon;

	private List<OMenuItem> subMenus;

	private int level = 0;

	// ------------------------------

	// --- without icon

	public OMenuItem(IModel<String> label) {
		this(null, label, null, new ArrayList<>());
	}

	public OMenuItem(IModel<String> label, List<OMenuItem> subMenus) {
		this(null, label, null, subMenus);
	}

	public OMenuItem(String href, IModel<String> label) {
		this(href, label, null, new ArrayList<>());
	}

	public OMenuItem(String href, IModel<String> label, List<OMenuItem> subMenus) {
		this(href, label, null, subMenus);
	}

	// --- with icon

	public OMenuItem(IModel<String> label, String icon) {
		this(null, label, icon, new ArrayList<>());
	}

	public OMenuItem(IModel<String> label, String icon, List<OMenuItem> subMenus) {
		this(null, label, icon, subMenus);
	}

	public OMenuItem(String href, IModel<String> label, String icon) {
		this(href, label, icon, new ArrayList<>());
	}

	// Main Constructor
	public OMenuItem(String href, IModel<String> label, String icon, List<OMenuItem> subMenus) {
		this.href = href;
		this.label = label;
		this.icon = icon;
		this.subMenus = subMenus;

		calcLevel();
	}

	// ------------------------------

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

	public String getIcon() {
		return icon;
	}

	public OMenuItem setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public List<OMenuItem> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<OMenuItem> subMenus) {
		this.subMenus = subMenus;

		calcLevel();
	}

	public int getLevel() {
		return level;
	}

	// ---------------

	@Override
	public String toString() {
		return label.getObject();
	}

	// ------------------------------

	private void calcLevel() {
		if (subMenus != null) {
			for (OMenuItem subMenu : this.subMenus) {
				subMenu.level = level + 1;
			}
		}
	}

}

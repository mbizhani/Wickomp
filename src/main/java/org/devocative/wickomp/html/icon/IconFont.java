package org.devocative.wickomp.html.icon;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.html.HTMLBase;

public abstract class IconFont extends HTMLBase {
	private static final long serialVersionUID = 1542721539086984091L;

	private String name;

	private IModel<String> tooltip;

	private String styleClass;

	// ------------------------------

	protected IconFont(String name, IModel<String> tooltip) {
		this.name = name;
		this.tooltip = tooltip;
	}

	// ------------------------------

	public abstract IconFont copyTo();

	// ------------------------------

	public String getName() {
		return name;
	}

	public IModel<String> getTooltip() {
		return tooltip;
	}

	public IconFont setTooltip(IModel<String> tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public IconFont setStyleClass(String styleClass) {
		this.styleClass = styleClass;
		return this;
	}
}

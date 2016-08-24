package org.devocative.wickomp.html.icon;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.html.HTMLBase;

public class FontAwesome extends HTMLBase {
	private static final long serialVersionUID = 8571616115102148637L;

	public enum Size {
		lg("lg"), x2("2x"), x3("3x"), x4("4x"), x5("5x");

		private String value;

		Size(String value) {
			this.value = value;
		}


		@Override
		public String toString() {
			return value;
		}
	}

	public enum Flip {horizontal, vertical}

	private String name;

	private Size size;

	private boolean spin = false;

	private Integer rotation;

	private Flip flip;

	private String color;

	private IModel<String> tooltip;

	private String styleClass;

	public FontAwesome(String name) {
		this(name, null);
	}

	// Main Constructor
	public FontAwesome(String name, IModel<String> tooltip) {
		this.name = name;
		this.tooltip = tooltip;
	}

	public FontAwesome setSize(Size size) {
		this.size = size;
		return this;
	}

	public FontAwesome setColor(String color) {
		this.color = color;
		return this;
	}

	public FontAwesome setStyleClass(String styleClass) {
		this.styleClass = styleClass;
		return this;
	}

	public FontAwesome spin() {
		this.spin = true;
		return this;
	}

	public FontAwesome rotate(Integer rotation) {
		this.rotation = rotation;
		return this;
	}

	public FontAwesome flip(Flip flip) {
		this.flip = flip;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<i class=\"fa fa-").append(name);

		if (size != null) {
			builder.append(" fa-").append(size);
		}
		if (spin) {
			builder.append(" fa-spin");
		}
		if (rotation != null) {
			builder.append(" fa-rotate-").append(rotation);
		}
		if (flip != null) {
			builder.append(" fa-flip-").append(flip);
		}
		if (styleClass != null) {
			builder.append(" ").append(styleClass);
		}
		builder.append("\"");

		if (color != null) {
			builder.append(" style=\"color:").append(color).append("\"");
		}

		if (tooltip != null) {
			builder.append(" title=\"").append(tooltip.getObject()).append("\"");
		}

		builder.append("></i>");
		return builder.toString();
	}
}

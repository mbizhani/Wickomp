package org.devocative.wickomp.html.icon;

import org.apache.wicket.model.IModel;
import org.devocative.wickomp.html.HTMLBase;

public class FontAwesome extends HTMLBase {
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

	public FontAwesome(String name) {
		this(name, null, null, null);
	}

	public FontAwesome(String name, String color) {
		this(name, null, color, null);
	}

	public FontAwesome(String name, String color, IModel<String> tooltip) {
		this(name, null, color, tooltip);
	}

	// Main Constructor
	public FontAwesome(String name, Size size, String color, IModel<String> tooltip) {
		this.name = name;
		this.size = size;
		this.color = color;
		this.tooltip = tooltip;
	}

	public FontAwesome name(String name) {
		this.name = name;
		return this;
	}

	public FontAwesome size(Size size) {
		this.size = size;
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

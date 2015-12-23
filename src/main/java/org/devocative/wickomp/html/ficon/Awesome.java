package org.devocative.wickomp.html.ficon;

import org.devocative.wickomp.html.HTMLBase;

public class Awesome extends HTMLBase {
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

	public Awesome(String name) {
		this(name, null);
	}

	// Main Constructor
	public Awesome(String name, Size size) {
		this.name = name;
		this.size = size;
	}

	public Awesome name(String name) {
		this.name = name;
		return this;
	}

	public Awesome size(Size size) {
		this.size = size;
		return this;
	}

	public Awesome spin() {
		this.spin = true;
		return this;
	}

	public Awesome rotate(Integer rotation) {
		this.rotation = rotation;
		return this;
	}

	public Awesome flip(Flip flip) {
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

		builder.append("\"></i>");
		return builder.toString();
	}
}

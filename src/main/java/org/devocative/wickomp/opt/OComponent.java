package org.devocative.wickomp.opt;

public abstract class OComponent extends Options {
	private static final long serialVersionUID = -2440914930844225509L;

	private Boolean fit;
	private OSize height;
	private OSize width;

	public Boolean getFit() {
		return fit;
	}

	public OComponent setFit(Boolean fit) {
		this.fit = fit;
		return this;
	}

	public OSize getHeight() {
		return height;
	}

	public OComponent setHeight(OSize height) {
		this.height = height;
		return this;
	}

	public OSize getWidth() {
		return width;
	}

	public OComponent setWidth(OSize width) {
		this.width = width;
		return this;
	}
}

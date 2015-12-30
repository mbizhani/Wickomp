package org.devocative.wickomp.opt;

public abstract class OComponent extends Options {
	private OSize width = OSize.percent(100);
	private OSize height;

	public OSize getWidth() {
		return width;
	}

	public OComponent setWidth(OSize width) {
		this.width = width;
		return this;
	}

	public OSize getHeight() {
		return height;
	}

	public OComponent setHeight(OSize height) {
		this.height = height;
		return this;
	}
}

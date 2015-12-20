package org.devocative.wickomp.opt;

public abstract class OComponent extends Options {
	private OSize width;

	public OSize getWidth() {
		return width;
	}

	public OComponent setWidth(OSize width) {
		this.width = width;
		return this;
	}
}

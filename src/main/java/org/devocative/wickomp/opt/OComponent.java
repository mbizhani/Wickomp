package org.devocative.wickomp.opt;

public abstract class OComponent extends Options {
	private OSize width = OSize.percent(100);

	public OSize getWidth() {
		return width;
	}

	public OComponent setWidth(OSize width) {
		this.width = width;
		return this;
	}
}

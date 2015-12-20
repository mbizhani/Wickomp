package org.devocative.wickomp.html;

public class Anchor extends HTMLElement {
	public enum ETarget {_blank, _parent, _self, _top}

	public Anchor() {
		super("a");
	}

	public Anchor setHref(String href) {
		addAttr("href", href);
		return this;
	}

	public Anchor setTarget(ETarget target) {
		addAttr("target", target.toString());
		return this;
	}
}

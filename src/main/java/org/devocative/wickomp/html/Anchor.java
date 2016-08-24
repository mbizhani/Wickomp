package org.devocative.wickomp.html;

public class Anchor extends HTMLElement {
	private static final long serialVersionUID = 4262218185221880076L;

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

package org.devocative.wickomp.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HTMLElement extends HTMLBase {
	private static final long serialVersionUID = 9150994207417703095L;

	private String name;
	private Map<String, String> attributes = new HashMap<String, String>();
	protected List<HTMLBase> children = new ArrayList<HTMLBase>();

	protected HTMLElement(String name) {
		this.name = name;
	}

	protected void addAttr(String name, String value) {
		if (name != null && value != null) {
			attributes.put(name, value);
		}
	}

	protected void addEvent(String name, String handler) {
		if (name != null && handler != null) {
			attributes.put(name, handler);
		}
	}

	public HTMLElement addChild(HTMLBase child) {
		if (child != null) {
			children.add(child);
		}
		return this;
	}

	public HTMLElement removeAllChildren() {
		children.clear();
		return this;
	}

	// --------------- Attributes ---------------

	public HTMLElement setHtmlClass(String cls) {
		addAttr("class", cls);
		return this;
	}

	public HTMLElement setStyle(String style) {
		addAttr("style", style);
		return this;
	}

	public HTMLElement setTitle(String title) {
		addAttr("title", title);
		return this;
	}

	// --------------- Events ---------------

	public HTMLElement setOnClick(String handler) {
		addEvent("onclick", handler);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<").append(name);
		for (Map.Entry<String, String> attr : attributes.entrySet()) {
			builder.append(" ")
				.append(attr.getKey())
				.append("=\"")
				.append(attr.getValue())
				.append("\"");
		}
		if (children.size() > 0) {
			builder.append(">");
			for (HTMLBase child : children) {
				builder.append(child.toString());
			}
			builder.append("</").append(name).append(">");
		} else {
			builder.append("/>");
		}
		return builder.toString();
	}
}

package org.devocative.wickomp.html;

import java.io.Serializable;

public class HTMLBase implements Serializable {
	private static final long serialVersionUID = 7436993663614204746L;

	private String allHtml;

	public HTMLBase() {
	}

	public HTMLBase(String allHtml) {
		this.allHtml = allHtml;
	}

	@Override
	public String toString() {
		return allHtml;
	}
}

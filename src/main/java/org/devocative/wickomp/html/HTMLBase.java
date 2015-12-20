package org.devocative.wickomp.html;

import java.io.Serializable;

public class HTMLBase implements Serializable {
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

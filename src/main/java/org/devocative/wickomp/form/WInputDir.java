package org.devocative.wickomp.form;

public enum WInputDir {
	AUTO("auto"), RTL("rtl"), LTR("ltr");

	private String html;

	WInputDir(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	@Override
	public String toString() {
		return getHtml();
	}
}

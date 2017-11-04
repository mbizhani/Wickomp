package org.devocative.wickomp.opt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OStyle extends Options {
	private static final long serialVersionUID = 8347783888547549864L;

	private String style;
	private String styleClass;

	// ------------------------------

	private OStyle(String style, String styleClass) {
		this.style = style;
		this.styleClass = styleClass;
	}

	// ------------------------------

	public String getStyle() {
		return style;
	}

	@JsonProperty("class")
	public String getStyleClass() {
		return styleClass;
	}

	// ------------------------------

	public static OStyle css(String styleClass) {
		return new OStyle(null, styleClass);
	}

	public static OStyle style(String style) {
		return new OStyle(style, null);
	}

	public static OStyle both(String style, String styleClass) {
		return new OStyle(style, styleClass);
	}
}

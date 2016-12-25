package org.devocative.wickomp.html.tab;

import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.OComponent;

public class OTabbedPanel extends OComponent implements ICallbackUrl {
	private static final long serialVersionUID = 2594082739919768676L;

	public enum OPosition {top, bottom, left, right, side}

	private Boolean border;
	private Boolean justified;
	private Boolean narrow;
	private Boolean plain;
	private Boolean pill;
	private Boolean showHeader;
	private OPosition tabPosition;
	// ---------------
	private String url;

	// ------------------------------

	public Boolean getBorder() {
		return border;
	}

	public OTabbedPanel setBorder(Boolean border) {
		this.border = border;
		return this;
	}

	public Boolean getJustified() {
		return justified;
	}

	public OTabbedPanel setJustified(Boolean justified) {
		this.justified = justified;
		return this;
	}

	public Boolean getNarrow() {
		return narrow;
	}

	public OTabbedPanel setNarrow(Boolean narrow) {
		this.narrow = narrow;
		return this;
	}

	public Boolean getPlain() {
		return plain;
	}

	public OTabbedPanel setPlain(Boolean plain) {
		this.plain = plain;
		return this;
	}

	public Boolean getPill() {
		return pill;
	}

	public OTabbedPanel setPill(Boolean pill) {
		this.pill = pill;
		return this;
	}

	public Boolean getShowHeader() {
		return showHeader;
	}

	public OTabbedPanel setShowHeader(Boolean showHeader) {
		this.showHeader = showHeader;
		return this;
	}

	public OPosition getTabPosition() {
		return tabPosition;
	}

	public OTabbedPanel setTabPosition(OPosition tabPosition) {
		this.tabPosition = tabPosition;
		return this;
	}

	// ---------------

	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
}

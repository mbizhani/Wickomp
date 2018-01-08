package org.devocative.wickomp.html.tab;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.OComponent;

public class OTabbedPanel extends OComponent implements ICallbackUrl {
	private static final long serialVersionUID = 2594082739919768676L;

	public enum OPosition {top, bottom, left, right, side}

	private Boolean border;
	private Boolean justified;
	private Boolean narrow;
	private String onAdd;
	private String onSelect;
	private Boolean plain;
	private Boolean pill;
	private Boolean showHeader;
	private OPosition tabPosition;

	private Boolean globalHotkeyEnabled; // default=false
	private Integer changeTabKeyCode;    // default is 191 which is '/'
	private Integer closeTabKeyCode;     // default is 190 which is '.'

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

	@JsonRawValue
	public String getOnAdd() {
		return onAdd;
	}

	public OTabbedPanel setOnAdd(String onAdd) {
		this.onAdd = onAdd;
		return this;
	}

	@JsonRawValue
	public String getOnSelect() {
		return onSelect;
	}

	public OTabbedPanel setOnSelect(String onSelect) {
		this.onSelect = onSelect;
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

	public Boolean getGlobalHotkeyEnabled() {
		return globalHotkeyEnabled;
	}

	public OTabbedPanel setGlobalHotkeyEnabled(Boolean globalHotkeyEnabled) {
		this.globalHotkeyEnabled = globalHotkeyEnabled;
		return this;
	}

	public Integer getChangeTabKeyCode() {
		return changeTabKeyCode;
	}

	public OTabbedPanel setChangeTabKeyCode(Integer changeTabKeyCode) {
		this.changeTabKeyCode = changeTabKeyCode;
		return this;
	}

	public Integer getCloseTabKeyCode() {
		return closeTabKeyCode;
	}

	public OTabbedPanel setCloseTabKeyCode(Integer closeTabKeyCode) {
		this.closeTabKeyCode = closeTabKeyCode;
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

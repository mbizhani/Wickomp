package org.devocative.wickomp.html.window;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.OAnimation;
import org.devocative.wickomp.opt.OComponent;

public class OModalWindow extends OComponent implements ICallbackUrl {
	private static final long serialVersionUID = -1690151110434417397L;

	private Boolean collapsible;
	private Boolean maximizable;
	private Boolean minimizable;
	private Boolean modal;
	private String title;

	private OAnimation openAnimation;
	private Integer openDuration;
	private OAnimation closeAnimation;
	private Integer closeDuration;

	// ---------------

	private String url;
	private Boolean callbackOnClose;
	private Boolean closeOnEscape;

	// ------------------------------

	public Boolean getCollapsible() {
		return collapsible;
	}

	public OModalWindow setCollapsible(Boolean collapsible) {
		this.collapsible = collapsible;
		return this;
	}

	public Boolean getMaximizable() {
		return maximizable;
	}

	public OModalWindow setMaximizable(Boolean maximizable) {
		this.maximizable = maximizable;
		return this;
	}

	public Boolean getMinimizable() {
		return minimizable;
	}

	public OModalWindow setMinimizable(Boolean minimizable) {
		this.minimizable = minimizable;
		return this;
	}

	public Boolean getModal() {
		return modal;
	}

	public OModalWindow setModal(boolean modal) {
		this.modal = modal;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public OModalWindow setTitle(String title) {
		this.title = title;
		return this;
	}

	public OAnimation getOpenAnimation() {
		return openAnimation;
	}

	public OModalWindow setOpenAnimation(OAnimation openAnimation) {
		this.openAnimation = openAnimation;
		return this;
	}

	public Integer getOpenDuration() {
		return openDuration;
	}

	public OModalWindow setOpenDuration(Integer openDuration) {
		this.openDuration = openDuration;
		return this;
	}

	public OAnimation getCloseAnimation() {
		return closeAnimation;
	}

	public OModalWindow setCloseAnimation(OAnimation closeAnimation) {
		this.closeAnimation = closeAnimation;
		return this;
	}

	public Integer getCloseDuration() {
		return closeDuration;
	}

	public OModalWindow setCloseDuration(Integer closeDuration) {
		this.closeDuration = closeDuration;
		return this;
	}

	// ---------------

	@JsonProperty("closeCallbackUrl")
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getCallbackOnClose() {
		return callbackOnClose;
	}

	public OModalWindow setCallbackOnClose(Boolean callbackOnClose) {
		this.callbackOnClose = callbackOnClose;
		return this;
	}

	public Boolean getCloseOnEscape() {
		return closeOnEscape;
	}

	public OModalWindow setCloseOnEscape(Boolean closeOnEscape) {
		this.closeOnEscape = closeOnEscape;
		return this;
	}
}

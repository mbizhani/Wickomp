package org.devocative.wickomp.html.window;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.OAnimation;
import org.devocative.wickomp.opt.OComponent;

public class OWindow extends OComponent implements ICallbackUrl {
	private static final long serialVersionUID = -913262235708657456L;

	private Boolean closable;     // JS Default: true
	private Boolean collapsible;  // JS Default: false
	private Boolean inline;       // JS Default: false
	private Boolean maximizable;  // JS Default: false
	private Boolean minimizable;  // JS Default: false
	private Boolean modal;        // JS Default: false
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

	public Boolean getClosable() {
		return closable;
	}

	public OWindow setClosable(Boolean closable) {
		this.closable = closable;
		return this;
	}

	public Boolean getCollapsible() {
		return collapsible;
	}

	public OWindow setCollapsible(Boolean collapsible) {
		this.collapsible = collapsible;
		return this;
	}

	public Boolean getInline() {
		return inline;
	}

	public OWindow setInline(Boolean inline) {
		this.inline = inline;
		return this;
	}

	public Boolean getMaximizable() {
		return maximizable;
	}

	public OWindow setMaximizable(Boolean maximizable) {
		this.maximizable = maximizable;
		return this;
	}

	public Boolean getMinimizable() {
		return minimizable;
	}

	public OWindow setMinimizable(Boolean minimizable) {
		this.minimizable = minimizable;
		return this;
	}

	public Boolean getModal() {
		return modal;
	}

	public OWindow setModal(boolean modal) {
		this.modal = modal;
		return this;
	}

	public OWindow setModal(Boolean modal) {
		this.modal = modal;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public OWindow setTitle(String title) {
		this.title = title;
		return this;
	}

	public OAnimation getOpenAnimation() {
		return openAnimation;
	}

	public OWindow setOpenAnimation(OAnimation openAnimation) {
		this.openAnimation = openAnimation;
		return this;
	}

	public Integer getOpenDuration() {
		return openDuration;
	}

	public OWindow setOpenDuration(Integer openDuration) {
		this.openDuration = openDuration;
		return this;
	}

	public OAnimation getCloseAnimation() {
		return closeAnimation;
	}

	public OWindow setCloseAnimation(OAnimation closeAnimation) {
		this.closeAnimation = closeAnimation;
		return this;
	}

	public Integer getCloseDuration() {
		return closeDuration;
	}

	public OWindow setCloseDuration(Integer closeDuration) {
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

	public OWindow setCallbackOnClose(Boolean callbackOnClose) {
		this.callbackOnClose = callbackOnClose;
		return this;
	}

	public Boolean getCloseOnEscape() {
		return closeOnEscape;
	}

	public OWindow setCloseOnEscape(Boolean closeOnEscape) {
		this.closeOnEscape = closeOnEscape;
		return this;
	}
}

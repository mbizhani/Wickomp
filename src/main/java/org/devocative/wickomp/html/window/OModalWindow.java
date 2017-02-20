package org.devocative.wickomp.html.window;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.opt.ICallbackUrl;
import org.devocative.wickomp.opt.OAnimation;
import org.devocative.wickomp.opt.OComponent;

public class OModalWindow extends OComponent implements ICallbackUrl {
	private static final long serialVersionUID = -1690151110434417397L;

	private Boolean collapsible = false;
	private Boolean maximizable = false;
	private Boolean minimizable = false;
	private boolean modal = true;
	private String title;

	private OAnimation openAnimation = OAnimation.fade;
	private Integer openDuration = 800;
	private OAnimation closeAnimation = OAnimation.fade;
	private Integer closeDuration = 800;

	// ---------------

	private String url;
	private boolean callBackOnClose = false;

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

	public boolean isModal() {
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

	// ------------------------------

	@JsonRawValue
	public String getOnClose() {
		return callBackOnClose ? String.format("function(){Wicket.Ajax.get({u:'%s'});}", url) : null;
	}

	// ------------------------------

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	public OModalWindow setCallBackOnClose(boolean callBackOnClose) {
		this.callBackOnClose = callBackOnClose;
		return this;
	}
}

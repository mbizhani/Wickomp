package org.devocative.wickomp.html.tab;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.json.JsonUseProperty;
import org.devocative.wickomp.json.UsePropertySerializer;
import org.devocative.wickomp.opt.Options;

public class OTab extends Options {
	private static final long serialVersionUID = 1250995600095912956L;

	private IModel<String> title;
	private Boolean closable;

	private String htmlId;
	private String tabId;

	// ------------------------------

	// Main Constructor
	public OTab(IModel<String> title, Boolean closable) {
		this.title = title;
		this.closable = closable;
	}

	// ------------------------------

	@JsonUseProperty("object")
	@JsonSerialize(using = UsePropertySerializer.class)
	public IModel<String> getTitle() {
		return title;
	}

	public Boolean getClosable() {
		return closable;
	}


	public String getHtmlId() {
		return htmlId;
	}

	public OTab setHtmlId(String htmlId) {
		this.htmlId = htmlId;
		return this;
	}

	@JsonIgnore
	public String getTabId() {
		return tabId;
	}

	public OTab setTabId(String tabId) {
		this.tabId = tabId;
		return this;
	}

	// ------------------------------

	@Override
	public String toString() {
		return title.getObject();
	}
}

package org.devocative.wickomp.grid.column.link;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.html.Anchor;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.html.WMessager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class OAjaxLinkColumn<T> extends OCallbackColumn<T> {
	private static final long serialVersionUID = 6520725348993447160L;

	private String confirmMessage;

	// ------------------------------

	public OAjaxLinkColumn(IModel<String> text, HTMLBase linkContent) {
		super(text, linkContent);
	}

	public OAjaxLinkColumn(IModel<String> text, String field) {
		super(text, field);
	}

	// ------------------------------

	public abstract void onClick(AjaxRequestTarget target, IModel<T> rowData);

	// ---------------

	public OAjaxLinkColumn<T> setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
		return this;
	}


	// ---------------

	public void onException(AjaxRequestTarget target, Exception e, IModel<T> rowData) {
		if (e.getMessage() != null) {
			List<Serializable> error = new ArrayList<>();
			error.add(exceptionToMessageHandler.handleMessage(null, e));
			onError(target, error, rowData);
		}
	}

	public void onError(AjaxRequestTarget target, List<Serializable> errors, IModel<T> rowData) {
		WMessager.show(WebUtil.getStringOfResource("label.error", "Error"), errors, target);
	}

	// ------------------------------

	@Override
	protected void fillAnchor(Anchor anchor, T bean, String id, int colNo, String url) {
		StringBuilder builder = new StringBuilder();
		if (confirmMessage != null) {
			builder.append(String.format("$.messager.confirm(wMsg.warning,'%s',function(r){if(r) ", confirmMessage));
		}
		builder.append(String.format("Wicket.Ajax.get({u:'%s'});", url));
		if (confirmMessage != null) {
			builder.append("});");
		}
		anchor
			.setHref("#")
			.setOnClick(builder.toString());
	}
}

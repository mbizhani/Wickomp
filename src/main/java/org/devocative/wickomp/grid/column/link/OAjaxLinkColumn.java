package org.devocative.wickomp.grid.column.link;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.html.Anchor;
import org.devocative.wickomp.html.HTMLBase;

public abstract class OAjaxLinkColumn<T> extends OCallbackColumn<T> {
	protected OAjaxLinkColumn(IModel<String> text, HTMLBase linkContent) {
		super(text, linkContent);
	}

	public OAjaxLinkColumn(IModel<String> text, String field) {
		super(text, field);
	}

	public abstract void onClick(AjaxRequestTarget target, IModel<T> rowData);

	@Override
	protected void fillAnchor(Anchor anchor, T bean, int rowNo, int colNo, String url) {
		anchor
			.setHref("#")
			.setOnClick(String.format("Wicket.Ajax.get({u:'%s'})", url));
	}

}

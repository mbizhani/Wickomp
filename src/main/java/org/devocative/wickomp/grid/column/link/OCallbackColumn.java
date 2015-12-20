package org.devocative.wickomp.grid.column.link;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.html.Anchor;
import org.devocative.wickomp.html.HTMLBase;

import java.io.Serializable;

public abstract class OCallbackColumn<T extends Serializable> extends OColumn<T> {
	private IModel<String> tooltip;
	private HTMLBase linkContent;

	protected OCallbackColumn(IModel<String> text, HTMLBase linkContent) {
		super(text);
		this.linkContent = linkContent;
	}

	protected OCallbackColumn(IModel<String> text, String dataField) {
		super(text, dataField);
	}

	public OCallbackColumn<T> setTooltip(IModel<String> tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	@Override
	public final String cellValue(T bean, int rowNo, int colNo, String url) {
		Anchor anchor = new Anchor();
		if (linkContent == null) {
			if (getField() != null) {
				Object value = PropertyResolver.getValue(getField(), bean);
				if (value != null) {
					anchor.addChild(new HTMLBase(value.toString()));
				}
			}
		} else {
			anchor.addChild(linkContent);
		}

		if (tooltip != null) {
			anchor.setTitle(tooltip.getObject());
		}

		fillAnchor(anchor, bean, rowNo, colNo, url);

		return anchor.toString();
	}

	protected abstract void fillAnchor(Anchor anchor, T bean, int rowNo, int colNo, String url);
}

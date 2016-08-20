package org.devocative.wickomp.grid.column.link;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.IExceptionToMessageHandler;
import org.devocative.wickomp.WDefaults;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.html.Anchor;
import org.devocative.wickomp.html.HTMLBase;

public abstract class OCallbackColumn<T> extends OColumn<T> {
	private IModel<String> tooltip;
	private HTMLBase linkContent;
	private String linkHTMLClass;
	protected IExceptionToMessageHandler exceptionToMessageHandler = WDefaults.getExceptionToMessageHandler();

	// ------------------------------

	protected OCallbackColumn(IModel<String> text, HTMLBase linkContent) {
		super(text);
		this.linkContent = linkContent;
	}

	protected OCallbackColumn(IModel<String> text, String field) {
		super(text, field);
	}

	// ------------------------------

	public OCallbackColumn<T> setTooltip(IModel<String> tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public OCallbackColumn setLinkHTMLClass(String linkHTMLClass) {
		this.linkHTMLClass = linkHTMLClass;
		return this;
	}

	public OCallbackColumn setExceptionToMessageHandler(IExceptionToMessageHandler exceptionToMessageHandler) {
		this.exceptionToMessageHandler = exceptionToMessageHandler;
		return this;
	}

	// ------------------------------

	@Override
	public final String cellValue(T bean, String id, int colNo, String url) {
		Anchor anchor = new Anchor();
		if (linkContent != null) {
			anchor.addChild(linkContent);
		} else if (!isDummyField()) {
			Object value = PropertyResolver.getValue(getField(), bean);
			if (value != null) {
				anchor.addChild(new HTMLBase(value.toString()));
			}
		}

		if (tooltip != null) {
			anchor.setTitle(tooltip.getObject());
		}

		anchor.setHtmlClass(linkHTMLClass);

		fillAnchor(anchor, bean, id, colNo, url);

		return anchor.toString();
	}

	@Override
	public String footerCellValue(Object bean, int colNo, String url) {
		throw new RuntimeException("Footer not supported in OCallbackColumn!");
	}

	// ------------------------------

	protected abstract void fillAnchor(Anchor anchor, T bean, String id, int colNo, String url);
}

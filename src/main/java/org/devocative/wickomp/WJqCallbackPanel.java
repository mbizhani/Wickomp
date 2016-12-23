package org.devocative.wickomp;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.html.MarkupUtil;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.devocative.wickomp.opt.OComponent;

public abstract class WJqCallbackPanel extends WJqCallbackComponent {
	private static final long serialVersionUID = -826492333243144473L;

	public static final String PANEL = "panel";

	// ------------------------------

	protected WJqCallbackPanel(String id, OComponent options) {
		super(id, options);
	}

	// ------------------------------

	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new PanelMarkupSourcingStrategy(false);
	}

	// ------------------------------

	@Override
	public IMarkupFragment getRegionMarkup() {
		IMarkupFragment markup = super.getRegionMarkup();

		if (markup == null) {
			return null;
		}

		IMarkupFragment panelMarkup = MarkupUtil.findStartTag(markup, PANEL);
		return panelMarkup != null ? panelMarkup : markup;
	}

}

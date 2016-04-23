package org.devocative.wickomp.html;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.Resource;

public class WFloatTable extends WebMarkupContainer {
	private static HeaderItem EQ_JS = Resource.getCommonJS("form/equalize.min.js");
	private static HeaderItem CSS = Resource.getCommonCSS("form/common.css");

	private boolean equalWidth = false;

	public WFloatTable(String id) {
		super(id);

		setOutputMarkupId(true);
	}

	public WFloatTable setEqualWidth(boolean equalWidth) {
		this.equalWidth = equalWidth;
		return this;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(CSS);
		response.render(EQ_JS);
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		StringBuilder script = new StringBuilder()
			.append(String.format("$('#%s').children().addClass('w-float-cell');", getMarkupId()))
			.append(String.format("$('#%s').equalize('height')", getMarkupId()));
		if (equalWidth) {
			script.append(".equalize('width')");
		}
		script.append(";");

		WebUtil.writeJQueryCall(script.toString(), true);
	}
}

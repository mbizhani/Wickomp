package org.devocative.wickomp.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.devocative.wickomp.wrcs.CommonBehavior;

public class WFloatTable extends WebMarkupContainer {
	private static final long serialVersionUID = -5899209300800478452L;

	/*
	private static HeaderItem EQ_JS = Resource.getCommonJS("form/equalize.min.js");
	private boolean equalWidth = false;
	*/

	public WFloatTable(String id) {
		super(id);

		add(new CommonBehavior());
		add(new AttributeModifier("class", "w-float-table"));

		setOutputMarkupId(true);
	}

	/*
	public WFloatTable setEqualWidth(boolean equalWidth) {
		this.equalWidth = equalWidth;
		return this;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(EQ_JS);
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		StringBuilder script = new StringBuilder()
			.append(String.format("$('#%s').children('div').addClass('w-float-cell');", getMarkupId()))
			.append(String.format("$('#%s').equalize({children:'div.w-float-cell', equalize:'height'})", getMarkupId()));
		if (equalWidth) {
			script.append(".equalize({children:'div.w-float-cell', equalize:'width'})");
		}
		script.append(";");

		WebUtil.writeJQueryCall(script.toString(), true);
	}
	*/
}

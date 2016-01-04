package org.devocative.wickomp.form;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

public class WBooleanInput extends WFormInputPanel<Boolean> {
	private static final HeaderItem MAIN_JS = Resource.getCommonJS("form/bool/candlestick.min.js");
	private static final HeaderItem MAIN_CSS = Resource.getCommonCSS("form/bool/candlestick.min.css");

	private HiddenField<String> hidden;

	public WBooleanInput(String id) {
		this(id, null);
	}

	public WBooleanInput(String id, IModel<Boolean> model) {
		super(id, model);

		hidden = new HiddenField<>("hidden", new Model<String>());
		hidden.setOutputMarkupId(true);
		add(hidden);

		add(new FontAwesomeBehavior());
	}

	// ----------------------- INTERNAL METHODS

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);
		response.render(MAIN_CSS);
		response.render(MAIN_JS);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Boolean model = getModelObject();
		hidden.setModelObject(model != null ? model.toString() : null);
	}

	@Override
	protected void convertInput() {
		String input = hidden.getConvertedInput();
		setConvertedInput(input != null ? Boolean.valueOf(input) : null);
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		getResponse().write(
			String.format("<script>$('#%s').candlestick({'on':'true','off':'false','nc':''});</script>",
				hidden.getMarkupId()));

	}
}

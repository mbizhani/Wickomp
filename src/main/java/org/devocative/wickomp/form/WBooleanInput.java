package org.devocative.wickomp.form;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

public class WBooleanInput extends WFormInputPanel<Boolean> {
	private static final HeaderItem MAIN_JS = Resource.getCommonJS("form/bool/candlestick.min.js");
	private static final HeaderItem MAIN_CSS = Resource.getCommonCSS("form/bool/candlestick.min.css");
	private static final HeaderItem FORM_CSS = Resource.getCommonCSS("form/common.css");

	private static final HeaderItem HAMMER_JS = Resource.getCommonJS("hammer/hammer.min.js");
	private static final HeaderItem HAMMER_JQ_JS = Resource.getCommonJS("hammer/hammer.jquery.js");

	private Label label;
	private HiddenField<String> hidden;

	public WBooleanInput(String id) {
		this(id, null);
	}

	// Main Constructor
	public WBooleanInput(String id, IModel<Boolean> model) {
		super(id, model);

		add(label = new Label("label"));

		hidden = new HiddenField<>("hidden", new Model<String>(), String.class);
		hidden.setOutputMarkupId(true);
		add(hidden);

		add(new FontAwesomeBehavior());
	}

	public WBooleanInput setLabelVisible(boolean visible) {
		label.setVisible(visible);
		return this;
	}

	// ----------------------- INTERNAL METHODS

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(HAMMER_JS);
		response.render(HAMMER_JQ_JS);

		response.render(FORM_CSS);
		response.render(MAIN_CSS);
		response.render(MAIN_JS);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		IModel<String> labelModel = getLabel();
		if (labelModel != null) {
			label.setDefaultModel(labelModel);
		} else {
			label.setVisible(false);
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Boolean model = getModelObject();
		hidden.setModelObject(model != null ? model.toString() : null);
	}

	@Override
	public void convertInput() {
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

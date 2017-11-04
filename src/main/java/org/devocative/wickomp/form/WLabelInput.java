package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WLabeledFormInputPanel;

public class WLabelInput extends WLabeledFormInputPanel {
	private static final long serialVersionUID = -3520753290546686461L;

	private Label roInput;

	// ------------------------------

	public WLabelInput(String id) {
		this(id, null);
	}

	public WLabelInput(String id, IModel model) {
		super(id, model);
	}

	// ------------------------------

	@Override
	public void convertInput() {
		setConvertedInput(getModelObject());
	}

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		roInput = new Label("roInput");
		add(roInput);
	}

	@Override
	protected void onBeforeRender() {
		Object modelObject = getModelObject();
		roInput.setDefaultModel(new Model<>(modelObject != null ? modelObject.toString() : null));

		super.onBeforeRender();
	}
}

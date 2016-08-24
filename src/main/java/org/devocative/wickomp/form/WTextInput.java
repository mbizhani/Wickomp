package org.devocative.wickomp.form;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;

public class WTextInput extends WFormInputPanel<String> {
	private static final long serialVersionUID = 7161394972946695203L;

	private Label label;
	private TextField<String> textField;

	// ------------------------------

	public WTextInput(String id) {
		this(id, null);
	}

	// Main Constructor
	public WTextInput(String id, IModel<String> model) {
		super(id, model);

		add(label = new Label("label"));

		textField = new TextField<>("textField", new Model<String>(), String.class);
		add(textField);

		add(new CommonBehavior());
	}

	// ------------------------------

	public WTextInput setLabelVisible(boolean visible) {
		label.setVisible(visible);
		return this;
	}

	// ------------------------------

	@Override
	public WTextInput add(Behavior... behavior) {
		for (Behavior b : behavior) {
			if (b instanceof IValidator) {
				super.add(b);
			} else {
				textField.add(behavior);
			}
		}
		return this;
	}

	@Override
	public void convertInput() {
		setConvertedInput(textField.getConvertedInput());
	}

	// ------------------------------

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

		textField.setModelObject(getModelObject());
	}
}

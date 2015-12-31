package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;

public class WTextInput extends WFormInputPanel<String> {
	private TextField<String> textField;

	public WTextInput(String id) {
		this(id, null);
	}

	public WTextInput(String id, IModel<String> model) {
		super(id, model);

		textField = new TextField<>("textField", new Model<String>());
		add(textField);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		textField.setModelObject(getModelObject());
	}

	@Override
	protected void convertInput() {
		setConvertedInput(textField.getConvertedInput());
	}
}

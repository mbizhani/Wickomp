package org.devocative.wickomp.form;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;

public class WTextInput extends WLabeledFormInputPanel<String> {
	private static final long serialVersionUID = 7161394972946695203L;

	private FormComponent<String> textField;

	// ------------------------------

	public WTextInput(String id) {
		this(id, null, false);
	}

	public WTextInput(String id, boolean password) {
		this(id, null, password);
	}

	public WTextInput(String id, IModel<String> model) {
		this(id, model, false);
	}

	// Main Constructor
	public WTextInput(String id, IModel<String> model, boolean password) {
		super(id, model);

		textField = new WTextField("textField", new Model<String>(), password);
		add(textField);

		add(new CommonBehavior());
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
	protected void onBeforeRender() {
		super.onBeforeRender();

		textField.setModelObject(getModelObject());
	}

	// ------------------------------

	private class WTextField extends AbstractTextComponent<String> {
		private static final long serialVersionUID = 5466481307117485383L;

		private boolean password;

		public WTextField(String id, IModel<String> model, boolean password) {
			super(id, model);
			this.password = password;
			setType(String.class);
		}


		protected void onComponentTag(final ComponentTag tag) {
			checkComponentTag(tag, "input");

			if (password) {
				tag.put("type", "password");
				tag.put("value", "");
			} else {
				tag.put("type", "text");
				tag.put("value", getValue());
			}

			super.onComponentTag(tag);
		}
	}
}

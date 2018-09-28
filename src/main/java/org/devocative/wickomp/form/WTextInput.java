package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
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

	private Integer maxlength;
	private Integer size;
	private WInputDir inputDir = WInputDir.AUTO;

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

		textField = new WTextField("textField", new Model<>(), password);
		add(textField);

		setEscapeModelStrings(false);

		add(new CommonBehavior());
	}

	// ------------------------------

	public WTextInput setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
		return this;
	}

	public WTextInput setSize(Integer size) {
		this.size = size;
		return this;
	}

	public WTextInput setInputDir(WInputDir inputDir) {
		this.inputDir = inputDir;
		return this;
	}

	// ---------------

	@Override
	public void convertInput() {
		setConvertedInput(textField.getConvertedInput());
	}

	// ------------------------------


	@Override
	protected void onInitialize() {
		super.onInitialize();

		textField.setEscapeModelStrings(getEscapeModelStrings());

		if (maxlength != null) {
			textField.add(new AttributeModifier("maxlength", maxlength));
		}

		if (size != null) {
			textField.add(new AttributeModifier("size", size));
		}

		if (inputDir != null) {
			textField.add(new AttributeModifier("dir", inputDir.getHtml()));
		}

		for (Behavior b : getBehaviors()) {
			if (!(b instanceof IValidator)) {
				textField.add(b);
			}
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		textField.setModelObject(getModelObject());
	}

	// ------------------------------

	private class WTextField extends AbstractTextComponent<String> {
		private static final long serialVersionUID = 5466481307117485383L;

		private boolean password;

		WTextField(String id, IModel<String> model, boolean password) {
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

package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.devocative.adroit.ObjectUtil;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;

public class WSqlStringInput extends WFormInputPanel<String> {
	private static final long serialVersionUID = -8999139607839521593L;

	private Label label;
	private TextField<String> text;
	private CheckBox leftBox, rightBox;

	// ------------------------------

	public WSqlStringInput(String id) {
		this(id, null);
	}

	// Main Constructor
	public WSqlStringInput(String id, IModel<String> model) {
		super(id, model);

		add(label = new Label("label"));

		add(text = new TextField<>("text", new Model<String>(), String.class));

		add(leftBox = new CheckBox("leftBox", new Model<>(true)));
		leftBox.add(new AttributeModifier("title", new ResourceModel("WSqlStringInput.startWithAny", "Starts with any")));

		add(rightBox = new CheckBox("rightBox", new Model<>(true)));
		rightBox.add(new AttributeModifier("title", new ResourceModel("WSqlStringInput.endWithAny", "Ends with any")));

		add(new CommonBehavior());
	}

	// ------------------------------ INTERNAL METHODS

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

		String str = getModelObject();
		if (str != null) {
			if (str.startsWith("%")) {
				leftBox.setDefaultModel(new Model<>(true));
				str = str.substring(1);
			} else {
				leftBox.setDefaultModel(new Model<>(false));
			}

			if (str.endsWith("%")) {
				rightBox.setDefaultModel(new Model<>(true));
				str = str.substring(0, str.length() - 1);
			} else {
				rightBox.setDefaultModel(new Model<>(false));
			}

			text.setDefaultModel(new Model<>(str));
		}
	}

	@Override
	public void convertInput() {
		String str = text.getConvertedInput();

		if (str != null) {

			if (ObjectUtil.isTrue(leftBox.getConvertedInput())) {
				str = "%" + str;
			}

			if (ObjectUtil.isTrue(rightBox.getConvertedInput())) {
				str = str + "%";
			}

			setConvertedInput(str);
		}
	}
}

package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.ObjectUtil;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;

public class WSqlStringInput extends WLabeledFormInputPanel<String> {
	private static final long serialVersionUID = -8999139607839521593L;

	private TextField<String> text;
	private CheckBox leftBox, rightBox;

	// ------------------------------

	public WSqlStringInput(String id) {
		this(id, null);
	}

	// Main Constructor
	public WSqlStringInput(String id, IModel<String> model) {
		super(id, model);

		add(text = new TextField<>("text", new Model<>(), String.class));
		add(leftBox = new CheckBox("leftBox", new Model<>(true)));
		add(rightBox = new CheckBox("rightBox", new Model<>(true)));

		add(new CommonBehavior());
	}

	// ------------------------------

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

	// ------------------------------

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
}

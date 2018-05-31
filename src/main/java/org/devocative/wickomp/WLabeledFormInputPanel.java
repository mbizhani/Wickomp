package org.devocative.wickomp;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class WLabeledFormInputPanel<T> extends WFormInputPanel<T> {
	private static final long serialVersionUID = 6423044126826123412L;

	private Label label;

	// ------------------------------

	public WLabeledFormInputPanel(String id, IModel<T> model) {
		super(id, model);

		add(label = new Label("label"));
	}

	// ------------------------------

	public WLabeledFormInputPanel<T> setLabelVisible(boolean visible) {
		label.setVisible(visible);
		return this;
	}

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		IModel<String> labelModel = getLabel();
		if (labelModel != null) {
			if (isRequired()) {
				label.setDefaultModel(new Model<>(labelModel.getObject() + " *"));
			} else {
				label.setDefaultModel(labelModel);
			}
		} else {
			label.setVisible(false);
		}
	}
}

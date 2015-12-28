package org.devocative.wickomp;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

public abstract class WFormInputPanel<T extends Serializable> extends FormComponentPanel<T> {
	public WFormInputPanel(String id, IModel<T> model) {
		super(id, model);
	}
}

package org.devocative.wickomp.form.range;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.IRange;
import org.devocative.wickomp.form.WTextInput;

public class WStringRangeInput extends WBaseRangeInput<String> {
	private static final long serialVersionUID = 6127097391007670937L;

	// ------------------------------

	public WStringRangeInput(String id) {
		this(id, null);
	}

	public WStringRangeInput(String id, IModel<IRange<String>> model) {
		super(id, model);
	}

	// ------------------------------

	@Override
	protected FormComponent<String> createFormComponent(String id, IModel<String> model) {
		return new WTextInput(id, model);
	}
}

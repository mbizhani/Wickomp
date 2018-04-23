package org.devocative.wickomp.form.range;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.IRange;
import org.devocative.wickomp.form.WTextInput;

public class WTextRangeInput extends WBaseRangeInput<String> {
	private static final long serialVersionUID = 6127097391007670937L;

	private Integer maxlength;
	private Integer size;

	// ------------------------------

	public WTextRangeInput(String id) {
		this(id, null);
	}

	public WTextRangeInput(String id, IModel<IRange<String>> model) {
		super(id, model);
	}

	// ------------------------------

	public WTextRangeInput setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
		return this;
	}

	public WTextRangeInput setSize(Integer size) {
		this.size = size;
		return this;
	}

	// ------------------------------

	@Override
	protected FormComponent<String> createFormComponent(String id, IModel<String> model) {
		return new WTextInput(id, model)
			.setMaxlength(maxlength)
			.setSize(size);
	}
}

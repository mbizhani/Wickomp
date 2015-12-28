package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;

import java.util.ArrayList;
import java.util.List;

public class WNumberInput extends WFormInputPanel<Number> {
	private TextField<Number> numberField;

	/*private Integer precision;
	private String groupSeparator;

	private Integer min;
	private Integer max;*/

	private List<String> options = new ArrayList<>();


	public WNumberInput(String id) {
		this(id, null);
	}

	public WNumberInput(String id, IModel<Number> model) {
		super(id, model);

		numberField = new TextField<>("numberField");
		add(numberField);
	}

	public WNumberInput setPrecision(Integer precision) {
		//this.precision = precision;
		options.add("precision:" + precision);
		return this;
	}

	public WNumberInput setGroupSeparator(String groupSeparator) {
		//this.groupSeparator = groupSeparator;
		options.add("groupSeparator:'" + groupSeparator + "'");
		return this;
	}

	public WNumberInput setMin(Integer min) {
		//this.min = min;
		options.add("min:" + min);
		return this;
	}

	public WNumberInput setMax(Integer max) {
		//this.max = max;
		options.add("max:" + max);
		return this;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		numberField.setModel(new Model<>(getModelObject()));

		if (options.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(options.get(0));

			for (int i = 1; i < options.size(); i++) {
				builder.append(",").append(options.get(i));
			}
			numberField.add(new AttributeModifier("data-options", builder.toString()));
		}
	}

	@Override
	protected void convertInput() {
		setConvertedInput(numberField.getConvertedInput());
	}
}

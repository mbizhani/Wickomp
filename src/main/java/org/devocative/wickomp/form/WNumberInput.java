package org.devocative.wickomp.form;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.HashMap;
import java.util.Map;

public class WNumberInput extends WLabeledFormInputPanel<Number> {
	private static final long serialVersionUID = 3194204815848275324L;

	private static final HeaderItem NUMERIC_JS = Resource.getCommonJS("form/autoNumeric.js");

	private TextField<String> numberField;

	private Map<String, Object> options = new HashMap<>();

	// ------------------------------

	public WNumberInput(String id, Class<? extends Number> type) {
		this(id, null, type);
	}

	// Main Constructor
	public WNumberInput(String id, IModel<Number> model, Class<? extends Number> type) {
		super(id, model);
		setType(type);

		numberField = new TextField<>("numberField", new Model<String>(), String.class);
		numberField.setOutputMarkupId(true);
		add(numberField);

		options.put("aSep", ",");
		options.put("mDec", "0");

		add(new CommonBehavior());
	}

	// ------------------------------

	public WNumberInput setPrecision(Integer precision) {
		options.put("mDec", precision);
		return this;
	}

	public WNumberInput setThousandSeparator(Character thousandSeparator) {
		if (thousandSeparator != null) {
			options.put("aSep", thousandSeparator);
		}
		return this;
	}

	public WNumberInput setMin(Integer min) {
		options.put("vMin", min);
		return this;
	}

	public WNumberInput setMax(Integer max) {
		options.put("vMin", max);
		return this;
	}

	// ------------------------------

	public WNumberInput removeThousandSeparator() {
		options.put("aSep", "");
		return this;
	}

	@Override
	public WNumberInput add(Behavior... behavior) {
		for (Behavior b : behavior) {
			if (b instanceof IValidator) {
				super.add(b);
			} else {
				numberField.add(behavior);
			}
		}
		return this;
	}

	@Override
	public void convertInput() {
		String convertedInput = numberField.getConvertedInput();
		if (convertedInput != null) {
			if (!options.get("aSep").toString().isEmpty()) {
				String reg = String.format("[%s]", options.get("aSep"));
				convertedInput = convertedInput.replaceAll(reg, "");
			}
			IConverter<Number> converter = getConverter(getType());
			setConvertedInput(converter.convertToObject(convertedInput, getLocale()));
		} else {
			setConvertedInput(null);
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);
		response.render(NUMERIC_JS);
	}

	// ------------------------------

	@Override
	protected void onBeforeRender() {
		if (getModelObject() != null) {
			numberField.setModelObject(getModelObject().toString());
		} else {
			numberField.setModelObject(null);
		}

		super.onBeforeRender();
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible() && isEnabled()) {
			String script = String.format("$('#%s').autoNumeric('init', %s);",
				numberField.getMarkupId(), WebUtil.toJson(options));

			WebUtil.writeJQueryCall(script, false);
		}
	}
}

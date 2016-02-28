package org.devocative.wickomp.form;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.HashMap;
import java.util.Map;

public class WNumberInput extends WFormInputPanel<Number> {
	private static final HeaderItem NUMERIC_JS = Resource.getCommonJS("form/autoNumeric.js");

	private Label label;
	private TextField<String> numberField;

	private Map<String, Object> options = new HashMap<>();

	public WNumberInput(String id, Class<? extends Number> type) {
		this(id, null, type);
	}

	// Main Constructor
	public WNumberInput(String id, IModel<Number> model, Class<? extends Number> type) {
		super(id, model);
		setType(type);

		add(label = new Label("label"));

		numberField = new TextField<>("numberField", new Model<String>());
		numberField.setOutputMarkupId(true);
		add(numberField);

		options.put("aSep", "");
		options.put("mDec", "0");

		add(new CommonBehavior());
	}

	public WNumberInput setPrecision(Integer precision) {
		options.put("mDec", precision);
		return this;
	}

	public WNumberInput setThousandSeparator(String thousandSeparator) {
		options.put("aSep", thousandSeparator);
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

	public WNumberInput setLabelVisible(boolean visible) {
		label.setVisible(visible);
		return this;
	}

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

		if (getModelObject() != null) {
			numberField.setModelObject(getModelObject().toString());
		} else {
			numberField.setModelObject(null);
		}
	}

	@Override
	protected void convertInput() {
		String convertedInput = numberField.getConvertedInput();
		if (convertedInput != null) {
			if (options.get("aSep") != null) {
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

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("$('#%1$s').autoNumeric('init', %2$s);",
			numberField.getMarkupId(), JsonUtil.toJson(options));

		getResponse().write(String.format("<script>%s</script>", script));
	}
}

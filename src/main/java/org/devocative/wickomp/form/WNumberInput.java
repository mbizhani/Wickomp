package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.HiddenField;
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

	private WebComponent numberField;
	private HiddenField<String> hiddenField;

	private Map<String, Object> options = new HashMap<>();

	public WNumberInput(String id, Class<? extends Number> type) {
		this(id, null, type);
	}

	// Main Constructor
	public WNumberInput(String id, IModel<Number> model, Class<? extends Number> type) {
		super(id, model);
		setType(type);

		numberField = new WebComponent("numberField");
		numberField.setOutputMarkupId(true);
		add(numberField);

		hiddenField = new HiddenField<>("hidden", new Model<String>());
		hiddenField.setOutputMarkupId(true);
		add(hiddenField);

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

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		Object number = getModelObject();
		if (number != null) {
			numberField.add(new AttributeModifier("value", number.toString()));
			hiddenField.setModelObject(number.toString());
		}
	}

	@Override
	protected void convertInput() {
		IConverter<Number> converter = getConverter(getType());
		setConvertedInput(converter.convertToObject(hiddenField.getConvertedInput(), getLocale()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);
		response.render(NUMERIC_JS);
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("$('#%s').autoNumeric('init', %s);",
			numberField.getMarkupId(), JsonUtil.toJson(options));

		script += String.format("$('#%1$s').bind('keypress',function(){$('#%2$s').val($('#%1$s').autoNumeric('get'));} );",
			numberField.getMarkupId(), hiddenField.getMarkupId());

		AjaxRequestTarget ajaxRequestTarget = getRequestCycle().find(AjaxRequestTarget.class);
		if (ajaxRequestTarget == null)
			getResponse().write(String.format("<script>%s</script>", script));
		else
			ajaxRequestTarget.appendJavaScript(script);
	}
}

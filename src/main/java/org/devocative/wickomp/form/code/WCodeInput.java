package org.devocative.wickomp.form.code;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.Resource;

public class WCodeInput extends WFormInputPanel<String> {
	private static final HeaderItem MAIN_CSS = Resource.getCommonCSS("codemirror/codemirror.css");
	private static final HeaderItem MAIN_JS = Resource.getCommonJS("codemirror/codemirror.js");
	private static final HeaderItem JQ_JS = Resource.getCommonJS("codemirror/wcodemirror.js");

	private OCode options;
	private TextArea<String> editor;

	public WCodeInput(String id, OCode options) {
		this(id, null, options);
	}

	public WCodeInput(String id, IModel<String> model, OCode options) {
		super(id, model);

		this.options = options;

		editor = new TextArea<>("editor", new Model<String>());
		editor.setOutputMarkupId(true);
		add(editor);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		editor.setModelObject(getModelObject());
	}

	@Override
	public void convertInput() {
		setConvertedInput(editor.getConvertedInput());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(MAIN_CSS);
		response.render(MAIN_JS);
		response.render(JQ_JS);

		response.render(Resource.getCommonJS(String.format("codemirror/mode/%s.js", options.getMode().getJsFile())));

		if(options.getMode().isHasHint()) {
			response.render(Resource.getCommonCSS("codemirror/hint/show-hint.css"));
			response.render(Resource.getCommonJS("codemirror/hint/show-hint.js"));
			response.render(Resource.getCommonJS(String.format("codemirror/hint/%s-hint.js", options.getMode().getJsFile())));
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("$('#%s').codemirror(%s);",
			editor.getMarkupId(),
			JsonUtil.toJson(options));

		getWebResponse().write("<script>" + script + "</script>");
	}
}

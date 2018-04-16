package org.devocative.wickomp.form.code;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.EasyUIBehavior;
import org.devocative.wickomp.wrcs.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WCodeInput extends WFormInputPanel<String> {
	private static final long serialVersionUID = 4501207922641028914L;
	private static final Logger logger = LoggerFactory.getLogger(WCodeInput.class);

	private static final HeaderItem MAIN_CSS = Resource.getCommonCSS("codemirror/codemirror.css");
	private static final HeaderItem MAIN_JS = Resource.getCommonJS("codemirror/codemirror.js");
	private static final HeaderItem JQ_JS = Resource.getCommonJS("codemirror/wCodeInput.js");

	// ------------------------------

	private OCode options;
	private TextArea<String> editor;

	// ------------------------------

	public WCodeInput(String id, OCode options) {
		this(id, null, options);
	}

	public WCodeInput(String id, IModel<String> model, OCode options) {
		super(id, model);

		this.options = options;

		editor = new TextArea<>("editor", new Model<>());
		editor.setType(String.class);
		editor.setOutputMarkupId(true);
		add(editor);

		setOutputMarkupId(true);

		add(new EasyUIBehavior());
	}

	// ------------------------------

	public String getCommandJSCall(String cmd) {
		return String.format("$('#%s').wCodeInput('%s');", editor.getMarkupId(), cmd);
	}

	@Override
	public void convertInput() {
		setConvertedInput(editor.getConvertedInput());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		if (options.getTheme() != null) {
			response.render(Resource.getCommonCSS(String.format("codemirror/theme/%s.css", options.getTheme())));
		}
		response.render(MAIN_CSS);
		response.render(MAIN_JS);
		response.render(JQ_JS);

		response.render(Resource.getCommonJS(String.format("codemirror/mode/%s.js", options.getMode().getJsFile())));

		if (options.getMode().isHasHint()) {
			response.render(Resource.getCommonCSS("codemirror/hint/show-hint.css"));
			response.render(Resource.getCommonJS("codemirror/hint/show-hint.js"));
			response.render(Resource.getCommonJS(String.format("codemirror/hint/%s-hint.js", options.getMode().getJsFile())));
		}

		if (options.getMatchBrackets() == null || options.getMatchBrackets()) {
			response.render(Resource.getCommonJS("codemirror/addon/matchbrackets.js"));
		}
	}

	// ------------------------------

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		editor.setModelObject(getModelObject());
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			if (!isEnabledInHierarchy()) {
				options.setEnabled(false);
			}

			String script = String.format("$('#%s').wCodeInput(%s);",
				editor.getMarkupId(),
				WebUtil.toJson(options));

			logger.debug("WCodeInput: {}", script);

			WebUtil.writeJQueryCall(script, true);
		}
	}
}

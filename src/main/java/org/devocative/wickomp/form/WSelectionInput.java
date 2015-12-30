package org.devocative.wickomp.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.resource.Resource;

import java.util.List;

public class WSelectionInput extends WFormInputPanel {
	private static final HeaderItem ADVANCED_LIST_JS = Resource.getCommonJS("advlist/advancedList.js");
	private static final HeaderItem ADVANCED_LIST_CSS = Resource.getCommonCSS("advlist/advancedList.css");

	private WSelectionList choices;
	private WebMarkupContainer opener;

	public WSelectionInput(String id, List choiceList, boolean multipleSelection) {
		this(id, null, choiceList, multipleSelection);
	}

	// Main Constructor
	public WSelectionInput(String id, IModel model, List choiceList, boolean multipleSelection) {
		super(id, model);

		choices = new WSelectionList("choices", new Model(), choiceList, multipleSelection);
		add(choices);

		add(opener = new WebMarkupContainer("opener"));

		setOutputMarkupId(true);
	}

	public boolean isMultipleSelection() {
		return choices.isMultipleSelection();
	}

	public WSelectionInput setMultipleSelection(boolean multipleSelection) {
		choices.setMultipleSelection(multipleSelection);
		return this;
	}

	public WSelectionInput addToChoices(Behavior... behaviors) {
		choices.add(behaviors);
		return this;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(Resource.getJQueryReference());
		response.render(ADVANCED_LIST_JS);
		response.render(ADVANCED_LIST_CSS);
	}

	@Override
	protected void convertInput() {
		setConvertedInput(choices.getConvertedInput());
	}

	@Override
	protected void onBeforeRender() {
		choices.setModelObject(getModelObject());
		if (!isEnabledInHierarchy())
			opener.add(new AttributeAppender("style", "background-color: #eeeeee;"));
		super.onBeforeRender();
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("handleAllAdvList('%s', %s, '%s', '%s');",
			getMarkupId(),
			isEnabledInHierarchy(),
			getString("label.select", null, "select"),
			getString("label.noOfSelection", null, "selection"));
		AjaxRequestTarget ajaxRequestTarget = getRequestCycle().find(AjaxRequestTarget.class);
		if (ajaxRequestTarget == null)
			getResponse().write(String.format("<script>%s</script>", script));
		else
			ajaxRequestTarget.appendJavaScript(script);
	}
}

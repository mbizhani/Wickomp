package org.devocative.wickomp.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.List;

public class WSelectionInput extends WFormInputPanel {
	private static final HeaderItem SEL_LIST_JS = Resource.getCommonJS("form/selList/selList.js");
	private static final HeaderItem SEL_LIST_CSS = Resource.getCommonCSS("form/selList/selList.css");

	private WSelectionList choices;
	private WebMarkupContainer opener;

	public WSelectionInput(String id, List choiceList, boolean multipleSelection) {
		this(id, null, choiceList, multipleSelection);
	}

	// Main Constructor
	public WSelectionInput(String id, IModel model, List choiceList, boolean multipleSelection) {
		super(id, model);

		choices = new WSelectionList("choices", new Model(), choiceList, multipleSelection);
		choices.setOutputMarkupId(true);
		add(choices);

		add(opener = new WebMarkupContainer("opener"));

		setOutputMarkupId(true);

		add(new FontAwesomeBehavior());
		add(new CommonBehavior());
	}

	// --------------------- ACCESSORS

	public boolean isMultipleSelection() {
		return choices.isMultipleSelection();
	}

	public WSelectionInput setMultipleSelection(boolean multipleSelection) {
		choices.setMultipleSelection(multipleSelection);
		return this;
	}

	public WSelectionInput setChoiceRenderer(IChoiceRenderer renderer) {
		choices.setChoiceRenderer(renderer);
		return this;
	}

	public WSelectionInput setChoices(List choiceList) {
		choices.setChoices(choiceList);
		return this;
	}

	// --------------------- METHODS

	public WSelectionInput addToChoices(Behavior... behaviors) {
		choices.add(behaviors);
		return this;
	}

	public WSelectionInput updateChoices(AjaxRequestTarget target, List choiceList) {
		setChoices(choiceList);
		target.add(choices);
		target.appendJavaScript(getHandleScript());
		return this;
	}

	// --------------------- INTERNAL METHODS

	@Override
	protected void onBeforeRender() {
		choices.setModelObject(getModelObject());

		if (!isEnabledInHierarchy()) {
			opener.add(new AttributeAppender("style", "background-color: #eeeeee;"));
		}

		super.onBeforeRender();
	}

	@Override
	protected void convertInput() {
		setConvertedInput(choices.getConvertedInput());
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		getResponse().write(String.format("<script>%s</script>", getHandleScript()));
	}

	private String getHandleScript() {
		return String.format("handleAllSelList('%s', %s, '%s', '%s');",
			getMarkupId(),
			isEnabledInHierarchy(),
			getString("label.select", null, "select"),
			getString("label.noOfSelection", null, "selection"));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);
		response.render(SEL_LIST_JS);
		response.render(SEL_LIST_CSS);
	}
}

package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.Collection;
import java.util.List;

public class WSelectionInput extends WLabeledFormInputPanel {
	private static final long serialVersionUID = 2363610394629306064L;

	private static final HeaderItem SEL_LIST_JS = Resource.getCommonJS("form/selList/selList.js");
	private static final HeaderItem SEL_LIST_CSS = Resource.getCommonCSS("form/selList/selList.css");

	private WSelectionList choices;
	private WebMarkupContainer opener;
	private WebComponent title;

	// ------------------------------

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
		opener.add(title = new WebComponent("title"));

		setOutputMarkupId(true);

		add(new FontAwesomeBehavior());
		add(new CommonBehavior());
	}

	// ------------------------------

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

	// ------------------------------

	public WSelectionInput addToChoices(Behavior... behaviors) {
		choices.add(behaviors);
		return this;
	}

	public WSelectionInput updateChoices(AjaxRequestTarget target, List choiceList) {
		setChoices(choiceList);
		target.add(this);
		target.appendJavaScript(getHandleScript());
		return this;
	}

	// ------------------------------

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);
		response.render(SEL_LIST_JS);
		response.render(SEL_LIST_CSS);
	}

	@Override
	public void convertInput() {
		setConvertedInput(choices.getConvertedInput());
	}

	// ------------------------------

	@Override
	protected void onBeforeRender() {
		choices.setModelObject(getModelObject());

		if (!isEnabledInHierarchy()) {
			opener.add(new AttributeAppender("style", "background-color: #eeeeee;"));
		}

		// The following snippet is added for input:reset HTML support in this component
		String cap;
		if (getModelObject() == null) {
			cap = getString("WSelectionInput.select");
		} else if (choices.isMultipleSelection()) {
			Collection col = (Collection) getModelObject();
			cap = String.format("%s %s", col.size(), getString("WSelectionInput.noOfSelection"));
		} else {
			int i = choices.getChoices().indexOf(getModelObject());
			if (i > -1) {
				cap = choices.getChoices().get(i).toString();
			} else {
				cap = getString("WSelectionInput.select");
			}
		}

		title.add(new AttributeModifier("value", cap));

		super.onBeforeRender();
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			WebUtil.writeJQueryCall(getHandleScript(), false);
		}
	}

	// ------------------------------

	private String getHandleScript() {
		return String.format("handleAllSelList('%s', %s, '%s', '%s');",
			getMarkupId(),
			isEnabledInHierarchy(),
			getString("WSelectionInput.select"),
			getString("WSelectionInput.noOfSelection"));
	}
}

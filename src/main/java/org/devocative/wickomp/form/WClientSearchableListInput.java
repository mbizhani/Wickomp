package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.html.window.OModalWindow;
import org.devocative.wickomp.html.window.WModalWindow;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class WClientSearchableListInput<T> extends WFormInputPanel<List<T>> {
	private static final HeaderItem SEL_LIST_JS = Resource.getCommonJS("form/selList/selList.js");
	private static final HeaderItem SEL_LIST_CSS = Resource.getCommonJS("form/selList/selList.css");
	private static final HeaderItem JS = Resource.getCommonJS("form/clientSearchList/clientSearchList.js");

	private Label label;
	private WModalWindow modalWindow;
	private WebMarkupContainer result;
	private WebComponent title;

	// ---------------------------- CONSTRUCTORS

	public WClientSearchableListInput(String id) {
		this(id, null);
	}

	// Main Constructor
	public WClientSearchableListInput(String id, IModel<List<T>> model) {
		super(id, model);

		add(label = new Label("label"));

		modalWindow = new WModalWindow("modalWindow");
		add(modalWindow);

		result = new WebMarkupContainer("result");
		result.setOutputMarkupId(true);
		add(result);

		add(title = new WebComponent("title"));
		title.setOutputMarkupId(true);

		add(new AjaxLink("openModal") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow
					.setContent(createSelectionPanel(modalWindow.getContentId()))
					.show(target);
			}
		});

		setOutputMarkupId(true);

		add(new FontAwesomeBehavior());
		add(new CommonBehavior());
	}

	// ---------------------------- ACCESSORS

	public OModalWindow getModalWindowOptions() {
		return modalWindow.getOptions();
	}

	public WClientSearchableListInput<T> setLabelVisible(boolean visible) {
		label.setVisible(visible);
		return this;
	}

	// ---------------------------- ABSTRACT METHODS

	protected abstract Component createSelectionPanel(String selectionPanelId);

	protected abstract T createServerObject(String key);

	// ---------------------------- PUBLIC METHODS

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(SEL_LIST_CSS);
		response.render(SEL_LIST_JS);

		response.render(JS);
	}

	@Override
	public void convertInput() {
		List<T> list = new ArrayList<>();
		String[] inputAsArray = getInputAsArray();
		if (inputAsArray != null) {
			for (String input : inputAsArray) {
				list.add(createServerObject(input));
			}
		}

		if (list.size() > 0) {
			setConvertedInput(list);
		} else {
			setConvertedInput(null);
		}
	}

	// ---------------------------- PROTECTED METHODS

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
		title.add(new AttributeModifier("value", "0"));

		super.onBeforeRender();
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("initClientSearchableList('%s');", getMarkupId());

		WebUtil.writeJQueryCall(script, false);
	}

	protected String getJSCallback() {
		return String.format("function(rows){handleClientSearchableList('%s', '%s', '%s', '%s', rows);}",
			modalWindow.getContainerMarkupId(),
			getInputName(),
			result.getMarkupId(),
			title.getMarkupId());
	}
}

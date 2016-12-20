package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.vo.KeyValueVO;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.html.window.OModalWindow;
import org.devocative.wickomp.html.window.WModalWindow;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class WClientSearchableListInput extends WLabeledFormInputPanel {
	private static final long serialVersionUID = 8131275251442491717L;

	private static final HeaderItem SEL_LIST_CSS = Resource.getCommonCSS("form/selList/selList.css");
	private static final HeaderItem SEL_LIST_JS = Resource.getCommonJS("form/selList/selList.js");
	private static final HeaderItem JS = Resource.getCommonJS("form/clientSearchList/clientSearchList.js");

	private WModalWindow modalWindow;
	private WebMarkupContainer result;
	private WebComponent title;
	private AjaxLink openModal;

	private boolean multipleSelection;

	// ---------------------------- CONSTRUCTORS

	public WClientSearchableListInput(String id, boolean multipleSelection) {
		this(id, null, multipleSelection);
	}

	// Main Constructor
	public WClientSearchableListInput(String id, IModel model, boolean multipleSelection) {
		super(id, model);

		this.multipleSelection = multipleSelection;

		modalWindow = new WModalWindow("modalWindow");
		add(modalWindow);

		result = new WebMarkupContainer("result");
		result.setOutputMarkupId(true);
		add(result);

		add(title = new WebComponent("title"));
		title.setOutputMarkupId(true);

		add(openModal = new AjaxLink("openModal") {
			private static final long serialVersionUID = 3599836029322960165L;

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

	public WClientSearchableListInput setOpenModalLinkVisible(boolean visible) {
		openModal.setVisible(visible);
		return this;
	}

	// ---------------------------- ABSTRACT METHODS

	protected abstract Component createSelectionPanel(String selectionPanelId);

	protected abstract Object createServerObject(String key);

	protected abstract List<KeyValueVO<String, String>> createClientOptions(List list);

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
		List list = new ArrayList<>();
		String[] inputAsArray = getInputAsArray();
		if (inputAsArray != null) {
			for (String input : inputAsArray) {
				list.add(createServerObject(input));
			}
		}

		if (list.size() > 0) {
			if (multipleSelection) {
				setConvertedInput(list);
			} else {
				setConvertedInput(list.get(0));
			}
		} else {
			setConvertedInput(null);
		}
	}

	// ---------------------------- PROTECTED METHODS

	@Override
	protected void onBeforeRender() {
		int size = 0;
		if (getModelObject() != null) {
			if (multipleSelection) {
				size = ((List) getModelObject()).size();
			} else {
				size = 1;
			}
		}
		title.add(new AttributeModifier("value", size));

		super.onBeforeRender();
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("initClientSearchableList('%s');", getMarkupId()));

		List objectRows;
		Object modelObject = getModelObject();
		if (modelObject != null) {
			if (multipleSelection) {
				objectRows = (List) modelObject;
			} else {
				objectRows = Collections.singletonList(modelObject);
			}
		} else {
			objectRows = Collections.emptyList();
		}

		String rows = WebUtil.toJson(createClientOptions(objectRows));
		builder.append(String.format(
				"handleClientSearchableList(null, '%s', '%s', '%s', %s, %s);",
				getInputName(),
				result.getMarkupId(),
				title.getMarkupId(),
				rows,
				multipleSelection
			)
		);

		WebUtil.writeJQueryCall(builder.toString(), false);
	}

	protected String getJSCallback() {
		return String.format("function(rows){handleClientSearchableList('%s', '%s', '%s', '%s', rows, %s);}",
			modalWindow.getContainerMarkupId(),
			getInputName(),
			result.getMarkupId(),
			title.getMarkupId(),
			multipleSelection);
	}
}

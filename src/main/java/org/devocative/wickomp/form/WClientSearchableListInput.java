package org.devocative.wickomp.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.html.window.WModalWindow;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class WClientSearchableListInput<T> extends WFormInputPanel<List<T>> {
	private static final HeaderItem JS = Resource.getCommonJS("form/clientSearchList/clientSearchList.js");

	private WModalWindow modalWindow;
	private WebMarkupContainer result;

	public WClientSearchableListInput(String id) {
		this(id, null);
	}

	// Main Constructor
	public WClientSearchableListInput(String id, IModel<List<T>> model) {
		super(id, model);

		modalWindow = new WModalWindow("modalWindow");
		add(modalWindow);

		result = new WebMarkupContainer("result");
		result.setOutputMarkupId(true);
		add(result);

		add(new AjaxLink("openModal") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow
					.setContent(createSelectionPanel(modalWindow.getContentId()))
					.show(target);
			}
		});

		add(new FontAwesomeBehavior());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(JS);
	}

	@Override
	protected void convertInput() {
		List<T> list = new ArrayList<>();
		String[] inputAsArray = getInputAsArray();
		if (inputAsArray != null) {
			for (String input : inputAsArray) {
				list.add(createServerObject(input));
			}
		}

		setConvertedInput(list);
	}

	protected abstract Component createSelectionPanel(String selectionPanelId);

	protected abstract T createServerObject(String key);

	protected String getJSCallback() {
		return String.format("function(rows){handleClientSearchableList('%s', '%s', '%s', rows);}",
			modalWindow.getContainerMarkupId(),
			getInputName(),
			result.getMarkupId());
	}
}

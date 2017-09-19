package org.devocative.wickomp.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WFormInputPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.CommonBehavior;
import org.devocative.wickomp.wrcs.FontAwesomeBehavior;
import org.devocative.wickomp.wrcs.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WOrderedListInput<T> extends WFormInputPanel<List<T>> {
	private static final long serialVersionUID = -2747619925611038307L;

	private static final HeaderItem ORDERED_LIST_JS = Resource.getCommonJS("form/orderedList.js");

	// ------------------------------

	private List<T> srcOptions = new ArrayList<T>();
	private List<T> destOptions = new ArrayList<T>();
	private List<T> options;
	private WebMarkupContainer src, dest;
	private HiddenField<String> hiddenIds;
	private WebMarkupContainer up, down;
	private String idDelimiter = "~";
	private int visibleSize = 10;

	// ------------------------------

	public WOrderedListInput(String id, List<T> options) {
		this(id, null, options);
	}

	// Main Constructor
	public WOrderedListInput(String id, IModel<List<T>> iModel, final List<T> options) {
		super(id, iModel);

		this.options = options;

		hiddenIds = new HiddenField<>("hiddenIds", new Model<>());
		hiddenIds.setOutputMarkupId(true);
		add(hiddenIds);

		src = new WebMarkupContainer("src");
		src.setOutputMarkupId(true);
		src.add(new ListView<T>("options", srcOptions) {
			private static final long serialVersionUID = -5104007033256708377L;

			@Override
			protected void populateItem(ListItem<T> listItem) {
				T object = listItem.getModelObject();
				Label option = new Label("option", object.toString());
				option.add(new AttributeAppender("value", options.indexOf(object)));
				listItem.add(option);
			}
		});
		add(src);

		dest = new WebMarkupContainer("dest");
		dest.setOutputMarkupId(true);
		dest.add(new ListView<T>("options", destOptions) {
			private static final long serialVersionUID = 3246291398601578643L;

			@Override
			protected void populateItem(ListItem<T> listItem) {
				T object = listItem.getModelObject();
				Label option = new Label("option", object.toString());
				option.add(new AttributeAppender("value", options.indexOf(object)));
				listItem.add(option);
			}
		});
		add(dest);

		up = new WebMarkupContainer("up");
		up.setOutputMarkupId(true);
		add(up);

		down = new WebMarkupContainer("down");
		down.setOutputMarkupId(true);
		add(down);

		add(new FontAwesomeBehavior());
		add(new CommonBehavior());
	}

	// ------------------------------

	public WOrderedListInput<T> setIdDelimiter(String idDelimiter) {
		this.idDelimiter = idDelimiter;
		return this;
	}

	public WOrderedListInput<T> setVisibleSize(int visibleSize) {
		this.visibleSize = visibleSize;
		return this;
	}

	// ------------------------------

	@Override
	public void convertInput() {
		List<T> result = new ArrayList<T>();

		String convertedInput = hiddenIds.getConvertedInput();
		if (convertedInput != null) {
			String[] parts = convertedInput.split(idDelimiter);
			for (String part : parts) {
				if (part != null && part.length() > 0) {
					int idx = Integer.parseInt(part);
					if (idx > -1) {
						result.add(options.get(idx));
					}
				}
			}
		}

		if (result.size() > 0) {
			setConvertedInput(result);
		} else {
			setConvertedInput(null);
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);
		response.render(ORDERED_LIST_JS);
	}

	// ------------------------------


	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (isEnabled()) {
			up.add(new AttributeAppender("style", "cursor: pointer;"));
			down.add(new AttributeAppender("style", "cursor: pointer;"));
		} else {
			src.add(new AttributeAppender("disabled", "true"));
			dest.add(new AttributeAppender("disabled", "false"));
		}
		src.add(new AttributeModifier("size", String.valueOf(visibleSize)));
		dest.add(new AttributeModifier("size", String.valueOf(visibleSize)));
	}

	@Override
	protected void onBeforeRender() {
		srcOptions.clear();
		destOptions.clear();

		srcOptions.addAll(options);

		List<T> list = getModelObject();
		if (list != null && list.size() > 0) {
			StringBuilder hiddenValue = new StringBuilder();
			List<Integer> toBeRemoved = new ArrayList<>();
			for (T obj : list) {
				int idx = options.indexOf(obj);
				if (idx > -1) {
					hiddenValue.append(idx).append(idDelimiter);
					destOptions.add(obj);
					toBeRemoved.add(idx);
				}
			}
			Collections.sort(toBeRemoved);
			Collections.reverse(toBeRemoved);
			toBeRemoved.forEach(srcOptions::remove);
			hiddenIds.setModel(new Model<>(hiddenValue.toString()));
		}

		super.onBeforeRender();
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible() && isEnabled()) {
			String script = String.format("handleOrderedList('%s', '%s', '%s', '%s', '%s', '%s');",
				hiddenIds.getMarkupId(), idDelimiter,
				src.getMarkupId(), dest.getMarkupId(), up.getMarkupId(), down.getMarkupId());

			WebUtil.writeJQueryCall(script, false);
		}
	}
}


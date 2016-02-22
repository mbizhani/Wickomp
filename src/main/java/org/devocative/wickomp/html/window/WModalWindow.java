package org.devocative.wickomp.html.window;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

public class WModalWindow extends WPanel {
	private Component content;
	private WebMarkupContainer container;

	private OModalWindow options;

	public WModalWindow(String id) {
		this(id, new OModalWindow());
	}

	// Main Constructor
	public WModalWindow(String id, OModalWindow options) {
		super(id);
		this.options = options;

		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		container.add(content = new WebMarkupContainer("content").setVisible(false));

		add(new EasyUIBehavior());
	}

	public String getContentId() {
		return "content";
	}

	public final WModalWindow setContent(Component component) {
		if (!component.getId().equals(getContentId())) {
			throw new WicketRuntimeException("Modal window content id is wrong. Component ID:" +
				component.getId() + "; content ID: " + getContentId());
		}
		container.replace(content = component.setVisible(false));
		return this;
	}

	public final WModalWindow show(AjaxRequestTarget target) {
		content.setVisible(true);
		target.add(container);
		String opt = JsonUtil.toJson(options);
		String script = String.format("$('#%s').window(%s);", container.getMarkupId(), opt);
		target.appendJavaScript(script);
		return this;
	}


}

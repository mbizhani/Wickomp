package org.devocative.wickomp.html.window;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

public class WModalWindow extends WPanel {
	private Component content;
	private WebMarkupContainer container;
	private AbstractDefaultAjaxBehavior callbackAjaxBehavior;

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

		callbackAjaxBehavior = new AbstractDefaultAjaxBehavior() {
			@Override
			protected void respond(AjaxRequestTarget target) {
				content.setVisible(false);

				onClose(target);
			}
		};

		add(callbackAjaxBehavior);

		add(new EasyUIBehavior());
	}

	public String getContentId() {
		return "content";
	}

	public String getContainerMarkupId() {
		return container.getMarkupId();
	}

	public OModalWindow getOptions() {
		return options;
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
		return show(null, null, target);
	}

	public final WModalWindow show(Component component, AjaxRequestTarget target) {
		return show(component, null, target);
	}

	// Main show()
	public final WModalWindow show(Component component, IModel<String> title, AjaxRequestTarget target) {
		if (component != null) {
			setContent(component);
		}

		if (title != null) {
			options.setTitle(title.getObject());
		}

		options.setUrl(callbackAjaxBehavior.getCallbackUrl().toString());
		content.setVisible(true);
		target.add(container);
		target.appendJavaScript(String.format("$('#%s').window(%s);",
			getContainerMarkupId(),
			WebUtil.toJson(options)));
		return this;
	}

	protected void onClose(AjaxRequestTarget target) {
	}
}

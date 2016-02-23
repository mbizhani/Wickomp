package org.devocative.wickomp.html.window;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.devocative.wickomp.WCallbackComponent;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

public class WModalWindow extends WCallbackComponent {
	private Component content;
	private WebMarkupContainer container;

	private OModalWindow options;

	public WModalWindow(String id) {
		this(id, new OModalWindow());
	}

	// Main Constructor
	public WModalWindow(String id, OModalWindow options) {
		super(id, options);
		this.options = options;

		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		container.add(content = new WebMarkupContainer("content").setVisible(false));

		setNeedHtmlBeside(true);
		setIsAutoJSRender(false);

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

		content.setVisible(true);
		target.add(container);
		target.appendJavaScript(getJQueryCall());
		return this;
	}

	public String getContainerMarkupId() {
		return container.getMarkupId();
	}

	protected void onClose(AjaxRequestTarget target) {
	}

	@Override
	protected String getJQueryFunction() {
		return "window";
	}

	@Override
	protected void onRequest(IRequestParameters parameters) {
		content.setVisible(false);

		onClose(createAjaxResponse());
	}
}

package org.devocative.wickomp.html.window;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.HeaderBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WWindow extends WPanel {
	private static final long serialVersionUID = -3725902010253369642L;
	private static final Logger logger = LoggerFactory.getLogger(WWindow.class);

	private Component content;
	private WebMarkupContainer container;
	private AbstractDefaultAjaxBehavior callbackAjaxBehavior;

	private OWindow options;

	// ------------------------------

	public WWindow(String id) {
		this(id, new OWindow());
	}

	// Main Constructor
	public WWindow(String id, OWindow options) {
		super(id);
		this.options = options;

		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		container.add(content = checkContentOnAdd(new WebMarkupContainer("content")));

		callbackAjaxBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 8334609333513108892L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				content.setVisible(false);

				onClose(target);
			}
		};

		add(callbackAjaxBehavior);

		add(new HeaderBehavior("main/wWindow.js").setNeedEasyUI(true));
	}

	// ------------------------------

	public String getContentId() {
		return "content";
	}

	public String getContainerMarkupId() {
		return container.getMarkupId();
	}

	public OWindow getOptions() {
		return options;
	}

	public final WWindow setContent(Component component) {
		if (!component.getId().equals(getContentId())) {
			throw new WicketRuntimeException("Modal window content id is wrong. Component ID:" +
				component.getId() + "; content ID: " + getContentId());
		}
		container.replace(content = checkContentOnAdd(component));
		return this;
	}

	// ------------------------------

	public final WWindow show(AjaxRequestTarget target) {
		return show(null, null, target);
	}

	public final WWindow show(Component component, AjaxRequestTarget target) {
		return show(component, null, target);
	}

	public final WWindow show(IModel<String> title, AjaxRequestTarget target) {
		return show(null, title, target);
	}

	// Main show()
	public final WWindow show(Component component, IModel<String> title, AjaxRequestTarget target) {
		if (component != null) {
			setContent(component);
		}

		String oldTitle = options.getTitle();
		if (title != null) {
			options.setTitle(title.getObject());
		}

		options.setUrl(callbackAjaxBehavior.getCallbackUrl().toString());
		content.setVisible(true);
		target.add(container);

		String script = String.format("$('#%s').wWindow(%s);",
			getContainerMarkupId(),
			WebUtil.toJson(options));

		options.setTitle(oldTitle);

		logger.debug("WWindow.show: {}", script);

		target.appendJavaScript(script);
		return this;
	}

	public void close(AjaxRequestTarget target) {
		target.appendJavaScript(String.format("$('#%s').wWindow('close');", getContainerMarkupId()));
	}

	// ------------------------------

	public static boolean closeParentWindow(Component component, AjaxRequestTarget target) {
		WWindow parent = component.findParent(WWindow.class);
		if (parent != null) {
			parent.close(target);
			return true;
		}

		return false;
	}

	// ------------------------------

	protected void onClose(AjaxRequestTarget target) {
	}

	// ------------------------------

	private Component checkContentOnAdd(Component content) {
		content.setVisible(false);
		return content;
	}
}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WModalWindow extends WPanel {
	private static final long serialVersionUID = 8033724982656671879L;
	private static final Logger logger = LoggerFactory.getLogger(WModalWindow.class);

	private Component content;
	private WebMarkupContainer container;
	private AbstractDefaultAjaxBehavior callbackAjaxBehavior;

	private OModalWindow options;
	private boolean closeOnEscape = true;

	// ------------------------------

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
			private static final long serialVersionUID = 8334609333513108892L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				content.setVisible(false);

				onClose(target);
			}
		};

		add(callbackAjaxBehavior);

		add(new EasyUIBehavior());
	}

	// ------------------------------

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

	public WModalWindow setCloseOnEscape(boolean closeOnEscape) {
		this.closeOnEscape = closeOnEscape;
		return this;
	}

	// ------------------------------

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

		String script = String.format("$('#%s').window(%s)",
			getContainerMarkupId(),
			WebUtil.toJson(options));

		if (closeOnEscape) {
			script += String.format(".attr('tabindex','-1').focus().keydown(function(e){if(e.which == 27) $('#%s').window('close');})", getContainerMarkupId());
		}

		script += ";";

		logger.debug("WModalWindow.show: {}", script);

		target.appendJavaScript(script);
		return this;
	}

	public void close(AjaxRequestTarget target) {
		target.appendJavaScript(String.format("$('#%s').window('close');", getContainerMarkupId()));
	}

	// ------------------------------

	public static boolean closeParentWindow(Component component, AjaxRequestTarget target) {
		WModalWindow parent = component.findParent(WModalWindow.class);
		if(parent != null) {
			parent.close(target);
			return true;
		}

		return false;
	}

	// ------------------------------

	protected void onClose(AjaxRequestTarget target) {
	}
}

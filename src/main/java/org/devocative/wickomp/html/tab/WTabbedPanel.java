package org.devocative.wickomp.html.tab;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.ObjectUtil;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.wrcs.MainBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WTabbedPanel extends WPanel {
	private static final long serialVersionUID = -6158080713080205336L;
	private static final Logger logger = LoggerFactory.getLogger(WTabbedPanel.class);

	public static final String TAB_ID = "tab";

	// ------------------------------

	private RepeatingView tabs;
	private WebMarkupContainer tabbedPanel;
	private AbstractDefaultAjaxBehavior callbackAjaxBehavior;

	private List<OTab> oTabList = new ArrayList<>();
	private transient List<Component> tabsList = new ArrayList<>();

	// ------------------------------

	public WTabbedPanel(String id) {
		super(id);

		setOutputMarkupId(true);
		add(new MainBehavior());
	}

	// ------------------------------

	public WTabbedPanel addTab(Component tab, IModel<String> title) {
		return addTab(tab, new OTab(title, false));
	}

	public WTabbedPanel addTab(Component tab, OTab oTab) {
		tabsList.add(tab);
		oTabList.add(oTab);
		return this;
	}

	public WTabbedPanel addTab(AjaxRequestTarget target, Component tab, IModel<String> title) {
		return addTab(target, tab, new OTab(title, false));
	}

	public WTabbedPanel addTab(AjaxRequestTarget target, Component tab, OTab oTab) {
		oTabList.add(oTab);

		WebMarkupContainer newTab = appendTab(tab, oTab);
		StringBuilder opt = new StringBuilder();
		opt.append("{")
			.append("htmlId:'").append(newTab.getMarkupId()).append("'")
			.append(",title:'").append(oTab.getTitle().getObject()).append("'");
		if (ObjectUtil.isTrue(oTab.getClosable())) {
			opt.append(",closable:").append(oTab.getClosable());
		}
		opt.append("}");
		target.prependJavaScript(
			String.format("$('#%s').wTabbedPanel('add', %s);",
				tabbedPanel.getMarkupId(),
				opt.toString()));
		target.add(newTab);

		return this;
	}

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		tabbedPanel = new WebMarkupContainer("tabbedPanel");
		tabbedPanel.setOutputMarkupId(true);
		add(tabbedPanel);

		tabs = new RepeatingView("tabs");
		for (int i = 0; i < oTabList.size(); i++) {
			appendTab(tabsList.get(i), oTabList.get(i));
		}
		tabbedPanel.add(tabs);

		callbackAjaxBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 7824230483172079039L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				Integer index = getRequest().getRequestParameters().getParameterValue("index").toOptionalInteger();
				logger.debug("WTabbedPanel.TabClosed: index=[{}]", index);
				if (index != null) {
					OTab removed = oTabList.remove(index.intValue());
					logger.debug("WTabbedPanel.TabClosed: removed=[{}] oTabList={}", removed, oTabList);
				}
			}
		};
		add(callbackAjaxBehavior);
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("$('#%s').wTabbedPanel({url:'%s'});",
			tabbedPanel.getMarkupId(),
			callbackAjaxBehavior.getCallbackUrl());
		WebUtil.writeJQueryCall(script, true);
	}

	// ------------------------------

	private WebMarkupContainer appendTab(Component tab, OTab oTab) {
		WebMarkupContainer tabContainer = new WebMarkupContainer(tabs.newChildId());
		tabContainer.add(new AttributeModifier("title", oTab.getTitle()));
		if (ObjectUtil.isTrue(oTab.getClosable())) {
			tabContainer.add(new AttributeModifier("closable", "true"));
		}
		tabContainer.add(tab);
		tabs.add(tabContainer);
		return tabContainer;
	}
}

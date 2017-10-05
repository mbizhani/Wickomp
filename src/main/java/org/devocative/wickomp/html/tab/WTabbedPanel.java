package org.devocative.wickomp.html.tab;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.devocative.adroit.ObjectUtil;
import org.devocative.wickomp.WJqCallbackPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.wrcs.HeaderBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WTabbedPanel extends WJqCallbackPanel {
	private static final long serialVersionUID = -6158080713080205336L;
	private static final String TAB_HTML_CLASS = "w-tab";
	private static final Logger logger = LoggerFactory.getLogger(WTabbedPanel.class);

	// ------------------------------

	private RepeatingView tabs;

	private OTabbedPanel oTabbedPanel;
	private List<OTab> oTabList = new ArrayList<>();
	private transient List<Component> tabsList = new ArrayList<>();

	// ------------------------------

	public WTabbedPanel(String id) {
		this(id, new OTabbedPanel());
	}

	// Main Constructor
	public WTabbedPanel(String id, OTabbedPanel oTabbedPanel) {
		super(id, oTabbedPanel);

		this.oTabbedPanel = oTabbedPanel;

		add(new HeaderBehavior("main/wTabbedPanel.js").setNeedEasyUI(true));
	}

	// ------------------------------

	public String getTabContentId() {
		return "tab";
	}

	public OTabbedPanel getOptions() {
		return oTabbedPanel;
	}

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

		WebMarkupContainer newTab = appendTab(tab);
		oTab.setHtmlId(newTab.getMarkupId());

		target.prependJavaScript(
			String.format("$('#%s').wTabbedPanel('add', %s);",
				getMarkupId(),
				WebUtil.toJson(oTab)
			)
		);
		target.add(newTab);

		return this;
	}

	// ------------------------------

	protected List<OTab> getTabList() {
		return Collections.unmodifiableList(oTabList);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		tabs = new RepeatingView("tabs");
		for (int i = 0; i < oTabList.size(); i++) {
			appendTab(tabsList.get(i), oTabList.get(i));
		}
		add(tabs);

		if (oTabbedPanel.getTabPosition() != null && oTabbedPanel.getTabPosition() == OTabbedPanel.OPosition.side) {
			oTabbedPanel.setTabPosition(
				getUserPreference().getLayoutDirection() == OLayoutDirection.LTR ?
					OTabbedPanel.OPosition.left :
					OTabbedPanel.OPosition.right);
		}
	}

	@Override
	protected void onRequest() {
		Integer index = getRequest().getRequestParameters().getParameterValue("index").toOptionalInteger();
		logger.debug("WTabbedPanel.TabClosed: index=[{}]", index);
		if (index != null) {
			OTab removed = oTabList.remove(index.intValue());
			logger.debug("WTabbedPanel.TabClosed: removed=[{}] oTabList={}", removed, oTabList);
			AjaxRequestTarget target = createAjaxResponse();
			onTabClose(target, removed);
		} else {
			sendJSONResponse("");
		}
	}

	@Override
	protected String getJQueryFunction() {
		return "wTabbedPanel";
	}

	protected void onTabClose(AjaxRequestTarget target, OTab closedTab) {
	}

	// ------------------------------

	private WebMarkupContainer appendTab(Component tab) {
		return appendTab(tab, null);
	}

	private WebMarkupContainer appendTab(Component tab, OTab oTab) {
		WebMarkupContainer tabContainer = new WebMarkupContainer(tabs.newChildId());
		tabContainer.add(new AttributeModifier("class", TAB_HTML_CLASS));
		if (oTab != null) {
			tabContainer.add(new AttributeModifier("title", oTab.getTitle()));
			if (ObjectUtil.isTrue(oTab.getClosable())) {
				tabContainer.add(new AttributeModifier("closable", "true"));
			}
		}
		tabContainer.add(tab);
		tabs.add(tabContainer);
		return tabContainer;
	}
}

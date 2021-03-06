package org.devocative.wickomp.demo.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.demo.panel.SelectionPanel;
import org.devocative.wickomp.html.WAjaxLink;
import org.devocative.wickomp.html.tab.OTab;
import org.devocative.wickomp.html.tab.OTabbedPanel;
import org.devocative.wickomp.html.tab.WTabbedPanel;

import java.util.UUID;

public class TabbedPage extends BasePage {
	private static final long serialVersionUID = -3655657275664796120L;

	private WTabbedPanel tabs;

	private int no = 1;

	public TabbedPage() {
		add(new WAjaxLink("addTab") {
			private static final long serialVersionUID = -1461377260349174102L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				no++;
				if (no % 3 != 0) {
					tabs.addTab(target, new Label(tabs.getTabContentId(), UUID.randomUUID().toString()), new OTab(new Model<>("T-" + no), true));
				} else {
					tabs.addTab(target, new SelectionPanel(tabs.getTabContentId()), new Model<>("T-" + no));
				}

				/*
				tabs.addTab(new OTab(new Label(WTabbedPanel.TAB_ID, UUID.randomUUID().toString()), new Model<>("T-" + no)).setClosable(true));
				target.add(tabs);
				*/
			}
		});

		OTabbedPanel oTabbedPanel = new OTabbedPanel();
		oTabbedPanel
			.setOnSelect("function(title,index){console.log('OnSelect', index);}")
			.setGlobalHotkeyEnabled(true)
		;
		tabs = new WTabbedPanel("tabs", oTabbedPanel);
		tabs.addTab(new Label(tabs.getTabContentId(), "Salam"), new Model<>("A"));
		tabs.addTab(new Label(tabs.getTabContentId(), "How r u?"), new Model<>("B"));

		add(tabs);
	}
}

package org.devocative.wickomp.html.menu;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.opt.OUserPreference;
import org.devocative.wickomp.wrcs.Resource;

import java.util.List;

public class WMenuBar extends WPanel {
	private static final long serialVersionUID = -2558846609610072069L;

	private static final HeaderItem JS = Resource.getCommonJS("menu/jquery.smartmenus.min.js");
	private static final HeaderItem CSS_CORE = Resource.getCommonCSS("menu/sm-core-css.css");
	//	private static final HeaderItem CSS_THEME = Resource.getCommonCSS("menu/sm-clean.css");
	private static final HeaderItem CSS_THEME = Resource.getCommonCSS("menu/sm-mint.css");

	private static final HeaderItem CSS_EXT = Resource.getCommonCSS("menu/sm-ext.css");
	private static final HeaderItem JS_EXT = Resource.getCommonJS("menu/sm-ext.js");

	// ------------------------------

	private MenuItemFragment rootMenu;
	private boolean contextRelativeLink = false;
	private boolean enableToggleButton = true;

	// ------------------------------

	public WMenuBar(String id, List<OMenuItem> menuItems) {
		super(id);
		rootMenu = new MenuItemFragment("menu", "menuItemFragment", this, menuItems);
		rootMenu.setOutputMarkupId(true);
		add(rootMenu);
	}

	// ------------------------------

	public boolean isContextRelativeLink() {
		return contextRelativeLink;
	}

	public WMenuBar setContextRelativeLink(boolean contextRelativeLink) {
		this.contextRelativeLink = contextRelativeLink;
		return this;
	}

	public boolean isEnableToggleButton() {
		return enableToggleButton;
	}

	public WMenuBar setEnableToggleButton(boolean enableToggleButton) {
		this.enableToggleButton = enableToggleButton;
		return this;
	}

	// ------------------------------

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(CSS_CORE);
		response.render(CSS_THEME);

		response.render(JS);

		if (enableToggleButton) {
			response.render(CSS_EXT);
			response.render(JS_EXT);
		}
	}

	// ------------------------------

	@Override
	protected void onInitialize() {
		super.onInitialize();

		OUserPreference userPreference = getUserPreference();

		if (OLayoutDirection.RTL.equals(userPreference.getLayoutDirection())) {
			rootMenu.add(new AttributeModifier("class", "sm sm-rtl sm-mint"));
			if (enableToggleButton) {
				add(new AttributeModifier("style", "text-align:left"));
			}
		} else {
			rootMenu.add(new AttributeModifier("class", "sm sm-mint"));
		}

		add(new WebMarkupContainer("toggleButton").setVisible(enableToggleButton));
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			String script = String.format("$(\"#%s\").smartmenus();", rootMenu.getMarkupId());

			WebUtil.writeJQueryCall(script, false);
		}
	}

	protected Component newMenuItemLink(String compId, OMenuItem item) {
		return new ExternalLink(compId, item.getHref(), item.getLabel().getObject())
			.setContextRelative(contextRelativeLink);
	}

	// ------------------------------

	private class MenuItemFragment extends Fragment {
		private static final long serialVersionUID = 1870498696384296613L;

		public MenuItemFragment(String id, String markupId, MarkupContainer markupProvider, List<OMenuItem> menuItems) {
			super(id, markupId, markupProvider);

			add(new ListView<OMenuItem>("menuItem", menuItems) {
				private static final long serialVersionUID = 8524413741007595434L;

				@Override
				protected void populateItem(ListItem<OMenuItem> item) {
					final OMenuItem menuItem = item.getModelObject();
					item.add(newMenuItemLink("link", menuItem));

					MenuItemFragment fragment = new MenuItemFragment("subMenuItem", "menuItemFragment", WMenuBar.this, menuItem.getSubMenus());
					fragment.setVisible(menuItem.getSubMenus().size() > 0);
					item.add(fragment);
				}
			});
		}
	}
}

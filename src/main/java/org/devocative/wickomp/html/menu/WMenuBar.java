package org.devocative.wickomp.html.menu;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.html.WExternalLink;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.opt.OUserPreference;
import org.devocative.wickomp.wrcs.Resource;

import java.util.List;

public class WMenuBar extends WPanel {
	private static final HeaderItem JS = Resource.getCommonJS("menu/jquery.smartmenus.min.js");
	private static final HeaderItem CSS_CORE = Resource.getCommonCSS("menu/sm-core-css.css");
	private static final HeaderItem CSS_CLEAN = Resource.getCommonCSS("menu/sm-clean.css");

	private MenuItemFragment rootMenu;

	public WMenuBar(String id, List<OMenuItem> menuItems) {
		super(id);
		rootMenu = new MenuItemFragment("menu", "menuItemFragment", this, menuItems);
		rootMenu.setOutputMarkupId(true);
		add(rootMenu);
	}

	protected Component newMenuItemLink(String compId, OMenuItem item) {
		return new WExternalLink(compId, item.getLabel(), item.getHref());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		Resource.addJQueryReference(response);

		response.render(CSS_CORE);
		response.render(CSS_CLEAN);

		response.render(JS);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		OUserPreference userPreference = getUserPreference();

		if (OLayoutDirection.RTL.equals(userPreference.getLayoutDirection())) {
			rootMenu.add(new AttributeModifier("class", "sm sm-rtl sm-clean"));
		} else {
			rootMenu.add(new AttributeModifier("class", "sm sm-clean"));
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		String script = String.format("$(\"#%s\").smartmenus();", rootMenu.getMarkupId());
		getResponse().write(String.format("<script type='text/javascript'>%s</script>", script));
	}

	private class MenuItemFragment extends Fragment {
		public MenuItemFragment(String id, String markupId, MarkupContainer markupProvider, List<OMenuItem> menuItems) {
			super(id, markupId, markupProvider);

			add(new ListView<OMenuItem>("menuItem", menuItems) {
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

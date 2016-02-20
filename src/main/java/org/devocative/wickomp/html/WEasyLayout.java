package org.devocative.wickomp.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.protocol.http.WebSession;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.opt.OUserPreference;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

public class WEasyLayout extends WebMarkupContainer {
	private WebMarkupContainer east, west;

	public WEasyLayout(String id) {
		super(id);

		add(new EasyUIBehavior());
	}

	public WEasyLayout setEast(WebMarkupContainer east) {
		this.east = east;
		add(east);
		return this;
	}

	public WEasyLayout setWest(WebMarkupContainer west) {
		this.west = west;
		add(west);
		return this;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		OUserPreference userPreference = OUserPreference.DEFAULT;
		WebSession session = WebSession.get();
		if (session instanceof OUserPreference) {
			userPreference = (OUserPreference) session;
		}

		if (userPreference.getLayoutDirection() == OLayoutDirection.RTL) {
			if (west != null) {
				west.add(new AttributeModifier("data-options", "region:'east',split:true"));
			}
			if (east != null) {
				east.add(new AttributeModifier("data-options", "region:'west',split:true"));
			}
		} else {
			if (west != null) {
				west.add(new AttributeModifier("data-options", "region:'west',split:true"));
			}
			if (east != null) {
				east.add(new AttributeModifier("data-options", "region:'east',split:true"));
			}
		}
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		tag.put("class", "easyui-layout");
	}

}

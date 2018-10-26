package org.devocative.wickomp.html;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.protocol.http.WebSession;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.opt.OLayoutDirection;
import org.devocative.wickomp.opt.OUserPreference;
import org.devocative.wickomp.wrcs.EasyUIBehavior;

public class WEasyLayout extends WebMarkupContainer {
	private static final long serialVersionUID = 6610030199903497866L;

	private WebMarkupContainer east, west;

	public WEasyLayout(String id) {
		super(id);

		setOutputMarkupId(true);

		add(new EasyUIBehavior());
	}

	public WEasyLayout setEastOfLTRDir(WebMarkupContainer east) {
		this.east = east;
		east.setOutputMarkupId(true);
		return this;
	}

	public WEasyLayout setWestOfLTRDir(WebMarkupContainer west) {
		this.west = west;
		west.setOutputMarkupId(true);
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
				west.add(new AttributeAppender("data-options", "region:'east',split:true", ","));
			}
			if (east != null) {
				east.add(new AttributeAppender("data-options", "region:'west',split:true", ","));
			}
		} else {
			if (west != null) {
				west.add(new AttributeAppender("data-options", "region:'west',split:true", ","));
			}
			if (east != null) {
				east.add(new AttributeAppender("data-options", "region:'east',split:true", ","));
			}
		}
	}

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (isVisible()) {
			String script = String.format("$('#%s').layout();", getMarkupId());
			WebUtil.writeJQueryCall(script, false);
		}
	}
}

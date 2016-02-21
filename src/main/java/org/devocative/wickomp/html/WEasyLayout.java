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
	private boolean eastFitToContent = true, westFitToContent = true;

	public WEasyLayout(String id) {
		super(id);

		setOutputMarkupId(true);

		add(new EasyUIBehavior());
	}

	public WEasyLayout setEast(WebMarkupContainer east) {
		this.east = east;
		east.setOutputMarkupId(true);
		add(east);
		return this;
	}

	public WEasyLayout setWest(WebMarkupContainer west) {
		this.west = west;
		west.setOutputMarkupId(true);
		add(west);
		return this;
	}

	public WEasyLayout setEastFitToContent(boolean eastFitToContent) {
		this.eastFitToContent = eastFitToContent;
		return this;
	}

	public WEasyLayout setWestFitToContent(boolean westFitToContent) {
		this.westFitToContent = westFitToContent;
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

	@Override
	protected void onAfterRender() {
		super.onAfterRender();

		if (westFitToContent || eastFitToContent) {
			StringBuilder builder = new StringBuilder();
			if (westFitToContent && west != null) {
				builder.append(getParentFitScript(west));
			}

			if (eastFitToContent && east != null) {
				builder.append(getParentFitScript(east));
			}

			getResponse().write(String.format("<script>%s</script>", builder.toString()));
		}
	}

	private String getParentFitScript(WebMarkupContainer container) {
		String max = String.format("(Math.max.apply(Math, $('#%s').find('table,div').map(function(){return $(this).width();}).get())+40)", container.getMarkupId());
		return String.format("$('#%s').css('width', %s + 'px');", container.getMarkupId(), max);
	}
}

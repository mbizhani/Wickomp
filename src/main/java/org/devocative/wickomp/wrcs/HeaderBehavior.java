package org.devocative.wickomp.wrcs;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

import java.util.HashMap;
import java.util.Map;

public class HeaderBehavior extends EasyUIBehavior {
	private static final long serialVersionUID = 2448549737676497644L;

	private static HeaderItem LOG_JS = Resource.getCommonJS("main/wLogger.js");
	private static HeaderItem MSG_JS = Resource.getCommonJS("main/wMsg.js");

	private static final Map<String, HeaderItem> HEADER_ITEM_MAP = new HashMap<>();

	// ------------------------------

	private String[] resources;
	private boolean needJQuery = true;
	private boolean needEasyUI = false;

	public HeaderBehavior(String... resources) {
		this.resources = resources;

		for (String resource : resources) {
			addResource(resource);
		}
	}

	// ------------------------------

	public HeaderBehavior setNeedJQuery(boolean needJQuery) {
		this.needJQuery = needJQuery;
		return this;
	}

	public HeaderBehavior setNeedEasyUI(boolean needEasyUI) {
		this.needEasyUI = needEasyUI;
		return this;
	}

	// ------------------------------

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		if (needEasyUI) {
			super.renderHead(component, response);
		} else if (needJQuery) {
			Resource.addJQueryReference(response);
		}

		response.render(LOG_JS);
		response.render(MSG_JS);

		for (String resource : resources) {
			response.render(HEADER_ITEM_MAP.get(resource));
		}
	}

	// ------------------------------

	private void addResource(String rsc) {
		if (rsc.endsWith(".js")) {
			HEADER_ITEM_MAP.put(rsc, Resource.getCommonJS(rsc));
		} else if (rsc.endsWith(".css")) {
			HEADER_ITEM_MAP.put(rsc, Resource.getCommonCSS(rsc));
		}
	}
}

package org.devocative.wickomp.page;

import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WebUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MountedPage extends BasePage {
	private static final long serialVersionUID = 6090775052270522353L;

	public static Map<String, List<String>> WEB_PARAMS_T_T;
	public static Map<String, List<String>> WEB_PARAMS_F_F;
	public static Set<String> PARAMS_T;
	public static Set<String> PARAMS_F;

	public MountedPage() {
		WEB_PARAMS_T_T = WebUtil.toMap(
			true,
			Collections.singletonList("ignoreparam"),
			true,
			Collections.singletonList("undefined")
		);
		WEB_PARAMS_F_F = WebUtil.toMap(false, false);

		PARAMS_T = WebUtil.toSet(true);
		PARAMS_F = WebUtil.toSet(false);
	}
}

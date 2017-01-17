package org.devocative.wickomp.page;

import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WebUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MountedPage extends BasePage {
	private static final long serialVersionUID = 6090775052270522353L;

	public static Map<String, List<String>> WEB_PARAMS_T_T;
	public static Map<String, List<String>> WEB_PARAMS_F_F;

	public static Set<String> PARAMS_T;
	public static Set<String> PARAMS_F;

	public static List<String> LIST_T;
	public static List<String> LIST_F;

	public static List<String> PARAMS_NAME_TO_IGNORE = new ArrayList<>();
	public static List<String> PARAMS_VALUE_TO_IGNORE = new ArrayList<>();

	public MountedPage() {
		WEB_PARAMS_T_T = WebUtil.toMap(
			true,
			PARAMS_NAME_TO_IGNORE,
			true,
			PARAMS_VALUE_TO_IGNORE
		);
		WEB_PARAMS_F_F = WebUtil.toMap(false, false);

		PARAMS_T = WebUtil.toSet(true);
		PARAMS_F = WebUtil.toSet(false);

		LIST_T = WebUtil.listOf("param1", true);
		LIST_F = WebUtil.listOf("param1", false);
	}
}

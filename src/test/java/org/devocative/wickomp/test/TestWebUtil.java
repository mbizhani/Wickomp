package org.devocative.wickomp.test;

import org.apache.wicket.util.tester.WicketTester;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.demo.WickompApplication;
import org.devocative.wickomp.demo.page.MountedPage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestWebUtil {

	@BeforeClass
	public static void setUp() {
		MountedPage.PARAMS_NAME_TO_IGNORE.add("ignoreparam");
		MountedPage.PARAMS_VALUE_TO_IGNORE.add("undefined");

		WicketTester wicketTester = new WicketTester(new WickompApplication());
		wicketTester.executeUrl("./mount?param1=abc&Param1=def&param2=&ignoreParam=123&qaZ WSX = 04&code=undefined&undefined=code");
	}

	@Test
	public void testToMap() {
		//System.out.println("MountedPage.WEB_PARAMS_T_T = " + MountedPage.WEB_PARAMS_T_T);
		//System.out.println("MountedPage.WEB_PARAMS_F_F = " + MountedPage.WEB_PARAMS_F_F);

		/*
			WEB_PARAMS_T_T = WebUtil.toMap(
				true,
				PARAMS_NAME_TO_IGNORE,
				true,
				PARAMS_VALUE_TO_IGNORE
			);
		 */
		Assert.assertEquals(2, MountedPage.WEB_PARAMS_T_T.get("param1").size());
		Assert.assertEquals(2, MountedPage.WEB_PARAMS_T_T.get("Param1").size());
		Assert.assertEquals(2, MountedPage.WEB_PARAMS_T_T.get("ParAm1").size());

		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("param2"));

		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("ignoreparam"));
		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("ignoreParam"));

		Assert.assertEquals(" 04", MountedPage.WEB_PARAMS_T_T.get("qaz wsx ").get(0));
		Assert.assertEquals(" 04", MountedPage.WEB_PARAMS_T_T.get("qaZ WSX ").get(0));

		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("code"));

		Assert.assertEquals("code", MountedPage.WEB_PARAMS_T_T.get("undefined").get(0));


		// ------------------------------

		// WEB_PARAMS_F_F = WebUtil.toMap(false, false);
		Assert.assertEquals(1, MountedPage.WEB_PARAMS_F_F.get("param1").size());
		Assert.assertEquals(1, MountedPage.WEB_PARAMS_F_F.get("Param1").size());
		Assert.assertEquals("", MountedPage.WEB_PARAMS_F_F.get("param2").get(0));
		Assert.assertEquals("123", MountedPage.WEB_PARAMS_F_F.get("ignoreParam").get(0));
		Assert.assertEquals(" 04", MountedPage.WEB_PARAMS_F_F.get("qaZ WSX ").get(0));
		Assert.assertNull(MountedPage.WEB_PARAMS_F_F.get("qaz wsx "));
		Assert.assertEquals("undefined", MountedPage.WEB_PARAMS_F_F.get("code").get(0));
		Assert.assertEquals("code", MountedPage.WEB_PARAMS_F_F.get("undefined").get(0));

		// ------------------------------

		Map<String, List<String>> mapOfUrl = WebUtil.toMap("param1=abc&Param1=def&param2=&qaZ WSX = 04&code=undefined&undefined=code", true, true);
		Assert.assertEquals(2, mapOfUrl.get("param1").size());
		Assert.assertEquals(2, mapOfUrl.get("Param1").size());
		Assert.assertEquals(2, mapOfUrl.get("ParAm1").size());

		Assert.assertNull(mapOfUrl.get("param2"));

		Assert.assertEquals(" 04", mapOfUrl.get("qaz wsx ").get(0));
		Assert.assertEquals(" 04", mapOfUrl.get("qaZ WSX ").get(0));
	}

	@Test
	public void testToSet() {
		Assert.assertEquals(6, MountedPage.PARAMS_T.size());

		Assert.assertTrue(MountedPage.PARAMS_T.contains("param1"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("Param1"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("PARAM1"));

		Assert.assertTrue(MountedPage.PARAMS_T.contains("param2"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("ignoreparam"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("qaz wsx "));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("code"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("undefined"));

		// ------------------------------

		Assert.assertEquals(7, MountedPage.PARAMS_F.size());

		Assert.assertTrue(MountedPage.PARAMS_F.contains("param1"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("Param1"));
		Assert.assertFalse(MountedPage.PARAMS_F.contains("PARAM1"));

		Assert.assertTrue(MountedPage.PARAMS_F.contains("param2"));
		Assert.assertFalse(MountedPage.PARAMS_F.contains("ignoreparam"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("ignoreParam"));
		Assert.assertFalse(MountedPage.PARAMS_F.contains("qaz wsx "));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("qaZ WSX "));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("code"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("undefined"));
	}

	@Test
	public void testListOf() {
		Assert.assertEquals(1, MountedPage.LIST_T.size());
		Assert.assertTrue(MountedPage.LIST_T.contains("abc"));
		Assert.assertTrue(MountedPage.LIST_T.contains("ABC"));
		Assert.assertTrue(MountedPage.LIST_T.contains("Abc"));

		// ------------------------------

		Assert.assertEquals(1, MountedPage.LIST_F.size());
		Assert.assertTrue(MountedPage.LIST_F.contains("abc"));
		Assert.assertFalse(MountedPage.LIST_F.contains("ABC"));
		Assert.assertFalse(MountedPage.LIST_F.contains("Abc"));
	}
}

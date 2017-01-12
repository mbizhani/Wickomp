package org.devocative.wickomp.test;

import org.apache.wicket.util.tester.WicketTester;
import org.devocative.wickomp.WickompApplication;
import org.devocative.wickomp.page.MountedPage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestWebUtil {

	@BeforeClass
	public static void setUp() {
		WicketTester wicketTester = new WicketTester(new WickompApplication());
		wicketTester.executeUrl("./mount?param1=1&Param1=2&param2=&ignoreParam=123&qaZ WSX = 04&code=undefined&undefined=code");
	}

	@Test
	public void testToMap() {
		//System.out.println("MountedPage.WEB_PARAMS_T_T = " + MountedPage.WEB_PARAMS_T_T);
		//System.out.println("MountedPage.WEB_PARAMS_F_F = " + MountedPage.WEB_PARAMS_F_F);

		Assert.assertEquals(2, MountedPage.WEB_PARAMS_T_T.get("param1").size());
		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("param2"));
		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("ignoreparam"));
		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("ignoreParam"));
		Assert.assertEquals(" 04", MountedPage.WEB_PARAMS_T_T.get("qaz wsx ").get(0));
		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("qaZ WSX "));
		Assert.assertNull(MountedPage.WEB_PARAMS_T_T.get("code"));
		Assert.assertEquals("code", MountedPage.WEB_PARAMS_T_T.get("undefined").get(0));

		Assert.assertEquals(1, MountedPage.WEB_PARAMS_F_F.get("param1").size());
		Assert.assertEquals(1, MountedPage.WEB_PARAMS_F_F.get("Param1").size());
		Assert.assertEquals("", MountedPage.WEB_PARAMS_F_F.get("param2").get(0));
		Assert.assertEquals("123", MountedPage.WEB_PARAMS_F_F.get("ignoreParam").get(0));
		Assert.assertEquals(" 04", MountedPage.WEB_PARAMS_F_F.get("qaZ WSX ").get(0));
		Assert.assertNull(MountedPage.WEB_PARAMS_F_F.get("qaz wsx "));
		Assert.assertEquals("undefined", MountedPage.WEB_PARAMS_F_F.get("code").get(0));
		Assert.assertEquals("code", MountedPage.WEB_PARAMS_F_F.get("undefined").get(0));
	}

	@Test
	public void testToSet() {
		Assert.assertEquals(6, MountedPage.PARAMS_T.size());
		Assert.assertTrue(MountedPage.PARAMS_T.contains("param1"));
		Assert.assertFalse(MountedPage.PARAMS_T.contains("Param1"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("param2"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("ignoreparam"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("qaz wsx "));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("code"));
		Assert.assertTrue(MountedPage.PARAMS_T.contains("undefined"));

		Assert.assertEquals(7, MountedPage.PARAMS_F.size());
		Assert.assertTrue(MountedPage.PARAMS_F.contains("param1"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("Param1"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("param2"));
		Assert.assertFalse(MountedPage.PARAMS_F.contains("ignoreparam"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("ignoreParam"));
		Assert.assertFalse(MountedPage.PARAMS_F.contains("qaz wsx "));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("qaZ WSX "));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("code"));
		Assert.assertTrue(MountedPage.PARAMS_F.contains("undefined"));
	}
}

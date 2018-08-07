package org.devocative.wickomp.test;

import org.devocative.wickomp.formatter.ONumberFormatter;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestFormatter {

	@Test
	public void testNumberFormatter() {
		ONumberFormatter fmt = ONumberFormatter.of("#,##0.*");

		assertEquals("1", fmt.format(1.000));
		assertEquals("1.1234", fmt.format(1.1234));
		assertEquals("1.001234", fmt.format(1.001234));
		assertEquals("0.001234", fmt.format(0.001234));
		assertEquals("0.0000000001234", fmt.format(.0000000001234));

		assertEquals("999.0000000001234", fmt.format(999.0000000001234));
		assertEquals("1,999.0000000001234", fmt.format(new BigDecimal("1999.0000000001234")));
		assertNotEquals("1,999.0000000001234", fmt.format(1999.0000000001234));


		fmt = ONumberFormatter.of("#,##0.0##");
		assertEquals("1.0", fmt.format(1));
		assertEquals("1.123", fmt.format(1.1234));
		assertEquals("1.001", fmt.format(1.001234));
		assertEquals("0.001", fmt.format(0.001234));
		assertEquals("0.0", fmt.format(.0000000001234));

		assertEquals("999.0", fmt.format(999.0000000001234));
		assertEquals("1,999.0", fmt.format(new BigDecimal("1999.0000000001234")));
	}

}

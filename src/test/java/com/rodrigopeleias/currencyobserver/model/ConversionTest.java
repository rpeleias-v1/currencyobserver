package com.rodrigopeleias.currencyobserver.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConversionTest {
	
	Conversion conversion, previousOverConversion;
	
	@Before
	public void setUp() {
		conversion = new Conversion();
		conversion.setFrom("BRL");
		conversion.setTo("USD");
		conversion.setFromAmount(1.00);
		conversion.setToAmount(3.20d);
		
		previousOverConversion = new Conversion();
		previousOverConversion.setFrom("BRL");
		previousOverConversion.setTo("USD");
		previousOverConversion.setFromAmount(1.00);
		previousOverConversion.setToAmount(2.70d);
	}

	@Test
	public void testCalculatePositiveVariation() {
		double positiveVariation = previousOverConversion.getToAmount() / conversion.getToAmount();
		positiveVariation = 100 - (positiveVariation * 100);
		assertEquals(previousOverConversion.calculateVariation(conversion), positiveVariation, 0.0001);
	}
	
	@Test
	public void testCalculateNegativeVariation() {
		double positiveVariation = conversion.getToAmount() / previousOverConversion.getToAmount();
		positiveVariation = (positiveVariation - 1) * 100;
		assertEquals(conversion.calculateVariation(previousOverConversion), positiveVariation, 0.0001);
	}
}

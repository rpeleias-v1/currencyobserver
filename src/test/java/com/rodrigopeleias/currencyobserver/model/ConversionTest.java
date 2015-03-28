package com.rodrigopeleias.currencyobserver.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.rodrigopeleias.currencyobserver.model.builder.ConversionBuilder;

public class ConversionTest {
	
	private Conversion conversion, previousOverConversion;
	
	@Before
	public void setUp() {
		ConversionBuilder builder = new ConversionBuilder();
		conversion = builder.from("BRL")
							.fromValue(1.00)
							.to("USD")
							.toValue(3.20d)
							.build();		
		
		previousOverConversion = builder.from("BRL")
				.fromValue(1.00)
				.to("USD")
				.toValue(2.70d)
				.build();		
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

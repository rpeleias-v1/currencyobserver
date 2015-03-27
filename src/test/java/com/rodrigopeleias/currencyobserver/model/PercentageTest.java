package com.rodrigopeleias.currencyobserver.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PercentageTest {
	
	private Percentage percentage;
	
	@Before
	public void setUp() {
		percentage = new Percentage(10);	
	}

	@Test
	public void testOverPercentage() {
		assertTrue(percentage.isOver(17));
	}
	
	@Test
	public void testUnderPercentage() {
		assertFalse(percentage.isOver(7));
	}

}

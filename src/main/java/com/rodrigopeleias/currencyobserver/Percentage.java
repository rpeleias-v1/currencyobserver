package com.rodrigopeleias.currencyobserver;

public class Percentage {
	
	private float percentage;

	public Percentage(float percentage) {	
		this.percentage = percentage;
	}
	
	public boolean isOver(double comparison) {
		if (comparison >= percentage) {
			return true;
		}
		return false;
	}
	
}

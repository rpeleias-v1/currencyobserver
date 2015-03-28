package com.rodrigopeleias.currencyobserver.model.builder;

import com.rodrigopeleias.currencyobserver.model.Percentage;

public class PercentageBuilder {
	
	private float percentage;
	
	public PercentageBuilder with(float percentage) {
		this.percentage = percentage;
		return this;
	}
	
	public Percentage build() {
		return new Percentage(percentage);
	}

}

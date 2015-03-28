package com.rodrigopeleias.currencyobserver.model.builder;

import com.rodrigopeleias.currencyobserver.model.Conversion;

public class ConversionBuilder {
	
	private String from;
	private String to;
	private double fromAmount;
	private double toAmount;
	
	public ConversionBuilder from(String from) {
		this.from = from;
		return this;
	}
	
	public ConversionBuilder fromValue(double fromAmount) {
		this.fromAmount = fromAmount;
		return this;
	}
	
	public ConversionBuilder to(String to) {
		this.to = to;
		return this;
	}
	
	public ConversionBuilder toValue(double toAmount) {
		this.toAmount = toAmount;
		return this;
	}
	
	public Conversion build() {
		Conversion conversion = new Conversion();
		conversion.setFrom(from);
		conversion.setFromAmount(fromAmount);
		conversion.setTo(to);
		conversion.setToAmount(toAmount);
		return conversion;
	}

}

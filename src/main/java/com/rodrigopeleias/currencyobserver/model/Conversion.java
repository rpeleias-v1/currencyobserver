package com.rodrigopeleias.currencyobserver.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Conversion {
	
	private String from;
	private String to;
	
	@JsonProperty("from_amount")
	private double fromAmount;
	
	@JsonProperty("to_amount")
	private double toAmount;
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public double getFromAmount() {
		return fromAmount;
	}
	
	public void setFromAmount(double fromAmount) {
		this.fromAmount = fromAmount;
	}
	
	public double getToAmount() {
		return toAmount;
	}

	public void setToAmount(double toAmount) {
		this.toAmount = toAmount;
	}

	@Override
	public String toString() {
		return "Conversion [from=" + from + ", to=" + to + ", fromAmount="
				+ fromAmount + ", toAmount=" + toAmount + "]";
	}
		
	public double calculateVariation(Conversion previous) {
		double variation = this.toAmount / previous.toAmount;
		double percentage;
		percentage = verify(variation);
		return percentage;
	}

	private double verify(double variation) {
		double percentage;
		if (variation >= 1) {
			percentage = (variation - 1) * 100;
		} else {
			percentage = 100 - (variation * 100);
		}
		return percentage;
	}
}

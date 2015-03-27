package com.rodrigopeleias.currencyobserver.job;

import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rodrigopeleias.currencyobserver.Conversion;
import com.rodrigopeleias.currencyobserver.Percentage;
import com.rodrigopeleias.currencyobserver.helper.HipChatHelper;

public class ConversionCalculatorJob implements Job{
	
	private static Conversion previousConversion;
	
	private String hipChatAccessToken = "chuL6cprvR8SWDQP5ibkYmpQRwlVmKDtaQCZWpeN";

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			HttpResponse<JsonNode> response = Unirest.get("https://currencyconverter.p.mashape.com/?from=USD&from_amount=1&to=BRL")
					.header("X-Mashape-Key", "5vFG4l0TdEmshwwd7HYYYlWlRZyLp129D9fjsn29gxyfoFBm8T")
					.header("Accept", "application/json")
					.asJson();
			JSONObject conversionResponse = response.getBody().getObject();
			
			ObjectMapper mapper = new ObjectMapper();
			Conversion conversion = mapper.readValue(conversionResponse.toString(), Conversion.class);
			System.out.println(conversion);
			
			Percentage percentage = new Percentage(10f);

			if (previousConversion != null) {
				HipChatHelper hipChat = new HipChatHelper(hipChatAccessToken);				
				hipChat.sendMessage("216979", "Previous Conversion = " + previousConversion);
				hipChat.sendMessage("216979", "Previous Conversion = " + conversion);
				if (percentage.isOver(conversion.calculateVariation(previousConversion))) {					
					hipChat.sendMessage("216979", "Conversion is over 10% in relation to previous information.");
				}
				else {
					hipChat.sendMessage("216979", "Conversion is under 10% in relation to previous information");										
				}				
			}	
			previousConversion = conversion;
			
		} catch ( UnirestException | IOException  ex) {
			ex.printStackTrace();
		}
		
	}

}
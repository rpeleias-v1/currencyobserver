package com.rodrigopeleias.currencyobserver.job;

import java.io.IOException;
import java.util.ResourceBundle;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rodrigopeleias.currencyobserver.helper.HipChatHelper;
import com.rodrigopeleias.currencyobserver.model.Conversion;
import com.rodrigopeleias.currencyobserver.model.Percentage;

public class ConversionCalculatorJob implements Job{
	
	private static Conversion previousConversion;
	
	private ResourceBundle bundle = ResourceBundle.getBundle("keysConfiguration");
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			doExecute();
		} catch ( UnirestException | IOException  ex) {
			ex.printStackTrace();
		}		
	}

	private void doExecute() throws UnirestException, IOException,
			JsonParseException, JsonMappingException {
		JSONObject conversionResponse = consumeCurrencyService();			
		Conversion conversion = mapJsonToObject(conversionResponse);			
		Percentage percentage = new Percentage(10f);
		if (previousConversion != null) {
			sendHipChatMessage(conversion, percentage);				
		}	
		previousConversion = conversion;
	}

	private Conversion mapJsonToObject(JSONObject conversionResponse)
			throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		Conversion conversion = mapper.readValue(conversionResponse.toString(), Conversion.class);
		System.out.println(conversion);
		return conversion;
	}

	private JSONObject consumeCurrencyService() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get("https://currencyconverter.p.mashape.com/?from=USD&from_amount=1&to=BRL")
				.header("X-Mashape-Key", bundle.getString("mashape.key"))
				.header("Accept", "application/json")
				.asJson();
		JSONObject conversionResponse = response.getBody().getObject();
		return conversionResponse;
	}

	private void sendHipChatMessage(Conversion conversion, Percentage percentage) {
		HipChatHelper hipChat = new HipChatHelper(bundle.getString("hipChat.accessToken"));	
		String hipChatRoomNumber = bundle.getString("hipchat.roomNumber");
		hipChat.sendMessage(hipChatRoomNumber, "Previous Conversion = " + previousConversion);
		hipChat.sendMessage(hipChatRoomNumber, "Previous Conversion = " + conversion);
		if (percentage.isOver(conversion.calculateVariation(previousConversion))) {					
			hipChat.sendMessage(hipChatRoomNumber, "Conversion is over 10% in relation to previous information.");
		}
		else {
			hipChat.sendMessage(hipChatRoomNumber, "Conversion is under 10% in relation to previous information");										
		}
	}
}

package com.rodrigopeleias.currencyobserver.helper;

import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;

public class HipChatHelper {
	
	private String accessToken;
	private HipChatClient client;
	
	public HipChatHelper(String accessToken) {
		super();
		this.accessToken = accessToken;
		client = new HipChatClient(accessToken);
	}
	
	public void sendMessage(String roomNumber, String message) {
		SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder(roomNumber, message);
		builder.setColor(MessageColor.GREEN).setNotify(true).build().execute();
	}

}

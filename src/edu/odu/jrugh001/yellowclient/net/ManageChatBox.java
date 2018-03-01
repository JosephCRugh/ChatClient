package edu.odu.jrugh001.yellowclient.net;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.odu.jrugh001.yellowclient.MessagingBox;
import edu.odu.jrugh001.yellowclient.ServerHandle;
import edu.odu.jrugh001.yellowclient.controller.ChatController;
import edu.odu.jrugh001.yellowclient.net.listening.Listener;
import edu.odu.jrugh001.yellowclient.net.listening.Opcode;
import edu.odu.jrugh001.yellowclient.net.listening.Opcodes;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ManageChatBox implements Listener {
	
	private final Font dateFont = Font.font("Segoe UI", 13);
	
	@Opcode(getOpcode = Opcodes.CHAT_BOX_MESSAGE)
	public void incomingChatMessage(ServerHandle serverHandle) {
		
		ChatController controller = ChatController.getInstance();
		TextFlow displayChatBox = controller.getDisplayChatBox();
		
		String[] dateString = new SimpleDateFormat("HH:mm").format(new Date()).split(":");
		int left = Integer.parseInt(dateString[0]);
		boolean amOrPm = left > 12;
		if (amOrPm) dateString[0] = String.valueOf(left - 12);
		
		MessagingBox messagingBox = controller.getMessagingBox();
		
		Text date = messagingBox.createText(
				dateString[0] + ":" + dateString[1] + (amOrPm ? " PM" : " AM") + " ", Color.GRAY, dateFont);
		Text userNameText = messagingBox.createText(
				serverHandle.readString() + " ",
				Color.web(serverHandle.readString(), 1.0), MessagingBox.DEFAULT_FONT);
		Text userMessage = messagingBox.createText(
				serverHandle.readString() + "\n", Color.LIGHTGRAY, MessagingBox.DEFAULT_FONT);
		
		messagingBox.addText(displayChatBox, date, userNameText, userMessage);
	}
}

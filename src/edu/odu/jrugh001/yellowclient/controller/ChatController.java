package edu.odu.jrugh001.yellowclient.controller;

import edu.odu.jrugh001.yellowclient.Client;
import edu.odu.jrugh001.yellowclient.MessagingBox;
import edu.odu.jrugh001.yellowclient.net.listening.Opcodes;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import lombok.Getter;

@Getter
public class ChatController extends SharedControls {
	
	private @Getter static ChatController instance;
	
	private MessagingBox messagingBox = new MessagingBox();
	
	public ChatController() {
		ChatController.instance = this;
	}
	
	@FXML
	private TextField sendMessageInput;
	
	@FXML
	private TextFlow displayChatBox;
	
	@FXML
	private ScrollPane messageScrollPane;
	
	@FXML
	private AnchorPane onlineUserPane;
	
	/**
	 * Receiving text from the keyboard to send across the network
	 */
	public void onUserInput(KeyEvent event) {
		String inputedChar = event.getCharacter();
		
		// Enter key
		if ((int)inputedChar.charAt(0) == 13) {
			
			String text = sendMessageInput.getText();
			sendMessageInput.clear();
			
			if (text.length() > 0) {
				Client.getInstance().getClientNetwork().send(Opcodes.CHAT_BOX_MESSAGE, outStream -> 
																				outStream.writeUTF(text));
			}
		}
	}
}

package edu.odu.jrugh001.yellowclient;

import edu.odu.jrugh001.yellowclient.controller.ChatController;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MessagingBox {
	
	public static final Font DEFAULT_FONT = Font.font("Segoe UI", 15);
	
	public void addText(TextFlow displayChatBox, Text ... texts) {
		ScrollPane scrollPane = ChatController.getInstance().getMessageScrollPane();
		Platform.runLater(() -> {
			displayChatBox.getChildren().addAll(texts);	
			scrollPane.setVvalue(1.0);
		});
	}
	
	public Text createText(String message, Color color, Font font) {
		Text text = new Text(message);
		text.setFill(color);
		text.setFont(font);
		return text;
	}
}

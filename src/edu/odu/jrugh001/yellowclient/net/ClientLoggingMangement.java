package edu.odu.jrugh001.yellowclient.net;

import edu.odu.jrugh001.yellowclient.Client;
import edu.odu.jrugh001.yellowclient.MessagingBox;
import edu.odu.jrugh001.yellowclient.ServerHandle;
import edu.odu.jrugh001.yellowclient.controller.ChatController;
import edu.odu.jrugh001.yellowclient.controller.LoginController;
import edu.odu.jrugh001.yellowclient.net.listening.Listener;
import edu.odu.jrugh001.yellowclient.net.listening.Opcode;
import edu.odu.jrugh001.yellowclient.net.listening.Opcodes;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ClientLoggingMangement implements Listener {
	
	private final int userLabelOffset = 18;
	
	@Opcode(getOpcode = Opcodes.LOGIN_REQUEST_RESPONSE)
	public void onLoginRequestResponse(ServerHandle serverHandle) {
		
		byte response = serverHandle.readByte();
		if (response == 0x1) {
			String username = serverHandle.readString();
			String[] usernames = (String[]) serverHandle.readObject();
			Client.getInstance().switchToChatScene(() -> {
				
				// Adding the client's username to the top of the list of online users
				AnchorPane onlineUserPane = ChatController.getInstance().getOnlineUserPane();
				onlineUserPane.getChildren().add(new Label(username));
				
				// The username's of other client's online
				for (int i = 0; i < usernames.length; i++) {
					Label userLabel = new Label(usernames[i]);
					userLabel.setLayoutY((i + 1) * userLabelOffset);
					onlineUserPane.getChildren().add(userLabel);
				}
			});
		} else if (response == 0x0) {
			// Could not validate login
			Client.getInstance().getClientNetwork().close();
			LoginController.getInstance().setLoginLabelText("Email or password is invalid.", Color.RED);
		} else if (response == 0x2) {
			Client.getInstance().getClientNetwork().close();
			LoginController.getInstance().setLoginLabelText("That account is already logged in.", Color.RED);
		}
	}
	
	/**
	 * Indicates when another client has logged into the server.
	 */
	@Opcode(getOpcode = Opcodes.INDICATE_LOGIN)
	public void onIndicateLogin(ServerHandle serverHandle) {
		ChatController controller = ChatController.getInstance();
		MessagingBox messagingBox = controller.getMessagingBox();
		
		String username = serverHandle.readString();
		
		Text message = messagingBox.createText(
				"Client " + username + " has logged in!\n",
				Color.GRAY,
				MessagingBox.DEFAULT_FONT);
		
		messagingBox.addText(controller.getDisplayChatBox(), message);
		
		AnchorPane onlineUserPane = ChatController.getInstance().getOnlineUserPane();
		
		Platform.runLater(() -> {
			Label userLabel = new Label(username);
			userLabel.setLayoutY(onlineUserPane.getChildren().size() * userLabelOffset);
			ChatController.getInstance().getOnlineUserPane().getChildren().add(userLabel);
		});
	}
	
	@Opcode(getOpcode = Opcodes.LOGOUT_REQUEST)
	public void onClientLoggedOut(ServerHandle serverHandle) {
	
		String username = serverHandle.readString();
		System.out.println("Client " + username + " has logged out.");
	}
}

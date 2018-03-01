package edu.odu.jrugh001.yellowclient.controller;

import edu.odu.jrugh001.yellowclient.Client;
import edu.odu.jrugh001.yellowclient.net.ClientNetwork;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class LoginController extends SharedControls {
	
	private @Getter static LoginController instance;
	
	public LoginController() {
		LoginController.instance = this;
	}
	
	@FXML
	private Label loginLabel;
	
	@FXML
	private TextField emailField;
	
	@FXML
	private PasswordField passwordField;
	
	/**
	 * When the client presses the login button
	 */
	public void onLoginButton(ActionEvent event) {
		ClientNetwork clientNetwork = Client.getInstance().getClientNetwork();
		if (!clientNetwork.isRunning()) {
			String email = emailField.getText();
			String password = passwordField.getText();
			if (email.isEmpty()) {
				setLoginLabelText("Must fill out email field.", Color.RED);
				return;
			} else if (password.isEmpty()) {
				setLoginLabelText("Must fill out the password field.", Color.RED);
				return;
			}
			clientNetwork.connect("localhost", email, password);
			setLoginLabelText("Connecting...", Color.GREEN);
		}	
	}
	
	public synchronized void setLoginLabelText(String text, Color color) {
		Platform.runLater(() -> {
			loginLabel.setText(text);
			loginLabel.setTextFill(color);
		});
	}
}

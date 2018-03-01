package edu.odu.jrugh001.yellowclient;

import com.sun.glass.ui.Window;

import edu.odu.jrugh001.yellowclient.controller.LoginController;
import edu.odu.jrugh001.yellowclient.net.ClientNetwork;
import edu.odu.jrugh001.yellowclient.net.listening.Opcodes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

@Getter
public class Client extends Application {

	private @Getter static Client instance; 
	private ClientNetwork clientNetwork;
	private Scene chatScene;
	private Scene loginScene;
	private Stage stage;
	private static String argumentEmail = "";
	private static String argumentPassword = "";
	
	static {
		System.loadLibrary("WindowMinimize");
	}
	
	public static void main(String args[]) {
		
		if (args.length == 2) {
			argumentEmail = args[0];
			argumentPassword = args[1];
		}
		
		launch(args);
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		instance = this;
		
		chatScene = new Scene(FXMLLoader.load(getClass().getResource("res/messaging.fxml")));
		chatScene.getStylesheets().add(getClass().getResource("res/messaging.css").toExternalForm());
		    
		loginScene = new Scene(FXMLLoader.load(getClass().getResource("res/login.fxml")));
		loginScene.getStylesheets().add(getClass().getResource("res/login.css").toExternalForm());
		
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(loginScene);
		stage.setTitle("YellowClient");
		stage.getIcons().add(new Image("file:src/edu/odu/jrugh001/yellowclient/res/window_icon.png"));
		
		clientNetwork = new ClientNetwork();
	
		stage.show();
		
		// Adds WS_MINIMIZEBOX property so the window can be minimized
		Window window = Window.getWindows().get(0);
		if (window != null) {
			setupTaskIconMinimization(window.getNativeWindow());	
		}
		
		if (!argumentEmail.isEmpty()) LoginController.getInstance().getEmailField().setText(argumentEmail);
		if (!argumentPassword.isEmpty()) LoginController.getInstance().getPasswordField().setText(argumentPassword);
		
		this.stage = stage;
	}
	
	// Associated with WindowMinimize.dll
	private native void setupTaskIconMinimization(long jhWnd);
	
	@Override
	public void stop() throws Exception {
		if (clientNetwork.isRunning()) clientNetwork.send(Opcodes.LOGOUT_REQUEST, outStream -> {});
		clientNetwork.close();
	}
	
	public void switchToChatScene(Runnable run) {
		Platform.runLater(() -> { 
			stage.setScene(chatScene);
			run.run();
		});
	}
}

package edu.odu.jrugh001.yellowclient.controller;

import edu.odu.jrugh001.yellowclient.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SharedControls {

	private double[] xOffset = { 0.0f };
	private double[] yOffset = { 0.0f };
	
	@FXML
	protected Pane exitButtonHoverPane;
	
	@FXML
	protected Pane minimizeButtonHoverPane;
	
	public void onExitButton(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	public void onEnteredExitButton() {
		exitButtonHoverPane.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	@FXML
	public void onLeftExitButton() {
		exitButtonHoverPane.setBackground(
				new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	public void onMinimizeButton(ActionEvent event) {
		Client.getInstance().getStage().setIconified(true);
	}
	
	@FXML
	public void onEnteredMinimizeButton() {
		minimizeButtonHoverPane.setBackground(
				new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	@FXML
	public void onLeftMinimizeButton() {
		minimizeButtonHoverPane.setBackground(
				new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	public void onMousePressedTop(MouseEvent event) {
		Stage stage = Client.getInstance().getStage();
		xOffset[0] = stage.getX() - event.getScreenX();
        yOffset[0] = stage.getY() - event.getScreenY();
	}
	
	public void onMouseDraggedTop(MouseEvent event) {
		Client client = Client.getInstance();
		client.getStage().setX(event.getScreenX() + xOffset[0]);
		client.getStage().setY(event.getScreenY() + yOffset[0]);
	}	
}

package ui.windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InputWindow {
	
	private TextField inputField = new TextField();
	private Button enterButton = new Button("Enter");
	private Label message = new Label();
	private Stage window = new Stage();

	public InputWindow(String title, String inputMessage) {
		BorderPane root = new BorderPane();
		AnchorPane anchorPane = new AnchorPane();
		
		AnchorPane.setLeftAnchor(inputField, 10.0);
		AnchorPane.setTopAnchor(inputField, 10.0);
		AnchorPane.setRightAnchor(inputField, 90.0);
		
		AnchorPane.setRightAnchor(enterButton, 10.0);
		AnchorPane.setTopAnchor(enterButton, 10.0);
		
		AnchorPane.setTopAnchor(message, 10.0);
		AnchorPane.setLeftAnchor(message, 10.0);
		AnchorPane.setRightAnchor(message, 10.0);
		
		message.setText(inputMessage);
		message.setAlignment(Pos.CENTER);
		
		anchorPane.getChildren().addAll(inputField, enterButton);
		root.setTop(message);
		root.setCenter(anchorPane);
		
		window.setTitle(title);
		window.setScene(new Scene(root, 500, 100));
		window.setAlwaysOnTop(true);
		window.show();
	}
	
	public TextField getTextField() {
		return this.inputField;
	}
	
	public Button getEnterButton() {
		return this.enterButton;
	}
	
	public void close() {
		window.close();
	}
}

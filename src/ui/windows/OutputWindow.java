package ui.windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OutputWindow {
	
	public OutputWindow(String output, String title) {
		BorderPane root = new BorderPane();
		Label outputLabel = new Label(output);
		outputLabel.setAlignment(Pos.CENTER);
		
		AnchorPane.setTopAnchor(outputLabel, 10.0);
		AnchorPane.setRightAnchor(outputLabel, 10.0);
		AnchorPane.setLeftAnchor(outputLabel, 10.0);
		root.setCenter(outputLabel);
		
		Stage window = new Stage();
		window.setTitle(title);
		window.setScene(new Scene(root, 500, 300));
		window.setAlwaysOnTop(true);
		window.show();
	}
}

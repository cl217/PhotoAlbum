package view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
	@FXML Button loginButton;

	public void buttonPress(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if (b == loginButton) {
			Parent abc = FXMLLoader.load( getClass().getResource("displayPicTest.fxml"));
			Scene scene = new Scene(abc);
			
			Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
		} 
	}
}

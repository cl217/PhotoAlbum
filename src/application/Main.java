package application;
	
import java.io.IOException;

import controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	@Override
	public void start(Stage mainStage) 
	throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/loginView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		Login loginController = loader.getController();
		loginController.start();
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


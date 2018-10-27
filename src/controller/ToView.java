package controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ToView{
	
	public static void toStage( FXMLLoader loader, AnchorPane view ) throws IOException {
		AnchorPane root = (AnchorPane)loader.load();
		Album controller = loader.getController();
		controller.start();
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void login( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( ToView.class.getResource("/view/loginView.fxml"));
		toStage( loader, view );
	}
	
	public static void album( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( ToView.class.getResource("/view/albumView.fxml"));
		toStage( loader, view );
	}
	
	public static void admin( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( ToView.class.getResource("/view/adminView.fxml"));
		toStage( loader, view );
	}
	
	public static void thumbnail( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( ToView.class.getResource("/view/thumbnailView.fxml"));
		toStage( loader, view );
	}

	public static void photo( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( ToView.class.getResource("/view/photoView.fxml"));
		toStage( loader, view );
	}	
}

package application;
	
import java.io.IOException;
import java.util.ArrayList;

import controller.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Data;
import model.User;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	public static final String directory = "data";
	
	public void loadData() throws ClassNotFoundException, IOException {
		System.out.println("ran3");
		System.out.println("ran4");
		Master.data = Data.readObjA();
		for( String name: Master.data.userList ) {
			User u = new User(name);
			u = u.read();
			Master.userMap.put(name, u);
		}

		System.out.println("5");
	}
	
	@Override
	public void start(Stage mainStage) 
	throws IOException {
		System.out.println("ran2");
		try {
			loadData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("4");
		
		
		//Testing purposes
		User cindy = new User( "cindy" );
		Master.userMap.put("cindy", cindy );
		ArrayList<String> stock = new ArrayList<String>();
		cindy.albumMap.put("Stock", stock);
		
		
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
		System.out.println("ran 1");
		launch(args);
	}
}


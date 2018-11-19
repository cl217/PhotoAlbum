package application;
	
import java.io.EOFException;
import java.io.IOException;


import controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Data;
import model.User;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Photos extends Application {
	
	//private static final String directory = "data";
	
	public void loadData() throws ClassNotFoundException, IOException {	
		try {
			Master.data = Data.read();
		}catch(EOFException e ) {
			if(Master.data.userList.isEmpty()){
				//System.out.println("writeStock");
				User stock = new User("stock");
				stock.loadStock();
				Master.userMap.put("stock", stock );
				Master.writeData();
			}
		}

		for( String name: Master.data.userList ) {
			User u = new User(name);
			u = u.read();
			Master.userMap.put(name, u);
		}

		//System.out.println("5.2");
		//System.out.println("stockalbumsize:" + Master.userMap.get("stock").albumMap.get("Stock").size());
	}
	
	@Override
	public void start(Stage mainStage) 
	throws IOException {
		//System.out.println("ran2");
		
		try {
			loadData();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("4");
		
		
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/loginView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		Login loginController = loader.getController();
		loginController.start();
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.setTitle("Photos");
		mainStage.show();
	}
	
	public static void main(String[] args) {
		//System.out.println("ran 1");
		launch(args);
	}
}


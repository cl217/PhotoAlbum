package controller;


import java.io.*;

import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

public class Master implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String directory = "data";
	//public static final String file = "userData";
	
	public static Data data = new Data();
	public static HashMap<String, User> userMap = new HashMap<String, User>();
	public static User currentUser;

	
	public static void writeData() throws FileNotFoundException, IOException {
		
		for( String name : userMap.keySet() ) {
			if( data.userList.contains(name) == false ) {
				System.out.println("name");
				File file = new File( "../data/" + name);				
			}
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(directory + File.separator + name) );
			System.out.println("6");
			oos.writeObject(userMap.get(name));
			System.out.println("7");
			oos.close();
		}
		
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(directory + File.separator + "userData") );
		oos.writeObject(data);
		oos.close();
		
	}
	
	public static void toLogin( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/loginView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Login controller = loader.getController();
		controller.start();
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void toAlbum( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/albumView.fxml"));
		System.out.println("7");
		AnchorPane root = (AnchorPane)loader.load();
		Album controller = loader.getController();
		controller.start();
		System.out.println("8");
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void toAdmin( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/adminView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Admin controller = loader.getController();
		controller.start();
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void toThumbnail( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/thumbnailView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Thumbnail controller = loader.getController();
		controller.start();
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public static void toPhoto( AnchorPane view ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/photoView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Photo controller = loader.getController();
		controller.start();
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}	
}

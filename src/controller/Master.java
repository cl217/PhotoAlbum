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

	/**
	 * 
	 */
	public static final String directory = "data";
	//public static final String file = "userData";
	
	public static Data data = new Data();
	public static HashMap<String, User> userMap = new HashMap<String, User>();
	public static User currentUser;
	public static String currentAlbum;
	
	public static void writeData(){
			try {
				for( String name : userMap.keySet() ) {
					if( data.userList.contains(name) == false ) {
						data.userList.add(name);
					}
					ObjectOutputStream oos;
					oos = new ObjectOutputStream( new FileOutputStream(directory + File.separator + name) );
					System.out.println("6");
					oos.writeObject(userMap.get(name));
					System.out.println("7");
					oos.close();
				}
				
				ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(directory + File.separator + "userData") );
				oos.writeObject(data);
				oos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		System.out.println("hi");
		controller.start();
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void toThumbnail( AnchorPane view ) throws IOException{
		System.out.println("Master.toThumbnail1");
		System.out.println(currentAlbum);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/thumbnailView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		System.out.println("toThumbnail1.2");
		Thumbnail controller = loader.getController();
		System.out.println("Master.toThumbnail2");
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

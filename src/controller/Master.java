package controller;

import java.io.*;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class Master implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String directory = "data";
	
	public static Data data = new Data();
	
	/**
	 * @userMap hashmap of all users
	 * @currentUser the user that's currently logged in
	 * @currentAlbum the album that's currently open
	 */
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

	/**
	 * 
	 * @param view current stage
	 * @throws IOException no stage
	 */
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
	
	/**
	 * 
	 * @param view current stage
	 * @throws IOException no stage
	 */
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
	
	/**
	 * 
	 * @param view current stage
	 * @throws IOException no stage
	 */
	public static void toTag( AnchorPane view, Picture p )throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/tagView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		TagController controller = loader.getController();
		controller.start(p);
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		//stage.initOwner((Stage) parent.getScene().getWindow());
        //stage.initModality(Modality.WINDOW_MODAL);
        //stage.setScene(scene);
		//stage.showAndWait();
		stage.show();
	}
	
	/**
	 * 
	 * @param view current stage
	 * @throws IOException no stage
	 */
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
	
	/**
	 * 
	 * @param view current stage
	 * @throws IOException no stage
	 */
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

	/**
	 * 
	 * @param view current stage
	 * @throws IOException no stage
	 */
	public static void toPhoto( AnchorPane view, int picIndex, ArrayList<Picture> album ) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation( Master.class.getResource("/view/photoView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Photo controller = loader.getController();
		controller.start(picIndex, album);
		Scene scene = new Scene(root);
		Stage stage = (Stage) view.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}	
	
}

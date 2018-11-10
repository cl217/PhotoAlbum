package controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Album {
	@FXML ListView<String> albumListView;
	@FXML Button logoutButton;
	
	/**
	 * Make updates to Master.currentUser.albumMap
	 * 		HashMap<String, ArrayList<String>> //<albumName, list of photos>
	 * To display albums to listview
	 * 		Iterate over Master.currentUser.albumMap.keySet()
	 * 			add to ObservableList
	 * 
	 */
	
	
	
	
	
	public void start() {
		//System.out.println("Album");
		ObservableList<String> list = FXCollections.observableArrayList();
		for( String albumName : Master.currentUser.albumMap.keySet() ) {
			list.add(albumName);
		}
		albumListView.setItems(	list );
	}
	
	public void buttonPress( ActionEvent event ) throws IOException {
		Button b = (Button)event.getSource();
		if (b == logoutButton) {
			toLogin(event);
		}
	}
	
	public void toLogin( ActionEvent e )throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/loginView.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		//Parent abc = FXMLLoader.load( getClass().getResource("/view/albumView.fxml"));
		Login controller = loader.getController();
		controller.start();
		Scene scene = new Scene(root);
		Stage mainStage = (Stage)((Node) e.getSource()).getScene().getWindow();
		//window.setScene(scene);
		//window.show();
		mainStage.setScene(scene);
		mainStage.show();
		return;
	}
}

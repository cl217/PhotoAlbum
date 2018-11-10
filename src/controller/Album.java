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
	 * AlbumSelected
	 * 		Master.currentAlbum = set to selected album name
	 * 		dont have to get from the hashmap
	 * 		go to thunbnailView
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
			Master.toLogin( /*@FXML Anchorpane name*/);
		}
		
		
		
	}
	
}

package controller;

import java.io.IOException;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import javafx.application.Platform;
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
	@FXML Button openButton;
	@FXML Button quitButton;
	@FXML AnchorPane albumView;
	
	
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
	
	ObservableList<String> list = FXCollections.observableArrayList();
	public void start() {
		//System.out.println("Album");
		for( String albumName : Master.currentUser.albumMap.keySet() ) {
			list.add(albumName);
		}
		albumListView.setItems(	list );
	}
	
	public void buttonPress( ActionEvent event ) throws IOException {
		Button b = (Button)event.getSource();
	
		if(b == openButton) {
			int selectIndex = albumListView.getSelectionModel().getSelectedIndex();
			Master.currentAlbum = list.get(selectIndex);
			Master.toThumbnail(albumView);
		}
		if (b == logoutButton) {
			Master.writeData();
			Master.toLogin( albumView );
		}	
		if(b == quitButton) {
			Master.writeData();
			Platform.exit();
		}
		
		
		
	}
	
}

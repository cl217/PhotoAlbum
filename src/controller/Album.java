package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Picture;
import model.User;

public class Album {
	@FXML ListView<String> albumListView;
	@FXML Button logoutButton;
	@FXML Button openButton;
	@FXML Button quitButton;
	@FXML AnchorPane albumView;
	@FXML Button createButton;
	@FXML Button deleteButton;
	@FXML Button renameButton;
	@FXML Text userText;

	ObservableList<String> list = FXCollections.observableArrayList();
	public void start() {
		//System.out.println("Album");
		userText.setText("User: " + Master.currentUser.name);
		updateList();
		albumListView.getSelectionModel().select(0);
	}
	
	private void updateList() {
		list.clear();
		for( String albumName : Master.currentUser.albumMap.keySet() ) {
			list.add(albumName);
		}
		albumListView.setItems(	list );
		return;
	}
	
	public void buttonPress( ActionEvent event ) throws IOException {
		Button b = (Button)event.getSource();
		String keyWord = "";
		if (b == createButton) {
			TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("List Album");
    		dialog.setHeaderText("Add Album");
    		dialog.setContentText("Enter name: ");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()) {
    			keyWord = result.get();
    			ArrayList<Picture> temp = new ArrayList<Picture>();
    			Master.currentUser.albumMap.put(keyWord, temp);
        		updateList();
    		}
		}else if (b == deleteButton) {
			keyWord = albumListView.getSelectionModel().getSelectedItem();
    		Master.currentUser.albumMap.remove(keyWord);
    		updateList();
		}else if (b == renameButton) {
			 String item = albumListView.getSelectionModel().getSelectedItem();
			 int index = albumListView.getSelectionModel().getSelectedIndex();
			 TextInputDialog dialog = new TextInputDialog(item);
			 dialog.setTitle("List Album");
			 dialog.setHeaderText("Selected Item (Index: " + index + ")");
			 dialog.setContentText("Enter new name: ");
			 Optional<String> result = dialog.showAndWait();
			 if (result.isPresent()) { 
				 list.set(index, result.get()); 
			 }
		}else if (b == logoutButton) {
			Master.writeData();
			Master.toLogin(albumView);
		}else if(b == openButton) {
			int selectIndex = albumListView.getSelectionModel().getSelectedIndex();
			Master.currentAlbum = list.get(selectIndex);
			Master.toThumbnail(albumView);
		}else {//quitButton
			Master.writeData();
    		Platform.exit();
		}
		
	}
	
}

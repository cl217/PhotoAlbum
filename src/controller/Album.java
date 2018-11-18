package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Picture;

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
	@FXML private Text errorText;;

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
		errorText.setVisible(false);
		Button b = (Button)event.getSource();
		String keyWord = "";
		int index=0;
		if (b == createButton) {
			TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("List Album");
    		dialog.setHeaderText("Add Album");
    		dialog.setContentText("Enter name: ");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()) {
    			keyWord = result.get();
    			String formatted = removeSpaces(result.get()); 
    			if(Master.currentUser.containsAlbum(formatted.toLowerCase())) {
    				errorText.setText("ERROR: ALBUM NAME ALREADY EXISTS");
    				errorText.setVisible(true);
    			}
    			else {
    				ArrayList<Picture> temp = new ArrayList<Picture>();
    				Master.currentUser.albumMap.put(formatted, temp);
    				updateList();
    			}
    		}
    		
    		for (String s : list) {
    			if( s.equals(keyWord.toLowerCase())) {
    				break;
    			}
    			index++;
    		}
    		
    		albumListView.getSelectionModel().select(index);
    		
		}else if (b == deleteButton) {
    		if (albumListView.getSelectionModel().getSelectedItem() == null) {
    			errorText.setVisible(true);
    			return;
    		}
    		else {
    			keyWord = albumListView.getSelectionModel().getSelectedItem();
    			index = albumListView.getSelectionModel().getSelectedIndex();
    			Master.currentUser.albumMap.remove(keyWord);
    			updateList();
    			
    			//System.out.println("Album size: " + Master.currentUser.albumMap.size()+ " , " + index);
    			if(Master.currentUser.albumMap.size()-1 < index) {
    				albumListView.getSelectionModel().select(Master.currentUser.albumMap.size()-1);
    			}
    			else{
    				albumListView.getSelectionModel().select(index);
    			}
    		}
		}else if (b == renameButton) {
    		if (albumListView.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("ERROR: NO ALBUM SELECTED");
    			errorText.setVisible(true);
    			return;
    		}
			 String item = albumListView.getSelectionModel().getSelectedItem();
			 index = albumListView.getSelectionModel().getSelectedIndex();
			 TextInputDialog dialog = new TextInputDialog(item);
			 dialog.setTitle("Photos");
			 dialog.setHeaderText("Rename album");
			 dialog.setContentText("Enter new name: ");
			 Optional<String> result = dialog.showAndWait();
			 if (result.isPresent()) { 
				String formatted = removeSpaces(result.get()); 
			   	if( !formatted.equals(item) && Master.currentUser.containsAlbum(formatted.toLowerCase())) {
		    		errorText.setText("ERROR: ALBUM NAME ALREADY EXISTS");
		    		errorText.setVisible(true);
		    	} else {
		    		list.set(index, formatted); 
		    	}
			 }
			 albumListView.getSelectionModel().select(index);
			 
		}else if (b == logoutButton) {
			Master.writeData();
			Master.toLogin(albumView);
		}else if(b == openButton) {
    		if (albumListView.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("ERROR: NO ALBUM SELECTED");
    			errorText.setVisible(true);
    			return;
    		}
			int selectIndex = albumListView.getSelectionModel().getSelectedIndex();
			Master.currentAlbum = list.get(selectIndex);
			Master.toThumbnail(albumView);
		}else {//quitButton
			Master.writeData();
    		Platform.exit();
		}
	}
	private String removeSpaces( String input ) {
		while(input.charAt(0)==' ') {
			input = input.substring(1, input.length());
		}
		while(input.charAt(input.length()-1)==' ') {
			input = input.substring(0, input.length()-1);
		}
		for( int i = 0; i < input.length(); i++ ) {
			if( input.charAt(i) == ' ' && input.charAt(i+1) == ' ' ) {
				input = input.substring(0, i) + input.substring(i+2, input.length());
			}
		}
		return input;
	}
}

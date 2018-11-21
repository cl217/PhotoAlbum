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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.AlbumObj;
import model.Picture;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 *
 */
public class Album {
	@FXML ListView<String> albumListView;
	@FXML Button logoutButton;
	@FXML Button openButton;
	@FXML Button quitButton;
	@FXML AnchorPane albumView;
	@FXML Button createButton;
	@FXML Button deleteButton;
	@FXML Button renameButton;
	@FXML Button detailsB;
	@FXML Text userText;
	@FXML Text errorText;

	ObservableList<String> list = FXCollections.observableArrayList();
	
	/**
	 * starts Album
	 */
	public void start() {
		////System.out.println("Album");
		userText.setText("User: " + Master.currentUser.name);
		updateList();
		albumListView.getSelectionModel().select(0);
	}
	
	private void updateList() {
		list.clear();
		for( AlbumObj a : Master.currentUser.albumMap.values() ) {
			list.add(a.name);
		}
		albumListView.setItems(	list );
		return;
	}
	
	/**
	 * 
	 * @param event a button is pressed
	 * @throws IOException no stage
	 */
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
    			if( formatted.equals(" ")) {
    				errorText.setText("ERROR: INVALID ALBUM NAME");
    				errorText.setVisible(true);
    			}
    			else if(Master.currentUser.containsAlbum(formatted.toLowerCase())) {
    				errorText.setText("ERROR: ALBUM NAME ALREADY EXISTS");
    				errorText.setVisible(true);
    			}
    			else {
    				ArrayList<Picture> temp = new ArrayList<Picture>();
    				AlbumObj album = new AlbumObj(formatted, temp);
    				Master.currentUser.albumMap.put(formatted.toLowerCase(), album);
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
    			Master.currentUser.albumMap.remove(keyWord.toLowerCase());
    			updateList();
    			
    			////System.out.println("Album size: " + Master.currentUser.albumMap.size()+ " , " + index);
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
				if( formatted.equals(" ")) {
					errorText.setText("ERROR: INVALID ALBUM NAME");
					errorText.setVisible(true);
					return;
				}
			   	if( !formatted.equalsIgnoreCase(item) && Master.currentUser.containsAlbum(formatted.toLowerCase())) {
		    		errorText.setText("ERROR: ALBUM NAME ALREADY EXISTS");
		    		errorText.setVisible(true);
		    	} else {
		    		AlbumObj temp = Master.currentUser.albumMap.get(item.toLowerCase());
		    		Master.currentUser.albumMap.remove(item);
		    		temp.name = formatted;
		    		Master.currentUser.albumMap.put(formatted.toLowerCase(), temp);
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
			Master.currentAlbum = Master.currentUser.albumMap.get(list.get(selectIndex).toLowerCase());
			Master.toThumbnail(albumView);
		}else {//quitButton
			Master.writeData();
    		Platform.exit();
		}
	}
	
	/**
	 * shows details of album
	 * @param event details button is pressed
	 */
	public void detailsPopup(ActionEvent event) {
		if(albumListView.getSelectionModel().isEmpty()) {
			errorText.setText("ERROR: NO ALBUM SELECTED");
			errorText.setVisible(true);
			return;
		}
		AlbumObj cAlbum = Master.currentUser.albumMap.get(albumListView.getSelectionModel().getSelectedItem().toLowerCase());
		Alert popup = new Alert(AlertType.INFORMATION);
		popup.setTitle("Photos");
		popup.setHeaderText("Album Information");
		if( cAlbum.album.isEmpty()) {
			popup.setContentText("Total Pictures: " + cAlbum.album.size()
									+ "\n" + "Earliest Picture: N/A"
									+ "\n" + "Latest Picture: N/A" );
		}else {
			popup.setContentText("Total Pictures: " + cAlbum.album.size()
									+ "\n" + "Earliest Picture: " + cAlbum.earliest.toString()
									+ "\n" + "Latest Picture: " + cAlbum.latest.toString() );
		}
		popup.showAndWait();
	}
	
	/**
	 * formats strings
	 * @param unformatted String
	 * @return formatted String
	 */
	private String removeSpaces( String input ) {
		while(input.charAt(0)==' ' && input.length() != 1 ) {
			input = input.substring(1, input.length());
		}
		while(input.charAt(input.length()-1)==' ' && input.length() != 1 ) {
			input = input.substring(0, input.length()-1);
		}
		for( int i = 0; i < input.length()-1; i++ ) {
			if( input.charAt(i) == ' ' && input.length() != 1 && input.charAt(i+1) == ' ' ) {
				input = input.substring(0, i) + input.substring(i+2, input.length());
			}
		}
		return input;
	}
}

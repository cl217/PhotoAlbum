package controller;

import java.io.*;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.User;

public class Admin{
	
	@FXML Button createButton;
	@FXML Button deleteButton;
	@FXML Button loButton; //log out button
	@FXML Button quitButton;
	@FXML AnchorPane adminView;
	@FXML ListView<String> listUsers;
	@FXML private Text errorText;
	@FXML private Text errorDeleteText;
	
	private ObservableList<String> obsList = FXCollections.observableArrayList();
	public void start(){
		System.out.println("stockNum: " + Master.userMap.size());
		updateList();
		listUsers.getSelectionModel().select(0);
	}
	
	private void updateList() {
		obsList.clear();
		for( String key: Master.userMap.keySet()) {
			obsList.add(key);
		}
		listUsers.setItems(obsList);
		return;
	}
	
	public void buttonPress(ActionEvent event) throws IOException{
    	/*create pop up for buttonPress entry
    	 * some how get access to those fields to get (key, value)
    	 */
		errorText.setVisible(false);
		errorDeleteText.setVisible(false);
		String keyWord = "";
		int index= 0;
		User newUser = null;
		Button b = (Button)event.getSource();
		
    	if (b == createButton) {
    		//create newUser with pop up input
    		TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("List User");
    		dialog.setHeaderText("Add User");
    		dialog.setContentText("Enter name: ");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()) {
    			keyWord = removeSpaces(result.get());
    			if(Master.userMap.containsKey(keyWord.toLowerCase())) {
    				errorText.setVisible(true);
    			}
    			else {
    				newUser = new User(keyWord);
    				Master.userMap.put(keyWord.toLowerCase(), newUser);
            		updateList();
    			}
    		}
    		
    		for (String s : obsList) {
    			if( s.equals(keyWord.toLowerCase())) {
    				break;
    			}
    			index++;
    		}
    		
    		listUsers.getSelectionModel().select(index);
    	}
    	else if (b == deleteButton) {
    		if (listUsers.getSelectionModel().getSelectedItem() == null) {
    			errorDeleteText.setVisible(true);
    		}
    		else{
    			keyWord = listUsers.getSelectionModel().getSelectedItem();
    			index = listUsers.getSelectionModel().getSelectedIndex();
    			Master.userMap.remove(keyWord);
    			Master.data.userList.remove(keyWord);
    			updateList();
    		
    			if(Master.userMap.size()-1 < index) {
    				listUsers.getSelectionModel().select(Master.userMap.size()-1);
    			}
    			else{
    				listUsers.getSelectionModel().select(index);
    			}
    		}
    	}
    	else if (b == loButton) {
    		//writeApp(adminUser);
    		Master.toLogin(adminView);
    	}
    	else { //quitButton
			Master.writeData();
    		Platform.exit();
    		//close the window
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

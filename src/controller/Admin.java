package controller;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
	
	
	/**
	 * delete/add to Master.userMap
	 * 
	 */
	
	
	//public static HashMap<String, User> allUsers = new HashMap<String, User>();

	private ObservableList<String> obsList = FXCollections.observableArrayList();
	
	/*
	public static final String storeDir = "dat";
	public static final String storeFile = "user.dat"; 
	*/
	
	public void start(){
		//adminUser = Asdmin.readApp();
		//select the first item
    	//listUsers.getSelectionModel().select(0);
		System.out.println("Admin.start1");
		System.out.println(Master.userMap.size());
		for( String key: Master.userMap.keySet()) {
			obsList.add(key);
		}
		listUsers.setItems(obsList);
		
	}
	
	private void updateList() {
		//obsList.clear();
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
		String keyWord = "";
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
    			keyWord = result.get();
    			newUser = new User(result.get());
    			Master.userMap.put(keyWord, newUser);
        		updateList();
    		}
    	}
    	else if (b == deleteButton) {
    		keyWord = listUsers.getSelectionModel().getSelectedItem();
    		Master.userMap.remove(keyWord);
    		updateList();
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
	
	/*
	public static void writeApp(Admin adminUser)  throws IOException { 
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( storeDir + File.separator + storeFile ));
		oos.writeObject(adminUser);
		oos.close();
	}
	
	public static Admin readApp()  throws IOException, ClassNotFoundException { 
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream( storeDir + File. separator + storeFile ));
		Admin adminUser = (Admin)ois.readObject();
		ois.close();
		return adminUser;
	}
	*/
	
}

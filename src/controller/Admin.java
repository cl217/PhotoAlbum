package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.User;

public class Admin implements Serializable{
	
	@FXML Button createButton;
	@FXML Button deleteButton;
	@FXML Button loButton; //log out button
	@FXML Button quitButton;
	@FXML Text invalid;
	@FXML AnchorPane adminView;
	@FXML ListView<User> listUsers;
	public static HashMap<String, User> allUsers = new HashMap<String, User>();
	private ObservableList<User> obsList = FXCollections.observableArrayList();
	
	public static final String storeDir = "dat";
	public static final String storeFile = "user.dat"; 
	
	public void start() throws Exception{
		Admin adminUser = Admin.readApp();
		writeApp(adminUser);
	}
	
	private void updateList() {
		obsList.clear();
		for(Map.Entry<String, User> entry : allUsers.entrySet()) {
			obsList.add(entry.getValue());
		}
		listUsers.setItems(obsList);
		return;
	}
	
	public void buttonPress(ActionEvent event) throws FileNotFoundException {
    	/*create pop up for buttonPress entry
    	 * some how get access to those fields to get (key, value)
    	 */
	   
    	Button b = (Button)event.getSource();

    	if (b == createButton) {
    		allUsers.put(key, value);
    		updateList();
    	}
    	else if (b == deleteButton) {
    		allUsers.remove(key);
    		updateList();
    	}
    	else if (b == loButton) {
    		ToView.login(adminView);
    	}
    	else { //quitButton
    		
    	}
	}
	
	public static void writeApp(Admin adminUser)  throws IOException { 
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( storeDir + File.separator + storeFile ));
		oos.writeObject(adminUser);
	}
	
	public static Admin readApp()  throws IOException, ClassNotFoundException { 
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream( storeDir + File. separator + storeFile ));
		Admin adminUser = (Admin)ois.readObject();
		return adminUser;
	}
	
}

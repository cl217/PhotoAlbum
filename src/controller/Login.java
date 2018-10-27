package controller;

import model.*;
import java.io.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.*;
import javafx.collections.ObservableList;
import javafx.event.*;

public class Login {
	@FXML Button loginButton;
	@FXML TextField userPrompt;
	@FXML Text invalid;
	@FXML AnchorPane loginView;
	
	//get user from stored data
	public static User user;
	public static HashMap<String, User> allUsers = new HashMap<String, User>();
	
	public void start() {
		invalid.setVisible(false);
		testing();

	}
	
	//manually inputting data for testing purposes
	public void testing() {
		User cindy = new User( "cindy" );
		allUsers.put("cindy", cindy );
		ObservableList<String> albumList = cindy.albumList;
		albumList.add("Stock");
		albumList.add("Cats");
		
		User person = new User( "person" );
		allUsers.put("person", person );
		albumList = person.albumList;
		albumList.add("Stock");
		albumList.add("dogs");
		albumList.add("random album name");
	}
	
	//goes to album view
	public void buttonPress(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		if (b == loginButton) {
			String input = userPrompt.getText();
			if( allUsers.containsKey( input ) ) {
				user = allUsers.get(input);
				//toAlbumView();
				ToView.album(loginView);
			}else {
				invalid.setVisible(true);
			}
		} 
	}	
}

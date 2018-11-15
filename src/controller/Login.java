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
	@FXML Button quitButton;

	public void start() {
		invalid.setVisible(false);
	}
	
	//goes to album view
	public void buttonPress(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		System.out.println("button pressed");
		if( b == quitButton ) {
			System.out.println("quit");
			Master.writeData();
		}
		
		if (b == loginButton) {
			String input = userPrompt.getText();
			if( input.equalsIgnoreCase("admin") ) {
				Master.toAdmin(loginView);
			}
			if( Master.userMap.containsKey( input ) ) {
				Master.currentUser = Master.userMap.get(input);
				System.out.println("6");
				Master.toAlbum(loginView);
			}else {
				invalid.setVisible(true);
			}
		} 
	}	
}

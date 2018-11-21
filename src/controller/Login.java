package controller;

import java.io.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.*;
import javafx.application.Platform;
import javafx.event.*;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class Login {
	@FXML Button loginButton;
	@FXML TextField userPrompt;
	@FXML Text invalid;
	@FXML AnchorPane loginView;
	@FXML Button quitButton;

	/**
	 * starts login window
	 */
	public void start() {
		invalid.setVisible(false);
	}
	
	/**
	 * handles button presses
	 * @param e a button is pressed
	 * @throws IOException no stage
	 */
	public void buttonPress(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		//System.out.println("button pressed");
		if( b == quitButton ) {
			//System.out.println("quit");
			Master.writeData();
			Platform.exit();
		}
		
		if (b == loginButton) {
			String input = userPrompt.getText();
			if( input.equalsIgnoreCase("admin") ) {
				Master.toAdmin(loginView);
			}
			if( Master.userMap.containsKey( input ) ) {
				Master.currentUser = Master.userMap.get(input);
				//System.out.println("6");
				Master.toAlbum(loginView);
			}else {
				invalid.setVisible(true);
			}
		} 
	}	
}

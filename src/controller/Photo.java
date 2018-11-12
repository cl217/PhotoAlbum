package controller;

import java.io.*;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Picture;

public class Photo {
	@FXML private ImageView picture;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Text captionText;
    @FXML private Text tagText;
    @FXML private Text dateText;
    @FXML private Text usernameText;
    @FXML private Button logoutButton;
    @FXML private Button quitButton;
    @FXML private AnchorPane photoView;
    
    int currentIndex;
    ArrayList<Picture> album = Master.currentUser.albumMap.get(Master.currentAlbum);
    
    public void start(int picIndex)throws IOException {
    	System.out.println("Photo.start");
    	System.out.println(album.size());
    	update(picIndex);
    	usernameText.setText("User: " + Master.currentUser.name);
	}
    private void update(int index) {
    	System.out.println("update");
    	Image image = new Image( album.get(index).url );
    	picture.setImage(image);
    	captionText.setText(album.get(index).caption);
    	currentIndex = index;
    }
	public void buttonPress(ActionEvent event) throws IOException {
		Button b = (Button) event.getSource();
		if( b == prevButton ) {
			System.out.println("prevButton1");
			if( currentIndex != 0 ) {
				System.out.println("prevButton2");
				update(currentIndex-1);
			}
		}
		if( b == nextButton ) {				
			System.out.println("nextButton");
			if( currentIndex != album.size()-1 ) {
				System.out.println("nextButton2");
				update(currentIndex+1);
			}
		}
		if( b == logoutButton ) {
			Master.writeData();
			Master.toLogin(photoView);
		}
		if( b == quitButton ) {
			Master.writeData();
			Platform.exit();
		}

    }
}

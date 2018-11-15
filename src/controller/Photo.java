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
    @FXML private Button backButton;
    
    int currentIndex;
    ArrayList<Picture> album = Master.currentUser.albumMap.get(Master.currentAlbum);
    
    public void start(int picIndex)throws IOException {
    	update(picIndex);
    	usernameText.setText("User: " + Master.currentUser.name);
	}
    private void update(int index) {
    	System.out.println("update");
    	Image image = new Image( album.get(index).url );
    	picture.setImage(image);
    	captionText.setText(album.get(index).caption);
    	dateText.setText(album.get(index).date.toString());
    	String tags = new String();
    	for( String key : album.get(index).tags.keySet() ) {
    		for( String tag: album.get(index).tags.get(key) ) {
    			tags = tags + key + '=' + tag + " ";
    		}
    	}
    	tagText.setText(tags);
    	currentIndex = index;
    }
    
    @FXML
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
		if( b == backButton ) {
			Master.toThumbnail(photoView);
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

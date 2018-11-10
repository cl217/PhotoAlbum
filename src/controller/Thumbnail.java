package controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Thumbnail {
    @FXML private AnchorPane thumbnailView;
    @FXML private GridPane grid;
    @FXML private Button addPictureButton;
    @FXML private Button removePictureButton;
    @FXML private Button editCaptionButton;
    @FXML private Button modifyTagsButton;
    @FXML private Button copyButton;
    @FXML private Button moveButton;
    @FXML private Button logoutButton;
    @FXML private Button quitButton;
    @FXML private Button searchButton;

    @FXML
    void buttonPress(ActionEvent event) {

    }
    
	public void start() {
		System.out.println("Thumbnail.start");
		ArrayList<String> album = Master.currentUser.albumMap.get(Master.currentAlbum);
		System.out.println("stockSize: " + Master.userMap.get("stock").albumMap.get("Stock").size());
		System.out.println(Master.currentAlbum);
		System.out.println(album.size());
		for( String str : album ) {
			System.out.println(str);
		}
	}
}

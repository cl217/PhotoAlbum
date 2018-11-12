package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Picture;
import javafx.stage.Stage.*;

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
    @FXML private Button openButton;


	ArrayList<Picture> album = Master.currentUser.albumMap.get(Master.currentAlbum);
	int selectedIndex;
	
	public void start() {
		update();
	}

	public void update() {
		grid.getChildren().clear();
		int col = 0;
		int row = 0;
		int i = 0;
	     grid.getColumnConstraints().add(new ColumnConstraints(-15)); 
	     grid.getRowConstraints().add(new RowConstraints(175)); 
		for( Picture p : album ) {
			System.out.println("tbnLoop: start");
			GridPane innerGrid = new GridPane();
			ImageView pic = new ImageView();
			pic.setFitHeight(150);
			pic.setFitWidth(150);
			Image image = new Image(p.url);
			pic.setImage(image);
			innerGrid.add(pic, 0, 0);
			
			Text text = new Text();
			text.setText(p.caption);
			innerGrid.add(text, 0, 1);
			
			Button button = new Button();
			button.setId(Integer.toString(i));
			button.setGraphic(innerGrid);
			button.setOnAction( event -> picClick(event) );
			
			grid.add(button, col, row );
			col++;
			if( col == 4 ) {
				col = 0;				
				row++;
			}
			i++;
		}	
	}
	
	public void picClick(ActionEvent event) {
		System.out.println("picture clicked");
		Button b = (Button) event.getSource();
		selectedIndex = Integer.parseInt(b.getId());
	}
	
	public void buttonPress( ActionEvent event ) throws IOException {
		Button b = (Button) event.getSource();
		if( b == openButton ) {
			System.out.println("openButton");
			Master.toPhoto(thumbnailView, selectedIndex);
		}
		if( b == addPictureButton ) {
			addPicture();
		}
		if( b == removePictureButton ) {
			album.remove(selectedIndex);
			System.out.println(album.size());
			update();
		}
		if( b == editCaptionButton ) {}
		if( b == modifyTagsButton ) {}
		if( b == copyButton ) {
			String toAlbum = pickAlbum("Copy to:");
			Master.currentUser.albumMap.get(toAlbum).add(album.get(selectedIndex));
		}
		if( b == moveButton ) {
			String toAlbum = pickAlbum("Move to:");
			Master.currentUser.albumMap.get(toAlbum).add(album.get(selectedIndex));
			album.remove(selectedIndex);
			update();
		}		
		if( b == searchButton ) {}	
		if( b == logoutButton ) {
			Master.writeData();
			Master.toLogin(thumbnailView);
		}
		if( b == quitButton ) {
			Master.writeData();
			Platform.exit();
		}
	}
	
	private void addPicture() {
		System.out.println("addPicture");
		Stage stage = (Stage) thumbnailView.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File selectedFile = fileChooser.showOpenDialog( stage );
		
		if( selectedFile !=  null ) {
			System.out.println(selectedFile.getPath());
			Picture pic = new Picture();
			pic.setURL(selectedFile.getPath());
			//new popup for tags and captions
			album.add(pic);
			update();
		}
	}
	
	private String pickAlbum(String title) {
		System.out.println("Popup");
		ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>();
		choiceDialog.setTitle("Photos");
		choiceDialog.setHeaderText(title);
		for( String album : Master.currentUser.albumMap.keySet() ) {
			if( !album.equals(Master.currentAlbum) ){
			choiceDialog.getItems().add(album);
			}
		}
		/*
		choiceDialog.showingProperty().addListener((ov, b, b1) -> {
		    if (b1) {
		        choiceDialog.setContentText("");
		    }else {
		        choiceDialog.setContentText(null);
		    }

		    //or 
		    
		    if (b1) {
		        Node comboBox = choiceDialog.getDialogPane().lookup(".combo-box");
		        comboBox.requestFocus();
		    }
		    
		});
		*/
		choiceDialog.showAndWait();
		return choiceDialog.getSelectedItem();
	}
}


package controller;

import java.io.*;
import java.util.*;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Picture;

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
	ArrayList<Picture> filteredAlbum = new ArrayList<Picture>();
	HashMap<String, ArrayList<Picture>> tags = new HashMap<String, ArrayList<Picture>>();
	//tag type to get Arraylist of pics that have that tag type
	//cant have 2 tags to location type for a pic but can have 2 tags to a person type for a single pic
	//??????
	int selectedIndex;
	
	public void start() {
		update(album);
		//updateTagsMap();
	}

	public void update( ArrayList<Picture> displayAlbum ) {
		grid.getChildren().clear();
		int col = 0;
		int row = 0;
		int i = 0;
	     grid.getColumnConstraints().add(new ColumnConstraints(-15)); 
	     grid.getRowConstraints().add(new RowConstraints(175)); 
	     
		for( Picture p : displayAlbum ) {
			
			//Makes tile
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
			
			for( String tagType : p.tags.keySet() ) {
				if( !tags.containsKey(tagType) ) {
					ArrayList<Picture> pics = new ArrayList<Picture>(); 
					tags.put(tagType, pics);
				}
				if( !tags.get(tagType).contains(p) ) {
					tags.get(tagType).add(p);
				}
			}
			
			col++;
			if( col == 4 ) {
				col = 0;				
				row++;
			}
			i++; //sets index to button
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
			//delete from tagsMap
			for( String tagType : album.get(selectedIndex).tags.keySet() ) {
				tags.get(tagType).remove(album.get(selectedIndex));
			}
			album.remove(selectedIndex);
			update(album);
		}
		if( b == editCaptionButton ) {}
		if( b == modifyTagsButton ) {
			Master.toTag(thumbnailView, album.get(selectedIndex));
			//modifyTags();
		}
		if( b == copyButton ) {
			String toAlbum = pickAlbum("Copy to:");
			Master.currentUser.albumMap.get(toAlbum).add(album.get(selectedIndex));
		}
		if( b == moveButton ) {
			String toAlbum = pickAlbum("Move to:");
			Master.currentUser.albumMap.get(toAlbum).add(album.get(selectedIndex));
			for( String tagType : album.get(selectedIndex).tags.keySet() ) {
				tags.get(tagType).remove(album.get(selectedIndex));
			}
			album.remove(selectedIndex);
			update(album);
		}		
		if( b == searchButton ) {
			//tags
			//for 1-2 tags
				//search
			update(filteredAlbum);
			
			//search by date...
		}	
		if( b == logoutButton ) {
			Master.writeData();
			Master.toLogin(thumbnailView);
		}
		if( b == quitButton ) {
			Master.writeData();
			Platform.exit();
		}
	}
	
	private void searchByTag( String tagType, String value ) {
		for( Picture p: tags.get(tagType) ) {
			if( p.tags.get(tagType).contains(value) ) {
				filteredAlbum.add(p);
			}
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
			update(album);
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
		choiceDialog.showAndWait();
		return choiceDialog.getSelectedItem();
	}
	
/**	
 * ******************TAGVIEW STUFF***************************************************
 */
	
	
}


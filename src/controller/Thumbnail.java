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
    @FXML private ChoiceBox<String> searchDrop;
    @FXML private TextField tagSearch;
    @FXML private DatePicker dateSearch1;
    @FXML private DatePicker dateSearch2;
    

	ArrayList<Picture> album = Master.currentUser.albumMap.get(Master.currentAlbum);
	ArrayList<Picture> filteredAlbum = new ArrayList<Picture>();
	HashMap<String, ArrayList<Picture>> tags = new HashMap<String, ArrayList<Picture>>();
	//tag type to get Arraylist of pics that have that tag type
	//cant have 2 tags to location type for a pic but can have 2 tags to a person type for a single pic
	//??????
	int selectedIndex;
	
	public void start() {
		update(album);
		resetSearch();
    	searchDrop.getItems().add("Search by:");
    	searchDrop.getItems().add("Tags");
    	searchDrop.getItems().add("Date");
		searchDrop.getSelectionModel().selectedItemProperty().addListener( (obs, oldVal, newVal) ->updateSearch());
	}

	private void updateSearch() {
		tagSearch.setVisible(false);
		dateSearch1.setVisible(false);
		dateSearch2.setVisible(false);
		if (searchDrop.getSelectionModel().getSelectedItem().equals("Tags")) {
			tagSearch.setVisible(true);
			tagSearch.setEditable(true);
		}
		if( searchDrop.getSelectionModel().getSelectedItem().equals("Date")) {
			dateSearch1.setVisible(true);
			dateSearch2.setVisible(true);
		}
	}
    private void resetSearch() {
		tagSearch.setVisible(false);
		dateSearch1.setVisible(false);
		dateSearch2.setVisible(false);
    	searchDrop.setValue("Search by:");
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
			if( searchDrop.getSelectionModel().getSelectedItem().equals("Tags")) {
				searchByTag();
			}
			
			if( searchDrop.getSelectionModel().getSelectedItem().equals("Date")) {
				
			}
			update(filteredAlbum);
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
	
	private void searchByTag() {
		String input = tagSearch.getText().toLowerCase();
		String type = new String();
		String value = new String();
		System.out.println(input);
		if( (input.contains("or") && input.charAt(input.indexOf("or")-1) == ' ' && input.charAt(input.indexOf("or")+2) == ' ') 
				|| (input.contains("and") && input.charAt(input.indexOf("and")-1) == ' ' && input.charAt(input.indexOf("and")+2) == ' ') ) {
			System.out.println("two tags");
			String type2 = new String();
			String value2 = new String();
			if(input.contains("or")){
				System.out.println("or");
				String part1 = input.substring(0, input.indexOf("or")-1);
				System.out.println(part1);
				type = part1.substring(0, input.indexOf("="));
				System.out.println(type);
				value = part1.substring(input.indexOf("=")+1, input.length());
				System.out.println(value);
				
				String part2 = input.substring(input.indexOf("or")+1, input.length());
				System.out.println(part2);
				type2 = part2.substring(0, input.indexOf("="));
				System.out.println(type2);
				value2 = part2.substring(input.indexOf("=")+1, input.length());
				System.out.println(value2);
				for( Picture pic : album ) {
					if( (pic.tags.containsKey(type) && pic.tags.get(type).contains(value)) || (pic.tags.containsKey(type2) && pic.tags.get(type2).contains(value2)) ) {
						filteredAlbum.add(pic);
					}
				}
			}
			else {
				System.out.println("and");
				String part1 = input.substring(0, input.indexOf("and")-1);
				System.out.println(part1);
				type = part1.substring(0, input.indexOf("="));
				System.out.println(type);
				value = part1.substring(input.indexOf("=")+1, input.length());
				System.out.println(value);
				
				String part2 = input.substring(input.indexOf("and")+1, input.length());
				type2 = part2.substring(0, input.indexOf("="));
				value2 = part2.substring(input.indexOf("=")+1, input.length());
				for( Picture pic : album ) {
					if( (pic.tags.containsKey(type) && pic.tags.get(type).contains(value)) && (pic.tags.containsKey(type2) && pic.tags.get(type2).contains(value2)) ) {
						filteredAlbum.add(pic);
					}
				}
			}
		}
		else {
			System.out.println("one tag");
			type = input.substring(0, input.indexOf("="));
			value = input.substring(input.indexOf("=")+1, input.length());
			for(Picture pic : album) {
				if( pic.tags.containsKey(type) && pic.tags.get(type).contains(value) ){
					filteredAlbum.add(pic);
				}
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
	

	
	
}


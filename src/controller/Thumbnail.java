package controller;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
    @FXML private Text userText;
    @FXML private Text albumText;
    @FXML private Text errorText;
    

	ArrayList<Picture> album = Master.currentUser.albumMap.get(Master.currentAlbum);
	ArrayList<Picture> filteredAlbum = new ArrayList<Picture>();
	HashMap<String, ArrayList<Picture>> tags = new HashMap<String, ArrayList<Picture>>();
	//tag type to get Arraylist of pics that have that tag type
	//cant have 2 tags to location type for a pic but can have 2 tags to a person type for a single pic
	//??????
	int selectedIndex;
	
	public void start() {
		errorText.setVisible(false);
		userText.setText("user: " + Master.currentUser.name);
		albumText.setText(Master.currentAlbum);
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
		if( b == editCaptionButton ) {
			editCaption();
		}
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
				searchByDate();
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
	
	private void editCaption() {
		TextInputDialog dialog = new TextInputDialog(album.get(selectedIndex).caption);
		dialog.setTitle("Photos");
		dialog.setHeaderText("Edit Caption: ");
		dialog.setContentText("Caption: ");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			album.get(selectedIndex).caption = result.get();
		}
		update(album);
	}
	private void searchByTag() {
		String input = tagSearch.getText().toLowerCase();
		//  |0: type1|1: value1|2: andor|3: type2|4: value2|
		String[] words = new String[5];
		
		//delete all extra spaces
		for( int i = 0; i < input.length(); i++ ) {
			if( input.charAt(i) == ' ' && input.charAt(i+1) == ' ' ) {
				input = input.substring(0, i) + input.substring(i+2, input.length());
			}
		}
		if( input.contains(" or ") || input.contains(" and ") ) {
			System.out.println("or/and");
			words[0] = input.substring(0, input.indexOf("=")); //type1
			if( input.contains(" or ")) {
				//System.out.println("or");
				words[1] = input.substring(input.indexOf("=")+1, input.indexOf(" or ")); //value1
				words[2] = "or"; //or
				words[3] = input.substring(input.indexOf(" or ")+4, input.indexOf("=", input.indexOf("=")+1)); //type2
				words[4] = input.substring(input.indexOf("=", input.indexOf("=")+1)+1, input.length());
				for( Picture pic: album ) {
					if( (pic.tags.containsKey(words[0]) && pic.tags.get(words[0]).contains(words[1]) ) 
							|| (pic.tags.containsKey(words[3]) && pic.tags.get(words[3]).contains(words[4])) ) {
						filteredAlbum.add(pic);
					}
				}
			}else { //and
				words[1] = input.substring(input.indexOf("=")+1, input.indexOf(" and ")); //value1
				words[2] = "and";
				words[3] = input.substring(input.indexOf(" and ")+4, input.indexOf("=", input.indexOf("=")+1)); //type2
				words[4] = input.substring(input.indexOf("=", input.indexOf("=")+1)+1, input.length());
				for( Picture pic: album ) {
					if( (pic.tags.containsKey(words[0]) && pic.tags.get(words[0]).contains(words[1]) ) 
							&& (pic.tags.containsKey(words[3]) && pic.tags.get(words[3]).contains(words[4])) ) {
						filteredAlbum.add(pic);
					}
				}
			}
		}else { //1 tag
			words[1] = input.substring(input.indexOf("=")+1, input.length());
			for( Picture pic: album ) {
				if( (pic.tags.containsKey(words[0]) && pic.tags.get(words[0]).contains(words[1]) ) ) {
					filteredAlbum.add(pic);
				}
			}
		}
	}
	
	private void searchByDate() {
		LocalDate date1 = dateSearch1.getValue();
		LocalDate date2 = dateSearch1.getValue();
		for( Picture pic : album ) {
			if( pic.date.equals(date1) || pic.date.equals(date2) ) {
				filteredAlbum.add(pic);
			}
			if( pic.date.isAfter(date1) && pic.date.isBefore(date2) ) {
				filteredAlbum.add(pic);
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
			Date date = new Date(selectedFile.lastModified());
			LocalDate localD = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			pic.date = localD;
			System.out.println(pic.date);
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


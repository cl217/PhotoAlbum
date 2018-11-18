package controller;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
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
    @FXML private Button resultAlbumB;
    @FXML private Button resetB;
    @FXML private Button backB;
    @FXML private ChoiceBox<String> searchDrop;
    @FXML private TextField tagSearch;
    @FXML private DatePicker dateSearch1;
    @FXML private DatePicker dateSearch2;
    @FXML private Text userText;
    @FXML private Text albumText;
    @FXML private Text errorText;
    @FXML private ScrollPane scrollPane;

    

	ArrayList<Picture> album = Master.currentUser.albumMap.get(Master.currentAlbum);
	ArrayList<Picture> filteredAlbum = new ArrayList<Picture>();
	HashMap<String, ArrayList<Picture>> tags = new HashMap<String, ArrayList<Picture>>();
	//tag type to get Arraylist of pics that have that tag type
	//cant have 2 tags to location type for a pic but can have 2 tags to a person type for a single pic
	//??????
	int selectedIndex;
	int filteredIndex;
	ObservableList<Node> gridB = FXCollections.observableArrayList();
	
	public void start() {
		errorText.setVisible(false);
		userText.setText("user: " + Master.currentUser.name);
		albumText.setText(Master.currentAlbum);
		update(album);
		if( gridB.isEmpty() == false) {
			gridB.get(0).requestFocus();
		}
		resetSearch();
    	searchDrop.getItems().add("Search by:");
    	searchDrop.getItems().add("Tags");
    	searchDrop.getItems().add("Date");
		searchDrop.getSelectionModel().selectedItemProperty().addListener( (obs, oldVal, newVal) ->updateSearch());
	}
	public void revert(MouseEvent event) {
		resetSelect();
	}

	private void updateSearch() {
		System.out.println("updateSearch");
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
		resetSelect();
	}
	
    private void resetSearch() {
		tagSearch.setVisible(false);
		dateSearch1.setVisible(false);
		dateSearch2.setVisible(false);
    	searchDrop.setValue("Search by:");
    }
    
	public void update( ArrayList<Picture> displayAlbum ) {
		grid.getChildren().clear();
		grid.getColumnConstraints().clear();
		//grid.getRowConstraints().clear();
		int col = 0;
		int row = 0;
		int i = 0;
	    grid.getColumnConstraints().add(new ColumnConstraints(166)); 
	    grid.getRowConstraints().add(new RowConstraints(175)); 
	    
		for( Picture p : displayAlbum ) {
			//Makes tile
			//System.out.println(p.url);
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
		gridB = grid.getChildren();
	}
	
	
	public void picClick(ActionEvent event) {
		System.out.println("picture clicked");
		Button b = (Button) event.getSource();
		selectedIndex = Integer.parseInt(b.getId());
	}
	
	public void buttonPress( ActionEvent event ) throws IOException {
		System.out.println("buttonPress1");
		errorText.setVisible(false);
		Button b = (Button) event.getSource();
		if(gridB.size()==0 ) {
			errorText.setText("NO PICTURE SELECTED");
			errorText.setVisible(true);
			return;
		}
		
		filteredIndex = selectedIndex;
		if( resetB.isVisible() ) {
			for( int i = 0; i < album.size(); i++ ) {
				if( filteredAlbum.get(selectedIndex).equals(album.get(i)) ) {
					selectedIndex = i;
					break;
				}
			}
		}
		
		if( b == openButton ) {
			System.out.println("openButton");
			if( resetB.isVisible() ) {
				Master.toPhoto(thumbnailView, filteredIndex, filteredAlbum );
			}else {
				Master.toPhoto(thumbnailView, selectedIndex, album );
			}
			return;
		}
		if( b == removePictureButton ) {
			for( String tagType : album.get(selectedIndex).tags.keySet() ) {
				tags.get(tagType).remove(album.get(selectedIndex));
			}
			album.remove(selectedIndex);
			update(album);
			if( resetB.isVisible() ) {
				filteredAlbum.remove(filteredIndex);
				update(filteredAlbum);
				selectedIndex = filteredIndex;
			}
			if( selectedIndex == gridB.size() ) {
				selectedIndex = selectedIndex-1;
			}
			if( gridB.isEmpty() == false ) {
				gridB.get(selectedIndex).requestFocus();
			}
			return;
		}
		if( b == editCaptionButton ) {
			if( resetB.isVisible()) {
				editCaption(true);
			}else {
				editCaption(false);
			}
		}
		if( b == modifyTagsButton ) {
			Master.toTag(thumbnailView, album.get(selectedIndex));
		}
		if( b == copyButton ) {
			String toAlbum = pickAlbum("Copy to:");
			if(toAlbum != null) {
				Master.currentUser.albumMap.get(toAlbum).add(album.get(selectedIndex));
			}
		}
		if( b == moveButton ) {
			String toAlbum = pickAlbum("Move to:");
			if( toAlbum != null ) {
				System.out.println("moved");
				Master.currentUser.albumMap.get(toAlbum).add(album.get(selectedIndex));
				for( String tagType : album.get(selectedIndex).tags.keySet() ) {
					tags.get(tagType).remove(album.get(selectedIndex));
				}
				album.remove(selectedIndex);
				update(album);
				if(resetB.isVisible()) {
					filteredAlbum.remove(filteredIndex);
					update(filteredAlbum);
				}
			}
		}		

		if( b == resultAlbumB ) {
			makeResultAlbum();
		}
		if( gridB.isEmpty() == false ) {
			if( resetB.isVisible() ) {
				gridB.get(filteredIndex).requestFocus();
				selectedIndex = filteredIndex;
			}else {
				gridB.get(selectedIndex).requestFocus();
			}
		}
	}
	
	private void resetSelect(){
		if(!gridB.isEmpty()) {
			gridB.get(selectedIndex).requestFocus();
		}
	}
	
	public void buttonPress2(ActionEvent e) throws IOException {
		errorText.setVisible(false);
		System.out.println("buttonPress2");
		Button b = (Button) e.getSource();
		if( b == addPictureButton ) {
			if(addPicture()) {
				selectedIndex = gridB.size()-1;
			}
		}
		if( b == searchButton ) {

			if(searchDrop.getSelectionModel().getSelectedIndex()==0) {
				errorText.setText("ERROR: NO SEARCH VALUE");
				resetSelect();
				return;
			}
			if( searchDrop.getSelectionModel().getSelectedItem().equals("Tags")) {
				if( tagSearch.getText().contains("=") == false ) {
					errorText.setText("ERROR: INVALID SEARCH VALUE");
					errorText.setVisible(true);
					resetSelect();
					return;
				}
				filteredAlbum.clear();
				searchByTag();
			}
			if( searchDrop.getSelectionModel().getSelectedItem().equals("Date")) {
				if( (dateSearch1.getValue() == null || dateSearch2.getValue() == null) 
						|| dateSearch1.getValue().isAfter(dateSearch2.getValue()) ) {
					errorText.setText("ERROR: INVALID DATE RANGE");
					errorText.setVisible(true);
					resetSelect();
					return;
				}
				filteredAlbum.clear();
				searchByDate();
			}
			update(filteredAlbum);
			if( filteredAlbum.isEmpty()) {
				errorText.setText("No results");
				errorText.setVisible(true);
			}
			resetB.setVisible(true);
			resultAlbumB.setVisible(true);
		}	
		if( b == resetB ) {
			update(album);
			resetSearch();
			tagSearch.clear();
			dateSearch1.getEditor().clear();
			dateSearch2.getEditor().clear();
			resetB.setVisible(false);
			resultAlbumB.setVisible(false);
			selectedIndex = 0;
		}
		if( b == backB ) {
			Master.toAlbum(thumbnailView);
		}
		if( b == logoutButton ) {
			Master.writeData();
			Master.toLogin(thumbnailView);
		}
		if( b == quitButton ) {
			Master.writeData();
			Platform.exit();
		}
		resetSelect();
	}
	
	private void makeResultAlbum() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Photos");
		dialog.setHeaderText("New album");
		dialog.setContentText("Enter new album name: ");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if( Master.currentUser.containsAlbum(result.get()) ){
				errorText.setText("Existing album name");
				errorText.setVisible(true);
			}else {
				Master.currentUser.albumMap.put(result.get(), filteredAlbum);
				errorText.setText("New album sucessfully created");
				errorText.setVisible(true);
			}
		}
		
	}
	
	private void editCaption( boolean filtered) {
		TextInputDialog dialog = new TextInputDialog(album.get(selectedIndex).caption);
		dialog.setTitle("Photos");
		dialog.setHeaderText("Edit Caption: ");
		dialog.setContentText("Caption: ");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			album.get(selectedIndex).caption = result.get();
			update(album);
			if(filtered) {
				System.out.println(filteredAlbum.size());
				filteredAlbum.get(filteredIndex).caption = result.get();
				update(filteredAlbum);
				System.out.println(filteredAlbum.size());
			}
		}
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
		System.out.println(date1);
		LocalDate date2 = dateSearch2.getValue();
		System.out.println(date2);
		for( Picture pic : album ) {
			System.out.println(pic.date);
			if( pic.date.equals(date1) || pic.date.equals(date2) ) {
				System.out.println("equals");
				filteredAlbum.add(pic);
			}
			if( pic.date.isAfter(date1) && pic.date.isBefore(date2) ) {
				System.out.println("between");
				filteredAlbum.add(pic);
			}
		}
	}
	
	private boolean addPicture() {
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
			return true;
		}
		return false;
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


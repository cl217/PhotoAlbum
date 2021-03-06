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
import model.AlbumObj;
import model.Picture;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */

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
    @FXML private Text toText;
    @FXML private ScrollPane scrollPane;

    
    /**
     * current album that is opened
     */
	ArrayList<Picture> album = Master.currentAlbum.album;
	
	/**
	 * search results 
	 */
	ArrayList<Picture> filteredAlbum = new ArrayList<Picture>();
	
	/**
	 * hashmap of tag category to list of pictures that have that tag category in this album
	 */
	HashMap<String, ArrayList<Picture>> tags = new HashMap<String, ArrayList<Picture>>();
	//tag type to get Arraylist of pics that have that tag type
	//cant have 2 tags to location type for a pic but can have 2 tags to a person type for a single pic
	//??????
	
	/**
	 * current selected index
	 */
	int selectedIndex;
	
	/**
	 * current selected index if search results are displayed
	 */
	int filteredIndex;
	
	/**
	 * grid of pictures
	 */
	ObservableList<Node> gridB = FXCollections.observableArrayList();
	
	/**
	 * starts ThumbnailView window
	 */
	public void start() {
		errorText.setVisible(false);
		userText.setText("user: " + Master.currentUser.name);
		albumText.setText(Master.currentAlbum.name);
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
	
	/**
	 * selects picture
	 * @param event mouseclick on not button 
	 */
	public void revert(MouseEvent event) {
		resetSelect();
	}

	private void updateSearch() {
		//System.out.println("updateSearch");
		tagSearch.setVisible(false);
		dateSearch1.setVisible(false);
		dateSearch2.setVisible(false);
		toText.setVisible(false);
		if (searchDrop.getSelectionModel().getSelectedItem().equals("Tags")) {
			tagSearch.setVisible(true);
			tagSearch.setEditable(true);
		}
		if( searchDrop.getSelectionModel().getSelectedItem().equals("Date")) {
			dateSearch1.setVisible(true);
			dateSearch2.setVisible(true);
			toText.setVisible(true);
		}
		resetSelect();
	}
	
    private void resetSearch() {
		tagSearch.setVisible(false);
		dateSearch1.setVisible(false);
		dateSearch2.setVisible(false);
		toText.setVisible(false);
    	searchDrop.setValue("Search by:");
		tagSearch.clear();
		dateSearch1.getEditor().clear();
		dateSearch2.getEditor().clear();
		resetB.setVisible(false);
		resultAlbumB.setVisible(false);
    }
    
    /**
     * updates pictures on display
     * @param displayAlbum full album or filteredalbum
     */
	private void update( ArrayList<Picture> displayAlbum ) {
		grid.getChildren().clear();
		grid.getColumnConstraints().clear();
		//grid.getRowConstraints().clear();
		int col = 0;
		int row = 0;
		int i = 0;
	    grid.getColumnConstraints().add(new ColumnConstraints(166)); 
	    grid.getRowConstraints().add(new RowConstraints(175)); 
	    
		for( Picture p : displayAlbum ) {
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
	
	
	/**
	 * get the index of the picture that is clicked
	 * @param event a picture is clicked
	 */
	public void picClick(ActionEvent event) {
		Button b = (Button) event.getSource();
		selectedIndex = Integer.parseInt(b.getId());
	}
	
	/**
	 * handles button presses
	 * @param event a button is pressed
	 * @throws IOException no stage
	 */
	public void buttonPress( ActionEvent event ) throws IOException {
		//System.out.println("buttonPress1");
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
			//System.out.println("openButton");
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
			LocalDate temp = album.get(selectedIndex).date;
			album.remove(selectedIndex);
			if( temp.equals(Master.currentAlbum.earliest) ) {
				Master.currentAlbum.resetEarliest();
			}
			if( temp.equals(Master.currentAlbum.latest) ) {
				Master.currentAlbum.resetLatest();
			}
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
				Master.currentUser.albumMap.get(toAlbum).album.add(album.get(selectedIndex));
			}
		}
		if( b == moveButton ) {
			String toAlbum = pickAlbum("Move to:");
			if( toAlbum != null ) {
				//Master.currentUser.albumMap.get(toAlbum).album.add(album.get(selectedIndex));
				for( String tagType : album.get(selectedIndex).tags.keySet() ) {
					tags.get(tagType).remove(album.get(selectedIndex));
				}
				album.remove(selectedIndex);
				System.out.println("regular album");
				update(album);
				if(resetB.isVisible()) {
					filteredAlbum.remove(filteredIndex);
					update(filteredAlbum);
				}
			}
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
	
	/**
	 * handles button presses
	 * @param e a button is pressed
	 * @throws IOException no stage
	 */
	public void buttonPress2(ActionEvent e) throws IOException {
		errorText.setVisible(false);
		//System.out.println("buttonPress2");
		Button b = (Button) e.getSource();
		if( b == addPictureButton ) {
			if(addPicture()) {
				selectedIndex = gridB.size()-1;
			}
		}
		if( b == resultAlbumB ) {
			makeResultAlbum();
		}
		if( b == searchButton ) {

			if(searchDrop.getSelectionModel().getSelectedIndex()==0) {
				errorText.setText("ERROR: NO SEARCH VALUE");
				errorText.setVisible(true);
				resetSelect();
				return;
			}
			
			if( searchDrop.getSelectionModel().getSelectedItem().equals("Tags")) {
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
			selectedIndex = 0;
		}	
		if( b == resetB ) {
			update(album);
			resetSearch();
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
	
	/**
	 * makes album from search results
	 */
	private void makeResultAlbum() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Photos");
		dialog.setHeaderText("New album");
		dialog.setContentText("Enter new album name: ");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			//System.out.println(result.get());
			if( Master.currentUser.containsAlbum(removeSpaces(result.get().toLowerCase())) ){
				errorText.setText("ERROR: ALBUM NAME ALREADY EXISTS");
				errorText.setVisible(true);
			}else {
				ArrayList<Picture> temp = new ArrayList<Picture>();
				temp.addAll(filteredAlbum);
				AlbumObj newAlbum = new AlbumObj( removeSpaces(result.get()),  temp );
				newAlbum.resetEarliest();
				newAlbum.resetLatest();
				Master.currentUser.albumMap.put( removeSpaces(result.get().toLowerCase()), newAlbum );
				errorText.setText("New album sucessfully created");
				errorText.setVisible(true);
			}
		}
		
	}
	
	/**
	 * edit caption
	 * @param filtered true if filtered album is currently on display
	 */
	private void editCaption( boolean filtered) {
		TextInputDialog dialog = new TextInputDialog(album.get(selectedIndex).caption);
		dialog.setTitle("Photos");
		dialog.setHeaderText("Edit Caption: ");
		dialog.setContentText("Caption: ");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String formatted = new String();
			if( result.get().isEmpty() ) {
				formatted = " ";
			}else{
				formatted = removeSpaces(result.get());
			}
			
			if(formatted.equals(" ")) {
				formatted = "";
			}
			album.get(selectedIndex).caption = formatted;
			update(album);
			if(filtered) {
				//System.out.println(filteredAlbum.size());
				filteredAlbum.get(filteredIndex).caption = result.get();
				update(filteredAlbum);
				//System.out.println(filteredAlbum.size());
			}
		}
	}
	
	/**
	 * search by tag
	 */
	private void searchByTag() {
		if( tagSearch.getText().isEmpty()) {
			errorText.setText("ERROR: NO TAGS ENTERED");
			errorText.setVisible(true);
			tagSearch.clear();
			return;
		}
		String input = tagSearch.getText().toLowerCase();
		//  |0: type1|1: value1|2: andor|3: type2|4: value2|
		String[] words = new String[5];
		
		//delete all extra spaces
		input = removeSpaces(input);
		try {
		if( input.contains(" or ") || input.contains(" and ") ) {
			//System.out.println("or/and");
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
				words[3] = input.substring(input.indexOf(" and ")+5, input.indexOf("=", input.indexOf("=")+1)); //type2
				words[4] = input.substring(input.indexOf("=", input.indexOf("=")+1)+1, input.length());
				for(int i = 0; i<5; i++) {
					//System.out.println(words[i]);
				}
				for( Picture pic: album ) {
					if( (pic.tags.containsKey(words[0]) && pic.tags.get(words[0]).contains(words[1]) ) 
							&& (pic.tags.containsKey(words[3]) && pic.tags.get(words[3]).contains(words[4])) ) {
						filteredAlbum.add(pic);
					}
				}
			}
		}else { //1 tag
			//System.out.println("one tag");
			words[0] = input.substring(0, input.indexOf("="));
			words[1] = input.substring(input.indexOf("=")+1, input.length());
			//System.out.println(words[0]);
			//System.out.println(words[1]);
			for( Picture pic: album ) {
				if( (pic.tags.containsKey(words[0]) && pic.tags.get(words[0]).contains(words[1]) ) ) {
					filteredAlbum.add(pic);
				}
			}
		}
		resetB.setVisible(true);
		resultAlbumB.setVisible(true);
		update(filteredAlbum);
		if( filteredAlbum.isEmpty()) {
			errorText.setText("No results");
			errorText.setVisible(true);
		}
		}catch(StringIndexOutOfBoundsException e) {
			//System.out.println("catched");
			errorText.setText("ERROR: INVALID TAG VALUE");
			errorText.setVisible(true);
		}
	}
	
	/**
	 * search by date range
	 */
	private void searchByDate() {
		LocalDate date1 = dateSearch1.getValue();
		//System.out.println(date1);
		LocalDate date2 = dateSearch2.getValue();
		//System.out.println(date2);
		for( Picture pic : album ) {
			//System.out.println(pic.date);
			if( pic.date.equals(date1) || pic.date.equals(date2) ) {
				//System.out.println("equals");
				filteredAlbum.add(pic);
			}
			if( pic.date.isAfter(date1) && pic.date.isBefore(date2) ) {
				//System.out.println("between");
				filteredAlbum.add(pic);
			}
		}
		resetB.setVisible(true);
		resultAlbumB.setVisible(true);
		update(filteredAlbum);
		if( filteredAlbum.isEmpty()) {
			errorText.setText("No results");
			errorText.setVisible(true);
		}
	}
	
	/**
	 * adds a picture
	 * @return true if picture is successfully added
	 */
	private boolean addPicture() {
		//System.out.println("addPicture");
		Stage stage = (Stage) thumbnailView.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File selectedFile = fileChooser.showOpenDialog( stage );
		
		if( selectedFile !=  null ) {
			//System.out.println(selectedFile.getPath());
			Picture pic = new Picture();
			pic.setURL(selectedFile.getPath());
			Date date = new Date(selectedFile.lastModified());
			LocalDate localD = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			pic.date = localD;
			//System.out.println(pic.date);
			//new popup for tags and captions
			if( album.isEmpty()) {
				Master.currentAlbum.earliest = pic.date;
				Master.currentAlbum.latest = pic.date;
			}else {
				if(pic.date.isAfter(Master.currentAlbum.latest)) {
					Master.currentAlbum.latest = pic.date;
				}
				if(pic.date.isBefore(Master.currentAlbum.earliest)) {
					Master.currentAlbum.earliest = pic.date;
				}
			}
			album.add(pic);
			update(album);
			resetSearch();
			return true;
		}
		return false;
	}
	
	/**
	 * pick album to copy/move picture to
	 * @param title popup window title
	 * @return name of the album selected
	 */
	private String pickAlbum(String title) {
		//System.out.println("Popup");
		ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>();
		choiceDialog.setTitle("Photos");
		choiceDialog.setHeaderText(title);
		for( String album : Master.currentUser.albumMap.keySet() ) {
			if( !album.equalsIgnoreCase(Master.currentAlbum.name) ){
			choiceDialog.getItems().add(album);
			}
		}
		choiceDialog.showAndWait();
		return choiceDialog.getSelectedItem();
	}
	
	/**
	 * formats input
	 * @param input unformatted string
	 * @return formatted string
	 */

	private String removeSpaces( String input ) {
		while(input.charAt(0)==' ' && input.length() != 1 ) {
			input = input.substring(1, input.length());
		}
		while(input.charAt(input.length()-1)==' ' && input.length() != 1 ) {
			input = input.substring(0, input.length()-1);
		}
		for( int i = 0; i < input.length()-1; i++ ) {
			if( input.charAt(i) == ' ' && input.length() != 1 && input.charAt(i+1) == ' ' ) {
				input = input.substring(0, i) + input.substring(i+2, input.length());
			}
		}
		return input;
	}	
	
}


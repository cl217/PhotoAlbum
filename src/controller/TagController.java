package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Picture;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */

public class TagController { 
    @FXML private AnchorPane tagView;
    @FXML private ChoiceBox<String> tagDropDown;
    @FXML private TextField tagField;
    @FXML private ListView<String> listView;
    @FXML private Button deleteB;
    @FXML private Button addCategoryB;
    @FXML private Button addB;
    @FXML private Text errorText;
    @FXML private Button doneB; //should this be a alert pop up like songLib?
    @FXML private Button editB;
    @FXML private Button deleteCatB;

    
    ObservableList<String> tagList = FXCollections.observableArrayList();
    
    /**
     * picture whose tags are being modified
     */
    Picture pic; 
    
    /**
     * starts tag window
     * @param p edit the tags of this picture
     */
    public void start(Picture p) {
    	this.pic = p;
    	updateListView();
    	updateTagCategory();
    	listView.getSelectionModel().select(0);
    }
    
    private void updateListView() {
    	//"type=value" for everything in pic.tags
    	//update tagDropDown for all values    	
    	
    	//System.out.println("TagControl.updateLV");
    	//System.out.println(pic.url);
		tagList.clear();
		for( String tagCategory : pic.tags.keySet() ) {
			//System.out.println(tagCategory);
			//System.out.println(tagCategory + ": " + pic.tags.get(tagCategory) );
			for(String tagValue: pic.tags.get(tagCategory)) {
				////System.out.println("Vincent: "+tagCategory + " , " + tagValue);
				tagList.add(tagCategory + "=" + tagValue);
				////System.out.println("list added: " + tagCategory + "=" + pic.tags.get(tagCategory));
			}
		}
		//System.out.println(tagList);
		listView.setItems(	tagList );
		//System.out.println("listView items set");
		return;
    
    }
    
    private void updateTagCategory() {
    	tagDropDown.getItems().clear();
    	for( Picture pic : Master.currentAlbum.album ) {
	    	for( String tagCategory : pic.tags.keySet() ) {
	    		if( tagDropDown.getItems().contains(tagCategory) == false ) {
	    			tagDropDown.getItems().add(tagCategory);
	    		}
	    	}
    	}
    }
    	
    /**
     * handles button presses
     * @param event a button is presed
     * @throws IOException no stage
     */
    public void buttonPress(ActionEvent event) throws IOException {
		errorText.setVisible(false);
    	Button b = (Button)event.getSource();
    	String categoryTag = "";
    	String category ="";
    	String tag = "";
    	int index = 0;
    	
    	if (b == addB) {
    		if( tagDropDown.getSelectionModel().getSelectedItem() == null ) {
    			errorText.setText("Error: a category must be selected");
    			errorText.setVisible(true);
    			return;	
    		}
    		category = tagDropDown.getSelectionModel().getSelectedItem();
    		if(tagField.getText().isEmpty()) {
    			errorText.setText("Error: a tag must be entered");
    			errorText.setVisible(true);
    			return;	
    		}
    		tag = removeSpaces(tagField.getText());
    		//System.out.println("CategoryTAG:" + tag);
    		if(tag.equals(" ")) {
    			errorText.setText("Error: a tag must be entered");
    			errorText.setVisible(true);
    			tagField.clear();
    			return;
    		}
    		if(pic.tags.get(category).contains(tag)) {
    			errorText.setText("Error: Duplicate value.");
    			errorText.setVisible(true);
    		}
    		else if ((pic.oneValueCat.contains(category) && pic.tags.get(category).size() == 1) ) {
    			errorText.setText("Error: Single value category already has a value.");
    			errorText.setVisible(true);
    		}
    		else {
    			pic.tags.get(category).add(tag);
    			updateListView();
    		}
    		listView.getSelectionModel().select(category+"="+tag);
    		tagField.clear();
    	}
    	else if (b == addCategoryB) {
			TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("Photos");
    		dialog.setHeaderText("Add Tag Category");
    		dialog.setContentText("Enter Category Name: ");
    		Optional<String> unformatted = dialog.showAndWait();
    		if(!unformatted.isPresent()) {
    			return;
    		}
    		String result = removeSpaces(unformatted.get());
    		if( result.equals(" ")) {
				errorText.setText("Error: Invalid Category Name");
				errorText.setVisible(true);
    		}
    		
    		if( pic.tags.keySet().contains(result) ) {
				errorText.setText("Error: Category already exists");
				errorText.setVisible(true);
				return;
    		}
    		
    		Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Photos");
        	alert.setHeaderText("Please select your category type");
        	alert.setContentText("Choose your option.");

        	ButtonType buttonTypeOne = new ButtonType("Single Value");
        	ButtonType buttonTypeTwo = new ButtonType("Multiple Value");
        	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        	Optional<ButtonType> result2 = alert.showAndWait();
        		
        	//if Single value is selected
        	for( Picture p : Master.currentAlbum.album) {
	        	if (result2.get() == buttonTypeOne){
	        		p.oneValueCat.add(result);
	        	}
	        	p.tags.put(result, new ArrayList<String>());
	        	tagDropDown.setValue(result);
        	}
        		
        	updateTagCategory();
        	tagDropDown.getSelectionModel().select(result);
    	}
    		
    	else if (b == deleteB) {
    		if (listView.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("Error: No tag selected");
    			errorText.setVisible(true);
    		}
    		else {
    			categoryTag = listView.getSelectionModel().getSelectedItem();
        		category = categoryTag.substring(0, categoryTag.indexOf("="));
        		tag = categoryTag.substring(categoryTag.indexOf("=")+1);
        		pic.tags.get(category).remove(tag);
        		updateListView();
        		
        		if(tagList.size()-1 < index) {
    				listView.getSelectionModel().select(tagList.size()-1);
    			}
    			else{
    				listView.getSelectionModel().select(index);
    			}
    		}
    	}
    	else if (b == editB) {
    		if (listView.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("Error: No tag selected");
    			errorText.setVisible(true);
    			return;
    		}
    		categoryTag = listView.getSelectionModel().getSelectedItem();
    		category = categoryTag.substring(0, categoryTag.indexOf("="));
    		tag = categoryTag.substring(categoryTag.indexOf("=")+1);
    		
			TextInputDialog dialog = new TextInputDialog(tag);
			dialog.setTitle("Photos");
			dialog.setHeaderText("Edit tag");
			dialog.setContentText("Enter new tag: ");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) { 
				String formatted = removeSpaces(result.get());
				if( formatted.equals(" ")) {
					errorText.setText("ERROR: NO TAG VALUE");
					errorText.setVisible(true);
					return;
				}
				if( !formatted.equals(tag) && pic.tags.get(category).contains(formatted)) {
					errorText.setText("ERROR: TAG ALREADY EXISTS");
					errorText.setVisible(true);
					return;
				}
				
				pic.tags.get(category).remove(tag);
				pic.tags.get(category).add(formatted);
			}
			updateListView();
			
			listView.getSelectionModel().select(index);
    	}
    	else if (b == deleteCatB) {
    		if (tagDropDown.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("Error: No category selected");
    			errorText.setVisible(true);
    			return;
    		}
    		Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Photos");
        	alert.setHeaderText("Are you sure you want to delete this Tag Category?");
        	alert.setContentText("This tag category and it's tag values will be deleted from every picture in the current album.");

        	ButtonType buttonTypeOne = new ButtonType("Confirm");
        	ButtonType buttonTypeTwo = new ButtonType("Cancel");
        	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        	Optional<ButtonType> result2 = alert.showAndWait();
        		
	        if (result2.get() == buttonTypeTwo){
	        	return;
	       	}
        	
    		category = tagDropDown.getSelectionModel().getSelectedItem();
    		for( Picture p : Master.currentAlbum.album ) {
    			if( p.oneValueCat.contains(category) ) {
    				p.oneValueCat.remove(category);
    			}
    			p.tags.remove(category);
   			}
        		
       		updateTagCategory();
       		updateListView();
   		    if(pic.tags.size()-1 < index) {
    			tagDropDown.getSelectionModel().select(pic.tags.size()-1);
    		}
    		else{
    			tagDropDown.getSelectionModel().select(index);
   			}
   		}
    	
    	if( b == doneB ) {
    		Master.toThumbnail(tagView);
    	}
    	
    }
    
    /**
     * formats input
     * @param input unformatted String
     * @return formatted String
     */
	private String removeSpaces( String input ) {
		input = input.toLowerCase();
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

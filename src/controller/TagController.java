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
    
    Picture pic; 
    
    /**
     * 
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
    	
    	System.out.println("TagControl.updateLV");
    	System.out.println(pic.url);
		tagList.clear();
		for( String tagCategory : pic.tags.keySet() ) {
			System.out.println(tagCategory);
			System.out.println(tagCategory + ": " + pic.tags.get(tagCategory) );
			for(String tagValue: pic.tags.get(tagCategory)) {
				//System.out.println("Vincent: "+tagCategory + " , " + tagValue);
				tagList.add(tagCategory + "=" + tagValue);
				//System.out.println("list added: " + tagCategory + "=" + pic.tags.get(tagCategory));
			}
		}
		System.out.println(tagList);
		listView.setItems(	tagList );
		System.out.println("listView items set");
		return;
    
    }
    
    private void updateTagCategory() {
    	tagDropDown.getItems().clear();
    	for( String tagCategory : pic.tags.keySet() ) {
    		tagDropDown.getItems().add(tagCategory);
    	}
    }
    	
    /**
     * 
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
    		category = tagDropDown.getSelectionModel().getSelectedItem();
    		tag = removeSpaces(tagField.getText());
    		
    		System.out.println("CategoryTAG:" + tag);
    		
    		if(pic.tags.get(category).contains(tag)) {
    			errorText.setText("Error: Duplicate value.");
    			errorText.setVisible(true);
    		}
    		else if ((pic.oneValueCat.contains(category) && pic.tags.get(category).size() == 1) ) {
    			errorText.setText("Error: Single Category already has a value.");
    			errorText.setVisible(true);
    		}
    		else {
    			pic.tags.get(category).add(tag);
    			updateListView();
    		}
    		listView.getSelectionModel().select(category+"="+tag);
    	}
    	else if (b == addCategoryB) {
			TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("List Tag Category");
    		dialog.setHeaderText("Add Tag Category");
    		dialog.setContentText("Enter name: ");
    		Optional<String> unformatted = dialog.showAndWait();
    		if(!unformatted.isPresent()) {
    			return;
    		}
    		String result = removeSpaces(unformatted.get());
    		
    		if( pic.tags.keySet().contains(result) ) {
				errorText.setText("Error: This category only allows one value");
				errorText.setVisible(true);
				return;
    		}
    		
    		Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Singular or Multiple Category type.");
        	alert.setHeaderText("Please select your category type");
        	alert.setContentText("Choose your option.");

        	ButtonType buttonTypeOne = new ButtonType("Single Value");
        	ButtonType buttonTypeTwo = new ButtonType("Multiple Value");
        	//ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        	Optional<ButtonType> result2 = alert.showAndWait();
        		
        	//if Single value is selected
        	if (result2.get() == buttonTypeOne){
        		category = result;
        		//System.out.println(category);
        		System.out.println(pic.url);
        		pic.oneValueCat.add(category);
        		System.out.println("ran");
        		pic.tags.put(category, new ArrayList<String>());
        		tagDropDown.setValue(category);
        			
        		//if multiple value is selected
        	} else {
        		category = result;
        		pic.tags.put(category, new ArrayList<String>());
        		tagDropDown.setValue(category);
        	}
        		
        	updateTagCategory();
        	tagDropDown.getSelectionModel().select(result);
    	}
    		
    	else if (b == deleteB) {
    		if (listView.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("Error: No items to delete");
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
    		categoryTag = listView.getSelectionModel().getSelectedItem();
    		category = categoryTag.substring(0, categoryTag.indexOf("="));
    		tag = categoryTag.substring(categoryTag.indexOf("=")+1);
    		
			int indexEdit = listView.getSelectionModel().getSelectedIndex();
			TextInputDialog dialog = new TextInputDialog(tag);
			dialog.setTitle("List Tag Edit");
			dialog.setHeaderText("Selected Item (Index: " + indexEdit + ")");
			dialog.setContentText("Enter new tag: ");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) { 
				String formatted = removeSpaces(result.get());
				if( !formatted.equals(tag) && pic.tags.get(category).contains(formatted)) {
					errorText.setText("ERROR: TAG ALREADY EXISTS");
					errorText.setVisible(true);
					return;
				}
				
				pic.tags.get(category).remove(tag);
				pic.tags.get(category).add(result.get());
			}
			updateListView();
			
			listView.getSelectionModel().select(index);
    	}
    	else if (b == deleteCatB) {
    		if (tagDropDown.getSelectionModel().getSelectedItem() == null) {
    			errorText.setText("Error: Category is empty.");
    			errorText.setVisible(true);
    		}
    		else{
    			category = tagDropDown.getSelectionModel().getSelectedItem();
        		pic.tags.remove(category);
        		
        		updateTagCategory();
        		updateListView();
    		
    			if(pic.tags.size()-1 < index) {
    				tagDropDown.getSelectionModel().select(pic.tags.size()-1);
    			}
    			else{
    				tagDropDown.getSelectionModel().select(index);
    			}
    		}
    	}
    	
    	if( b == doneB ) {
    		Master.toThumbnail(tagView);
    	}
    	
    }
    
    /**
     * 
     * @param input unformatted String
     * @return formatted String
     */
	private String removeSpaces( String input ) {
		input = input.toLowerCase();
		while(input.charAt(0)==' ') {
			input = input.substring(1, input.length());
		}
		while(input.charAt(input.length()-1)==' ') {
			input = input.substring(0, input.length()-1);
		}
		for( int i = 0; i < input.length(); i++ ) {
			if( input.charAt(i) == ' ' && input.charAt(i+1) == ' ' ) {
				input = input.substring(0, i) + input.substring(i+2, input.length());
			}
		}
		return input;
	}

}

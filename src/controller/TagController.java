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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Picture;

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
    public void start(Picture p) {
    	this.pic = p;
    	
    	/*
    	//manually add some tags for testing purposes
    	ArrayList<String> animal = new ArrayList<String>();
    	pic.tags.put("animal", animal);
    	pic.addTag("animal", "cat");
    	pic.addTag("animal", "notDog");
    	System.out.println("added to animals: " + pic.tags.get("animal").size());
    	ArrayList<String> color = new ArrayList<String>();
    	pic.tags.put("color", color);
    	pic.addTag("color", "white");
    	*/

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
    	
    
    public void buttonPress(ActionEvent event) throws IOException {
		errorText.setVisible(false);
    	Button b = (Button)event.getSource();
    	String categoryTag = "";
    	String category ="";
    	String tag = "";
    	boolean error = false;
    	int index = 0;
    	
    	if (b == addB) {
    		category = tagDropDown.getSelectionModel().getSelectedItem();
    		tag = tagField.getText();
    		
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
    		
    		for (String tagCategory : pic.tags.keySet()) {
    			for(String tagValue: pic.tags.get(tagCategory)) {
    				String s = tagCategory+"="+tagValue;
    				if( s.equals(category+"="+tag)) {
    					break;
    				}
    				index++;
    			}
    		}
    		
    		listView.getSelectionModel().select(index);
    	}
    	else if (b == addCategoryB) {
			TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("List Tag Category");
    		dialog.setHeaderText("Add Tag Category");
    		dialog.setContentText("Enter name: ");
    		Optional<String> result = dialog.showAndWait();
    		
    		for(String s : pic.tags.keySet()) {
    			if(result.isPresent() && isDuplicate(result.get(), pic.tags.get(s))){
    				errorText.setText("Error: Duplicate value.");
    				errorText.setVisible(true);
    				error = true;
    				break;
    			}
    		}
    		
    		if (result.isPresent() && !error) {
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
        			category = result.get();
        			pic.oneValueCat.add(category);
        			pic.tags.put(category, new ArrayList<String>());
        			tagDropDown.setValue(category);
        			
        		//if multiple value is selected
        		} else {
        			category = result.get();
        			pic.tags.put(category, new ArrayList<String>());
        			tagDropDown.setValue(category);
        		}
        		
        		updateTagCategory();
        		
        		for (String tagCategory : tagList) {
        			for(String tagValue: pic.tags.get(tagCategory)) {
        				String s = tagCategory+"="+tagValue;
        				if( s.equals(category+"="+tag)) {
        					break;
        				}
        				index++;
        			}
        		}
        		tagDropDown.getSelectionModel().select(index);
    		}
    		
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
    		
    		String item = listView.getSelectionModel().getSelectedItem();
			int indexEdit = listView.getSelectionModel().getSelectedIndex();
			TextInputDialog dialog = new TextInputDialog(tag);
			dialog.setTitle("List Tag Edit");
			dialog.setHeaderText("Selected Item (Index: " + indexEdit + ")");
			dialog.setContentText("Enter new tag: ");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) { 
				//this is only editing tag, not category.
				//if allowing edits on category, will it mess up our hashtable?
				//tagList.set(index, category + "=" + result.get()); 
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
    
    public boolean isDuplicate(String value, ArrayList<String> array) {
    	for (String s :array) {
    		if(value.equals(s)) {
    			return true;
    		}
    	}
    	return false;
    }

}

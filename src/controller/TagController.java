package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML private Text errorMsg;
    @FXML private Button doneB; //should this be a alert pop up like songLib?
    @FXML private Button cancelB; //should this be a alert pop up like songLib?
    @FXML private Button editB;

    
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
    }
    
    private void updateListView() {
    	//"type=value" for everything in pic.tags
    	//update tagDropDown for all values    	
    	
    	System.out.println("TagControl.updateLV");
    	System.out.println(pic.url);
		tagList.clear();
		System.out.println("1");
		for( String tagCategory : pic.tags.keySet() ) {
			System.out.println(tagCategory);
			System.out.println(tagCategory + ": " + pic.tags.get(tagCategory) );
			for(String tagValue: pic.tags.get(tagCategory)) {
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
    	Button b = (Button)event.getSource();
    	String categoryTag = "";
    	String category ="";
    	String tag = "";
    	
    	if (b == addB) {
    		category = tagDropDown.getSelectionModel().getSelectedItem();
    		tag = tagField.getSelectedText();
    		pic.tags.get(category).add(category + "=" + tag);
    	}
    	else if (b == addCategoryB) {
			TextInputDialog dialog = new TextInputDialog();
    		dialog.setTitle("List Tag Category");
    		dialog.setHeaderText("Add Tag Category");
    		dialog.setContentText("Enter name: ");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()) {
    			category = result.get();
    			pic.tags.put(category, new ArrayList<String>());
    			tagDropDown.setValue(category);
    		}
    		updateTagCategory();
    	}
    	else if (b == deleteB) {
    		categoryTag = listView.getSelectionModel().getSelectedItem();
    		category = categoryTag.substring(0, categoryTag.indexOf("="));
    		tag = categoryTag.substring(categoryTag.indexOf("=")+1);
    		pic.tags.get(category).remove(tag);
    		updateListView();
    	}
    	else if (b == editB) {
    		categoryTag = listView.getSelectionModel().getSelectedItem();
    		category = categoryTag.substring(0, categoryTag.indexOf("="));
    		tag = categoryTag.substring(categoryTag.indexOf("=")+1);
    		
    		String item = listView.getSelectionModel().getSelectedItem();
			int index = listView.getSelectionModel().getSelectedIndex();
			TextInputDialog dialog = new TextInputDialog(tag);
			dialog.setTitle("List Tag Edit");
			dialog.setHeaderText("Selected Item (Index: " + index + ")");
			dialog.setContentText("Enter new tag: ");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) { 
				//this is only editing tag, not category.
				//if allowing edits on category, will it mess up our hashtable?
				tagList.set(index, category + "=" + result.get()); 
			}
    	}
    	
    	if( b == doneB ) {
    		Master.toThumbnail(tagView);
    	}
    	
    }

}

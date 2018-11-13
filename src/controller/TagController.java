package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    @FXML private Button confirmB; //should this be a alert pop up like songLib?
    @FXML private Button cancelB; //should this be a alert pop up like songLib?
    @FXML private Button editB;
    
    Picture pic; 
    public void start(Picture p) {
    	this.pic = p;
    }
    
    private void updateListView() {
    	//"type=value" for everything in pic.tags
    
    }
    	
    
    private void buttonPress(ActionEvent event) {
    	Button b = (Button)event.getSource();
    	if (b == addB) {
    		pic.tags.get(tagDropDown.getSelectionModel().getSelectedItem()).add(tagField.getSelectedText());
    	}
    	else if (b == addCategoryB) {
    		tagDropDown.setValue(tagField.getSelectedText());
    	}
    	else if (b == deleteB) {
    		//we have access to the the tag, but need to find the key.
    		//once we have the key, we can go search the key and delete the tag inside the arraylist.
    	}
    }

}

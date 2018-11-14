package controller;

import java.util.ArrayList;
import java.util.Optional;

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
    			tagDropDown.setValue(category);
    		}
    	}
    	else if (b == deleteB) {
    		categoryTag = listView.getSelectionModel().getSelectedItem();
    		category = categoryTag.substring(0, categoryTag.indexOf("="));
    		tag = categoryTag.substring(categoryTag.indexOf("=")+1);
    		pic.tags.get(category).remove(tag);
    		//updateList();
    	}
    }

}

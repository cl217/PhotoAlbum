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
    @FXML private Button confirmB;
    @FXML private Button cancelB;
    @FXML private Button editB;
    
    Picture pic; 
    public void start(Picture p) {
    	this.pic = p;
    }
    
    private void updateListView() {
    	//"type=value" for everything in pic.tags
    
    }
    	
    
    private void buttonPress(ActionEvent event) {
    }

}

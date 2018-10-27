package model;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	String name;
	
	// HashMap< albumName, all pictures >, what to store data in?
	public ObservableList<String> albumList = FXCollections.observableArrayList();
	public HashMap<String, ObservableList<String>> album = new HashMap<String, ObservableList<String>>();
	
	public User( String name ) {
		this.name = name;
	}
	
	
}

package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User implements Serializable {
	public String name;
	
	// HashMap< albumName, all pictures >, what to store data in?
	//public ArrayList<String> albumList = new ArrayList<String>();
	
	//iterate over keys to print album list
	//get key to get list of pictures
	public HashMap<String, ArrayList<String>> albumMap = new HashMap<String, ArrayList<String>>();
	
	public User( String name ) {
		this.name = name;
	}
	
	public static final String directory = "data";
	
	public User read() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(directory + File.separator + name));
		User u = (User) ois.readObject();
		ois.close();
		return u;
	}
	

}

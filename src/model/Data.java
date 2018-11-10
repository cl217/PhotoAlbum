package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import controller.Master;

public class Data implements Serializable {
	public ArrayList<String> userList = new ArrayList<String>();
	public static final String directory = "data";
	public static final String file = "userData";
	
	public static Data readObjA() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(directory + File.separator + file));
		Data data = (Data) ois.readObject();
		ois.close();
		return data;
	}
	
}

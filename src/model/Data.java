package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * list of all users
	 */
	public ArrayList<String> userList = new ArrayList<String>();
	private static final String directory = "data";
	private static final String file = "userData";
	
	/**
	 * read data object
	 * @return a data object
	 * @throws IOException ObjectInputStream
	 * @throws ClassNotFoundException ObjectInputStream
	 */
	public static Data read() throws IOException, ClassNotFoundException {
		//System.out.println("ran5");
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(directory + File.separator + file));
		//System.out.println("ran6");
		Data data = (Data) ois.readObject();
		//System.out.println("Data7");
		ois.close();
		return data;
	}
	
}

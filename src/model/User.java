package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */
		 
public class User implements Serializable {
	
	private static final String directory = "data";
	
	private static final long serialVersionUID = 1L;

	public String name;
	
	// HashMap< albumName, all pictures >, what to store data in?
	//public ArrayList<String> albumList = new ArrayList<String>();
	
	//iterate over keys to print album list
	//get key to get list of pictures
	public HashMap<String, ArrayList<Picture>> albumMap = new HashMap<String, ArrayList<Picture>>();
	//have to make keys all lowercase
	
	/**
	 * 
	 * @param name name of user
	 */
	public User( String name ) {
		this.name = name;
	}

	/**
	 * 
	 * @param name album name
	 * @return if user already has an album with this name
	 */
	public boolean containsAlbum(String name) {
		for( String existingName : albumMap.keySet() )
			if( name.equalsIgnoreCase(existingName) ) {
				return true;
			}
		return false;
	}
	
	/**
	 * 
	 * @return User object
	 * @throws IOException ObjectInput Stream
	 * @throws ClassNotFoundException Object Input Stream
	 */
	public User read() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(directory + File.separator + name));
		User u = (User) ois.readObject();
		ois.close();
		return u;
	}
	
	public void loadStock(){
		System.out.println("loadStock1");
		ArrayList<Picture> pics = new ArrayList<Picture>();

		File dir = new File("..\\Photos85\\data\\stockPhotos");
		//File dir = new File("..\\Photos85\\stock");
		System.out.println(dir.listFiles());
		File[] directoryListing = dir.listFiles();
		System.out.println("loadStock2");
		
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("file found");
				System.out.println(child.getPath());
				Picture p = new Picture();
				p.setURL(child.getPath());
				p.caption = "Stock photo";
				Date date = new Date(child.lastModified());
				LocalDate localD = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				p.date = localD;
				pics.add(p);
			}
		}
		System.out.println("pics size" + pics.size() );
		albumMap.put("Stock", pics);
	}
	

}

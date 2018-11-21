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

	/**
	 * name of user
	 */
	public String name;
	
	/**
	 * hashmap of album name to album
	 */
	public HashMap<String, AlbumObj> albumMap = new HashMap<String, AlbumObj>();

	
	/**
	 * constructor
	 * @param name name of user
	 */
	public User( String name ) {
		this.name = name;
	}

	/**
	 * check if album name already exists
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
	 * read user data
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
	
	/**
	 * load stock user on first run
	 */
	public void loadStock(){
		//System.out.println("loadStock1");
		ArrayList<Picture> pics = new ArrayList<Picture>();
		AlbumObj album = new AlbumObj( "Stock", pics );

		File dir = new File("..\\Photos85\\data\\stockPhotos");
		File[] directoryListing = dir.listFiles();
		
		if (directoryListing != null) {
			for (File child : directoryListing) {
				Picture p = new Picture();
				p.setURL(child.getPath());
				p.caption = "Stock photo";
				Date date = new Date(child.lastModified());
				LocalDate localD = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				p.date = localD;
				if( album.album.isEmpty()) {
					album.earliest = p.date;
					album.latest = p.date;
				}else {
					if(p.date.isAfter(album.latest)) {
						album.latest = p.date;
					}
					if(p.date.isBefore(album.earliest)) {
						album.earliest = p.date;
					}
				}
				album.album.add(p);
			}
		}
		albumMap.put("stock", album);

	}
	

}

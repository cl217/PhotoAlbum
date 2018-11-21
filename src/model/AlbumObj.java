package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */
public class AlbumObj implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * arraylist of all this Pictures in this album
	 */
	public ArrayList<Picture> album = new ArrayList<Picture>();
	
	/**
	 * name of this album
	 */
	public String name;
	
	/**
	 * date of the earliest picture
	 */
	public LocalDate earliest;
	
	/**
	 * date of the lastest picture
	 */
	public LocalDate latest;
	
	/**
	 * 
	 * @param name name of new album
	 * @param album all the pictures
	 */
	public AlbumObj ( String name, ArrayList<Picture> album ) {
		this.name = name;
		this.album = album;
	}
	
	/**
	 * finds the date of the earliest picture
	 */
	public void resetEarliest() {
		if( album.isEmpty() ) {
			this.earliest = null;
			return;
		}
		LocalDate first = album.get(0).date;
		for( Picture p : album ) {
			if( p.date.isBefore(first) ) {
				first = p.date;
			}
		}
		this.earliest = first;
	}
	
	/**
	 * finds the date of the latest picture
	 */
	public void resetLatest() {
		if( album.isEmpty() ) {
			this.latest = null;
			return;
		}
		LocalDate last = album.get(0).date;
		for( Picture p : album ) {
			if( p.date.isAfter(last) ) {
				last = p.date;
			}
		}
		this.latest = last;
	}
}


package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Cindy Lin
 * @author Vincent Phan
 */

public class Picture implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * location of picture
	 */
	public String url;
	
	/**
	 * caption
	 */
	public String caption;
	
	/**
	 * key: tag category, value: arraylist of values to the category
	 */
	public HashMap<String, ArrayList<String>> tags = new HashMap<String, ArrayList<String>>();
	
	/**
	 * array of categories set to only have one value
	 */
	public ArrayList<String> oneValueCat = new ArrayList<String>();
 	//Type to values
	
	/**
	 * date of picture
	 */
	public LocalDate date;
	//Date
	
	/**
	 * adds tag to the tags hashmap
	 * @param type tag category
	 * @param value tag value
	 */
	public void addTag(String type, String value) {
		tags.get(type).add(value);
	}
	
	/**
	 * converts directory to url
	 * @param str URL
	 */
	public void setURL(String str) {
		this.url = "file:" + str;
	}
	
}

package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Picture implements Serializable{

	private static final long serialVersionUID = 1L;
	public String url;
	public String caption;
	public HashMap<String, ArrayList<String>> tags = new HashMap<String, ArrayList<String>>();
	//Type to values
	public LocalDate date;
	//Date
	
	public void addTag(String type, String value) {
		tags.get(type).add(value);
	}
	
	public void setURL(String str) {
		this.url = "file:" + str;
	}
	
}

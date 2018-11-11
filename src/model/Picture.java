package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Picture implements Serializable{

	private static final long serialVersionUID = 1L;
	public String url;
	public String caption;
	public ArrayList<String> tags = new ArrayList<String>();
	
	public void addTag(String str) {
		tags.add("#"+str);
	}
	public void setURL(String str) {
		this.url = "file:" + str;
	}
	
}

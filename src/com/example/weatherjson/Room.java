package com.example.weatherjson;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class Room {
	private long id;
	private String title;
	JSONArray json_post_tree = new JSONArray();	//all the posts and comments in this room
	ArrayList<String> members;// = new ArrayList<String>(length);
	
	public Room(long id, String title) {
	    this.id = id;
	    this.title = title;	    
	}
	
	public JSONArray get_post_tree() {
		return json_post_tree;
	}
	
	public void set_member_names(JSONArray names_array_json) {
		int length = names_array_json.length();
		members = new ArrayList<String>(length);
		
		for (int i = 0; i < length; i++) {
			try {
				members.add(names_array_json.getJSONObject(i).getString("screenname"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public ArrayList<String> get_member_names() {
		return members;
	}
	
	public void set_post_tree(JSONArray p_t) {
		
		for(int i = 0; i < p_t.length(); i++) {
			try {
				p_t.getJSONObject(i).getJSONObject("top_level_post").getString("created_at").replace("/[T]+/"," ");
				p_t.getJSONObject(i).getJSONObject("top_level_post").getString("created_at").replace("/[Z]+/"," ");
				
				JSONArray comments = p_t.getJSONObject(i).getJSONArray("comments");
				for(int c = 0; c < comments.length(); c++) {
					p_t.getJSONObject(i).getJSONArray("comments").getJSONObject(c).getJSONObject("comment").getString("created_at").replace("T"," ");
					p_t.getJSONObject(i).getJSONArray("comments").getJSONObject(c).getJSONObject("comment").getString("created_at").replace("T"," ");
				}				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		json_post_tree = p_t;
	}

	
	public long getId() {
	    return id;
	}
	public void setId(long id) {
	    this.id = id;
	}
	public String getTitle() {
	    return title;
	}
	public void setTitle(String title) {
	    this.title = title;
	}
}

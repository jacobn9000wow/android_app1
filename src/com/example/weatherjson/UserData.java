package com.example.weatherjson;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//singleton containing all data downloaded from the server
public class UserData {
	
	private static UserData unique_instance_user_data = new UserData();
	
	private Integer user_id_num;
	private JSONArray json_array_rooms;
	private JSONArray json_array_recent_posts;
	private String screen_name;
	private ArrayList<Room> rooms = new ArrayList<Room>();			//this list will not necessarily be in any particular order
	
    private UserData() {} //constructor
	
	public static UserData getInstance() {	//does this really need to be synchronized? I'm not using threads, but Android probably is
		//if(unique_instance_client == null) {
		//	unique_instance_client = new SingletonDefaultHttpClient();
		//}
		return unique_instance_user_data;
	}
	
	private int find_room_by_id(int id) {
		for(int i = 0; i < rooms.size(); i++) {   //crashes here
			if((int)rooms.get(i).getId() == id) {		
				return i;
			}
		}
		
		return -1;
	}
	
	public String get_room_name_by_id(int i) {
		int index = find_room_by_id(i);
		if (index == -1) return "Room not found";
		return rooms.get(index).getTitle();
	}
	
	public JSONObject get_post_by_id(int room_id, int post_id) {
		JSONArray post_tree = get_post_tree(room_id);
		for(int i = 0; i < post_tree.length(); i++) {
			int id;
			try {
				id = post_tree.getJSONObject(i).getJSONObject("top_level_post").getInt("id");
				if (id == post_id) {				
					return post_tree.getJSONObject(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		return null;
	}
	
	public JSONArray get_post_tree(int room_id) {
		
		int index = find_room_by_id(room_id);
		if(index == -1) {
			return null;
		}
		
		return rooms.get(index).get_post_tree();				
	}
	
	public void save_post_tree(int room_id, JSONArray p_t) {
		
		int index = find_room_by_id(room_id);
		if(index == -1) {						
			return;
		}
		
		(rooms.get(index)).set_post_tree(p_t);
	}
	
	
	public void set_id(Integer id)
	{
		user_id_num = id;
	}
	
	public Integer get_id()
	{
		return user_id_num;
	}
	
	public JSONArray get_rooms()
	{		
		return json_array_rooms;
	}
	
	public void set_rooms(JSONArray new_rooms_list)
	{
		json_array_rooms = new_rooms_list;
		
		for(int i = 0; i < json_array_rooms.length(); i++) {
			JSONObject entry;
			
			try {
				entry = (JSONObject) json_array_rooms.get(i);
				Room new_room = new Room(entry.getInt("id"),entry.getString("room_name"));
				rooms.add(new_room);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void set_member_names_for_room(JSONArray names, int r_id) {
		rooms.get(r_id).set_member_names(names);
	}
	
	public ArrayList<String> get_member_names_for_room(int r_id) {
		return rooms.get(r_id).get_member_names();
	}
	
	public void add_new_room(int room_id, String room_name) {
		
		Room new_room = new Room(room_id,room_name);
		rooms.add(new_room);
	}

	public void set_screenname(String screenname) {
		// TODO Auto-generated method stub
		screen_name = screenname;
	}
	
	public String get_screenname() {
		return screen_name;
	}

	public void set_recent_posts(JSONArray recent_posts) {
		// TODO Auto-generated method stub
		json_array_recent_posts = recent_posts;
	}

}

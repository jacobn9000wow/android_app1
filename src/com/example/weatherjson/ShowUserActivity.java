package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.weatherjson.RoomActivity.ReadRoomJSONFeedTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowUserActivity extends Activity {
	
	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ;
	private ArrayAdapter<String> listAdapterRoomName;
	private List<Integer> room_id;
	
	int room_to_load;
	
	private void load_room_list() {
		// Find the ListView resource.  
		ListView roomsListView = (ListView) findViewById (R.id.listView1);						 
			    
		// Set the ArrayAdapter as the ListView's adapter.  
		//roomsListView.setAdapter( listAdapter ); 
			    
		//load data from the UserData singleton
		UserData user_data = UserData.getInstance();
				
		JSONArray json_rooms = user_data.get_rooms();
		JSONObject json_room = new JSONObject();
				
		int length = json_rooms.length();
				
		//final ArrayList<Room> roomsArray = new ArrayList<Room>(length);
		final ArrayList<String> roomsNameArray = new ArrayList<String>(length);
		final ArrayList<Integer> roomsIdArray = new ArrayList<Integer>(length);
		final ArrayList<String> dummyArray = new ArrayList<String>(length);			//array of empty strings
				
		for (int i = 0; i < length; i++) {
			try {
				json_room = json_rooms.getJSONObject(i);
				//roomsArray.add(new Room(json_room.getLong("id"), json_room.getString("room_name")));
				roomsNameArray.add(json_room.getString("room_name"));
				roomsIdArray.add(json_room.getInt("id"));
				dummyArray.add("");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
				
		// Create ArrayAdapter using the room name list. 
		//http://developer.android.com/reference/android/R.layout.html
		listAdapterRoomName = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomsNameArray);  			    	    
		
		//Custom adapter
		RoomListAdapter postListAdapter = new RoomListAdapter(this, roomsNameArray, dummyArray, dummyArray);
						
		// Set the ArrayAdapter as the ListView's adapter.  
		//roomsListView.setAdapter( listAdapterRoomName ); 
		roomsListView.setAdapter( postListAdapter ); 
		
		//Display user's screenname
		TextView screennameTextView = (TextView) findViewById (R.id.textView1);
		screennameTextView.setText(user_data.get_screenname()); 
		
		
		roomsListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
			
			//get id of room
			Integer id_of_room = roomsIdArray.get((int) arg3);						
			room_to_load = id_of_room;
			
			//String TASKS_URL = "https://glacial-mountain-3555.herokuapp.com/rooms/" + room_to_load + ".json";	  	
			
			//ReadRoomJSONFeedTask get_room_data_task = new ReadRoomJSONFeedTask(ShowUserActivity.this);
			
			//Toast.makeText(this, room_to_load, Toast.LENGTH_LONG).show();		
			
			//get_room_data_task.setMessageLoading("Retrieving...");
			//get_room_data_task.execute(TASKS_URL);
			
			
			Intent intent = new Intent(getApplicationContext(), ShowRoomActivity.class);	      
            
			Bundle bundle = new Bundle();
			bundle.putInt("room_id", room_to_load);	          
	            
			intent.putExtras(bundle);
                
			startActivity(intent);
						
			//Intent intent = new Intent();
			//intent.setClass(ShowUserActivity.this, RoomActivity.class);
			            
			//Bundle bundle = new Bundle();
			//bundle.putInt("room_id", id_of_room);	          
			            
			//intent.putExtras(bundle);
			            
			//Toast.makeText(this, id_of_room.toString(), Toast.LENGTH_LONG).show();
			            
			//startActivity(intent);
			//finish();				
			}
			    	
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_user);
		
		//set the user's screen name
		UserData user_data = UserData.getInstance();
		TextView username_view = (TextView)this.findViewById(R.id.textView1);
		username_view.setText(user_data.get_screenname());
		
		load_room_list();
		
		//if (roomsListView != null) {
		//	roomsListView.setAdapter(new RoomAdapter(ShowUserActivity.this,
	    //          android.R.layout.simple_list_item_checked, roomsArray));
	    //    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_show_user);
		
		//reload the room list after adding a new room, or redeeming an invite
		load_room_list();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_user, menu);
		return true;
	}
	
	/*public void new_room(View view) {
		Intent intent = new Intent(getApplicationContext(), NewRoomActivity.class);	      
        startActivity(intent);
	}*/
			
	//http://ofps.oreilly.com/titles/9781118177679/network_programming.html
		public class ReadRoomJSONFeedTask extends AsyncTask <String, Void, String> {
				
			Context base_context;
			private String messageLoading;
			private ProgressDialog progressDialog = null;
				
			ReadRoomJSONFeedTask(Context c) {
				base_context = c;
			}
			
			public void setMessageLoading(String messageLoading) {
				this.messageLoading = messageLoading;
			}
			
			@Override 
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(
					this.base_context, 
					"Loading", 
					this.messageLoading, 
					true,
					true,
					new DialogInterface.OnCancelListener() {	
						@Override
						public void onCancel(DialogInterface arg0) {
							ReadRoomJSONFeedTask.this.cancel(true);
						}
					}
				);
			}

			@Override
			protected String doInBackground(String... urls) {
				//Toast.makeText(base_context, urls[0], Toast.LENGTH_LONG).show();
				return readJSONFeed(urls[0]);
			}
				
			protected void onPostExecute(String result) {
				
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				progressDialog = null;
				
				try {
			        	
			        //Toast.makeText(base_context, result, Toast.LENGTH_SHORT).show();
			        
			        JSONObject json_post_tree_object = new JSONObject(result);
			        
			        JSONArray json_post_tree = json_post_tree_object.getJSONArray("posts");
			        
			        JSONArray member_names = json_post_tree_object.getJSONArray("members");
			        
			        //Toast.makeText(base_context, json_post_tree.toString(), Toast.LENGTH_SHORT).show();
			        
					//JSONArray json_post_tree = new JSONArray(result);
			            
					//extract the post tree JSON object and save it to the UserData singleton
					UserData user_data = UserData.getInstance();
			            
					user_data.save_post_tree(room_to_load, json_post_tree);
					
					user_data.set_member_names_for_room(member_names, room_to_load);
			            
					Intent intent = new Intent(getApplicationContext(), ShowRoomActivity.class);	      
		                
					Bundle bundle = new Bundle();
					bundle.putInt("room_id", room_to_load);	          
			            
					intent.putExtras(bundle);
		                
					startActivity(intent);
					//finish();
			            
				} catch (Exception e) {
					//Toast.makeText(base_context, result, Toast.LENGTH_LONG).show();
			     Log.d("room activity exception", e.getLocalizedMessage());
			}
		}
				
				

		public String readJSONFeed(String URL) {
			StringBuilder stringBuilder = new StringBuilder();
			        
			//HttpClient httpClient = new DefaultHttpClient();
			SingletonDefaultHttpClient httpClient = SingletonDefaultHttpClient.getInstance();
			        
			HttpGet httpGet = new HttpGet(URL);
			try {
			     HttpResponse response = httpClient.execute(httpGet);
			     StatusLine statusLine = response.getStatusLine();
			     int statusCode = statusLine.getStatusCode();
			     if (statusCode == 200) {
			                HttpEntity entity = response.getEntity();
			                InputStream inputStream = entity.getContent();
			                BufferedReader reader = new BufferedReader(
			                        new InputStreamReader(inputStream));
			                String line;
			                while ((line = reader.readLine()) != null) {
			                    stringBuilder.append(line);
			                }
			                inputStream.close();
			            } else {
			                Log.d("readJSONFeed", "Failed to download file");
			            }
			        } catch (Exception e) {
			            Log.d("readJSONFeed", e.getLocalizedMessage());
			        }
			        return stringBuilder.toString();
			    }

			}
	
	private class RoomAdapter extends ArrayAdapter<Room> implements OnClickListener {

		  private ArrayList<Room> items;
		  private int layoutResourceId;

		  public RoomAdapter(Context context, int layoutResourceId, ArrayList<Room> items) {
		    super(context, layoutResourceId, items);
		    this.layoutResourceId = layoutResourceId;
		    this.items = items;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    View view = convertView;
		      if (view == null) {
		        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        view = (ListView) layoutInflater.inflate(layoutResourceId, null);
		      }
		      Room task = items.get(position);
		      if (task != null) {
		    	  ListView taskCheckedTextView = (ListView) view.findViewById(R.id.listView1);
		          if (taskCheckedTextView != null) {
		            //taskCheckedTextView.setText(task.getTitle());
		            //taskCheckedTextView.setChecked(task.getCompleted());
		            taskCheckedTextView.setOnClickListener(this);
		          }
		          view.setTag(task.getId());
		      }
		      return view;
		  }

		  @Override
		  public void onClick(View view) {
		    //CheckedTextView taskCheckedTextView = (CheckedTextView) view.findViewById(android.R.id.text1);
		    //if (taskCheckedTextView.isChecked()) {
		    //  taskCheckedTextView.setChecked(false);
		      //toggleTasksWithAPI(TOGGLE_TASKS_URL + view.getTag() + "/open.json");
		    //} else {
		     // taskCheckedTextView.setChecked(true);
		      //toggleTasksWithAPI(TOGGLE_TASKS_URL + view.getTag() + "/complete.json");
		    //}
		  }

		
		}


	

}

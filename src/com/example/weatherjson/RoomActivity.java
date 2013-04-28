package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.weatherjson.UserActivity.ReadUserJSONFeedTask;
import com.savagelook.android.UrlJsonAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;


//skipping this entire activity! -moved relevent parts into ShowUserActivity
public class RoomActivity extends Activity {

	int room_to_load;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		
		Intent intent = getIntent();
		room_to_load = intent.getIntExtra("room_id", -1);
		
		if(room_to_load == -1) {
			//something went wrong!
		}
		
		String TASKS_URL = "https://glacial-mountain-3555.herokuapp.com/rooms/" + room_to_load + ".json";	  	
		
		ReadRoomJSONFeedTask get_room_data_task = new ReadRoomJSONFeedTask(this);
		
		//Toast.makeText(this, room_to_load, Toast.LENGTH_LONG).show();		
		
		get_room_data_task.setMessageLoading("Retrieving...");
		get_room_data_task.execute(TASKS_URL);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room, menu);
		return true;
	}
	
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
				finish();
		            
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

}

package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

import com.savagelook.android.UrlJsonAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//loads the user data - list of rooms, recent posts, and screen name
//saves these data into UserData singleton
public class UserActivity extends Activity {
	
	//
	private String TASKS_URL;// = "https://glacial-mountain-3555.herokuapp.com/users/" + UserData. + ".json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		//what is our user id number?
		UserData user_data = UserData.getInstance();		
		TASKS_URL = "https://glacial-mountain-3555.herokuapp.com/users/" + user_data.get_id() + ".json";
	  	
		loadTasksFromAPI(TASKS_URL);
		
		//new ReadWeatherJSONFeedTask(this).execute("https://glacial-mountain-3555.herokuapp.com/users/.json");
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}
	
	private void loadTasksFromAPI(String url) {
	    //GetTasksTask getTasksTask = new GetTasksTask(UserActivity.this);
	    //getTasksTask.setMessageLoading("Loading tasks...");
	    //getTasksTask.execute(url);
		
		
	    new ReadUserJSONFeedTask(this).execute(url);
	}
	
	//http://ofps.oreilly.com/titles/9781118177679/network_programming.html
	public class ReadUserJSONFeedTask extends AsyncTask <String, Void, String> {
		
		Context base_context;
		
		ReadUserJSONFeedTask(Context c) {
			base_context = c;
		}

		@Override
		protected String doInBackground(String... urls) {
	        return readJSONFeed(urls[0]);
	    }
		
		protected void onPostExecute(String result) {
	        try {
	        	
	        	//Toast.makeText(base_context, result, Toast.LENGTH_LONG).show();
	        		        		        	
	            JSONObject jsonObject = new JSONObject(result);
	            
	            String screenname = jsonObject.getString("screen_name");
	            
	            JSONArray rooms = new JSONArray(jsonObject.getString("rooms"));
	            
	            JSONArray recent_posts = new JSONArray(jsonObject.getString("recent_posts"));
	            
	            //JSONObject weatherObservationItems = new
	                //JSONObject(jsonObject.getString("weatherObservation"));

	            //Toast.makeText(base_context,
	            //        weatherObservationItems.getString("clouds") +
	            //        " - " + weatherObservationItems.getString("stationName"),
	            //        Toast.LENGTH_SHORT).show();
	            
	            //save this data to the UserData singleton
	        	UserData user_data = UserData.getInstance();
	        	
	        	user_data.set_rooms(rooms);
	        	
	        	user_data.set_screenname(screenname);
	        	
	        	user_data.set_recent_posts(recent_posts);
	        	
	        	// launch the ShowUserActivity and close this one
                //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                Intent intent = new Intent(getApplicationContext(), ShowUserActivity.class);	      
                startActivity(intent);
                finish();
	            
	        } catch (Exception e) {
	            Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
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

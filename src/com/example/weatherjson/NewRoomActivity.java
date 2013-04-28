package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import com.savagelook.android.UrlJsonAsyncTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewRoomActivity extends Activity {
	
	String mRoomName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_room);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_room, menu);
		return true;
	}
	
	public void create_room(View button) {
		
		EditText userLoginNameField = (EditText) findViewById(R.id.editText1);
		mRoomName = userLoginNameField.getText().toString();
		
		if (mRoomName.length() == 0) {
	        // input fields are empty
	        Toast.makeText(this, "Please complete all the fields",
	            Toast.LENGTH_LONG).show();
	        return;
	    } else {
	    	String LOGIN_API_ENDPOINT_URL = "https://glacial-mountain-3555.herokuapp.com/newroommobile.json";	    	
	    	NewRoomTask roomTask = new NewRoomTask(NewRoomActivity.this);
	    	roomTask.setMessageLoading("Creating room...");
	    	roomTask.execute(LOGIN_API_ENDPOINT_URL);
	    }
		
	}
	
private class NewRoomTask extends UrlJsonAsyncTask {
		
		Context base_context;
		String r;
		
	    public NewRoomTask(Context context) {
	        super(context);
	        base_context = context;
	    }

	    @Override
	    protected JSONObject doInBackground(String... urls) {
	        //DefaultHttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(urls[0]);
	        JSONObject holder = new JSONObject();
	        JSONObject roomObj = new JSONObject();
	        HttpResponse response = null;
	        JSONObject json = new JSONObject();
	        
	        //http://stackoverflow.com/questions/3587254/how-do-i-manage-cookies-with-httpclient-in-android-and-or-java
	        // Create a local instance of cookie store	      
	        //BasicCookieStore cookieStore = new BasicCookieStore();
	        	       
	        // Create local HTTP context
	        //HttpContext localContext = new BasicHttpContext();
	        
	        // Bind custom cookie store to the local context
	        //localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	        
	        //pass localContext in to client.execute()   - below:

	        try {
	            try {
	                // setup the returned values in case
	                // something goes wrong
	                json.put("success", false);
	                json.put("info", "Something went wrong. Retry!");
	                // add the user email and password to
	                // the params
	                roomObj.put("room_name", mRoomName);
	                holder.put("room", roomObj);
	                StringEntity se = new StringEntity(holder.toString());
	                post.setEntity(se);	                
	               
	                // setup the request headers
	                post.setHeader("Accept", "application/json");
	                post.setHeader("Content-Type", "application/json");

	                ResponseHandler<String> responseHandler = new BasicResponseHandler();
	                
	                //the client must remember cookies
	                SingletonDefaultHttpClient client = SingletonDefaultHttpClient.getInstance();	                	                
	                	                
	                response = client.execute(post);//, responseHandler);	                
	                
	                HttpEntity entity = response.getEntity();
	                InputStream is = entity.getContent();
	                
	                String response_string = convertStreamToString(is);
	                
	                r = response_string;
	                
	                json = new JSONObject(response_string);
	                
	                
	                
	                //Toast.makeText(base_context, response_string, Toast.LENGTH_LONG).show();
	                
	            } catch (HttpResponseException e) {
	                e.printStackTrace();
	                Log.e("ClientProtocol", "" + e);
	                //Log.e(response, response);
	                json.put("info", "Email and/or password are invalid. Retry!!!");
	                //Toast.makeText(base_context, response, Toast.LENGTH_LONG).show();
	            } catch (IOException e) {
	                e.printStackTrace();
	                Log.e("IO", "" + e);
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	            Log.e("JSON", "" + e);
	        }

	        return json;
	    }
	    
	    private String convertStreamToString(InputStream is) {

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append((line + "\n"));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }

	    @Override
	    protected void onPostExecute(JSONObject json) {
	    	//Toast.makeText(base_context, r, Toast.LENGTH_LONG).show();
	        try {
	            if (json.getBoolean("success")) {
	                
	            	int room_id = json.getInt("room_id");
	            	
	            	//save this data to the UserData singleton
		        	UserData user_data = UserData.getInstance();
	            	
		        	user_data.add_new_room(room_id, mRoomName);
		        	
		        	//load the new room
		        	Intent intent = new Intent();
		            intent.setClass(NewRoomActivity.this, RoomActivity.class);		            
		            Bundle bundle = new Bundle();
		            bundle.putInt("room_id", room_id);	          		            
		            intent.putExtras(bundle);            
		            startActivity(intent);
		        	
	                finish();
	            }
	            //Toast.makeText(base_context, r, Toast.LENGTH_LONG).show();
	            //Toast.makeText(context, json.getString("info"), Toast.LENGTH_LONG).show();
	        } catch (Exception e) {
	            // something went wrong: show a Toast
	            // with the exception message
	        	//Toast.makeText(base_context, r, Toast.LENGTH_LONG).show();
                
	            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
	        } finally {
	            super.onPostExecute(json);
	        }
	    }
	}

}

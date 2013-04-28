package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends Activity {
	
	int room_to_load;
	int post_to_load;
	String post_content;
	String post_author;
	String post_reply_content;
	ArrayList<String> commentsContentArray;// = new ArrayList<String>(length);
	ArrayList<String> commentsAuthorsArray;
	ArrayList<String> commentsTimesArray;
	ArrayList<String> dummyArray;			//array of empty strings
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		//Find the room and post to show
		Intent intent = getIntent();
		room_to_load = intent.getIntExtra("room_id", -1);
		post_to_load = intent.getIntExtra("post_id", -1);
		
		if(room_to_load == -1 || post_to_load == -1) {
			//something went wrong!
		}
		
		UserData user_data = UserData.getInstance();
		try {
			
			JSONObject post = user_data.get_post_by_id(room_to_load, post_to_load);
			
			post_content = post.getJSONObject("top_level_post").getString("content");
			post_author = post.getString("author");
			
			//comments
			int length = post.getJSONArray("comments").length();
			commentsContentArray = new ArrayList<String>(length);
			commentsAuthorsArray = new ArrayList<String>(length);
			commentsTimesArray = new ArrayList<String>(length);
			dummyArray = new ArrayList<String>(length);
			
			for (int i = 0; i < length; i++){
				commentsContentArray.add(post.getJSONArray("comments").getJSONObject(i).getJSONObject("comment").getString("content"));
				commentsTimesArray.add(post.getJSONArray("comments").getJSONObject(i).getJSONObject("comment").getString("created_at"));
				commentsAuthorsArray.add(post.getJSONArray("comments").getJSONObject(i).getString("author"));
				dummyArray.add("");
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//comments
		
		//Find the ListView resources.  
		TextView postTextView = (TextView) findViewById (R.id.textView1);
		TextView authorTextView = (TextView) findViewById (R.id.textView3);
		TextView roomTextView = (TextView) findViewById (R.id.textView2);
		ListView commentsListView = (ListView) findViewById (R.id.listView1);
		
		//set the post text
		postTextView.setText(post_content);
		authorTextView.setText(post_author);
		roomTextView.setText(user_data.get_room_name_by_id(room_to_load));
		
		//Custom adapter - use the PostListAdapter for our comments
		PostListAdapter postListAdapter = new PostListAdapter(this, commentsAuthorsArray, commentsContentArray, dummyArray, commentsTimesArray);

		commentsListView.setAdapter(postListAdapter);		
		
	}
	
	public void post_reply(View button) {
		
		EditText postField = (EditText) findViewById(R.id.editText1);
	    post_reply_content = postField.getText().toString();
	    String POST_API_ENDPOINT_URL = "https://glacial-mountain-3555.herokuapp.com/newpostreplymobile.json";
	    
	    if (post_reply_content.length() == 0) {
	        // input fields are empty
	        Toast.makeText(this, "Post cannot be empty",
	            Toast.LENGTH_LONG).show();
	        return;
	    } else {
	    	
	    	PostReplyTask postTask = new PostReplyTask(PostActivity.this);
	    	postTask.setMessageLoading("Posting...");
	    	postTask.execute(POST_API_ENDPOINT_URL);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}
	
	private class PostReplyTask extends UrlJsonAsyncTask {
		
		Context base_context;
		String r;
	
		public PostReplyTask(Context context) {
			super(context);
			base_context = context;
			// TODO Auto-generated constructor stub
		}
		
		@Override
	    protected JSONObject doInBackground(String... urls) {
	        //DefaultHttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(urls[0]);
	        JSONObject holder = new JSONObject();
	        JSONObject postObj = new JSONObject();
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
	                UserData user_data = UserData.getInstance();
	                int user_id = user_data.get_id();
	                
	                postObj.put("user_id", user_id);
	                postObj.put("content", post_reply_content);
	                //postObj.put("room_id", room_to_load);
	                postObj.put("post_id", post_to_load);
	                
	                
	                
	                //postObj.put("title", "");
	                postObj.put("url", "");
	                holder.put("comments", postObj);
	                //holder.put("target_room_id", room_to_load);
	                holder.put("target_post_id", post_to_load);	//this is the only difference from sending top-level posts
	               
	                
	                StringEntity se = new StringEntity(holder.toString());
	                post.setEntity(se);	               
	               
	                // setup the request headers
	                post.setHeader("Accept", "application/json");
	                post.setHeader("Content-Type", "application/json");

	                //ResponseHandler<String> responseHandler = new BasicResponseHandler();
	                
	                //the client must remember cookies
	                SingletonDefaultHttpClient client = SingletonDefaultHttpClient.getInstance();	                	                
	                
	                response = client.execute(post);//, responseHandler);	                
	                
	                HttpEntity entity = response.getEntity();
	                InputStream is = entity.getContent();
	                
	                String response_string = convertStreamToString(is);
	                
	                r = holder.toString();
	                
	                json = new JSONObject(response_string);//throws ex because response is 404
	                
	                
	                
	                //Toast.makeText(base_context, response_string, Toast.LENGTH_LONG).show();
	                
	            } catch (HttpResponseException e) {
	                e.printStackTrace();
	                Log.e("ClientProtocol", "" + e);
	                //Log.e(response, response);
	                json.put("info", "Email and/or password are invalid. Retry!!!");
	                //Toast.makeText(base_context, "here ", Toast.LENGTH_LONG).show();
	            } catch (IOException e) {
	                e.printStackTrace();
	                Log.e("IO", "" + e);
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	            Log.e("JSON", "" + e);
	            //Toast.makeText(base_context, debug, Toast.LENGTH_LONG).show();
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
	                	            	
	            	// everything is ok
	                
		        	
		        	Toast.makeText(base_context, "Post created", Toast.LENGTH_LONG).show();
		        	
		        	
	            }
	            else Toast.makeText(base_context, r, Toast.LENGTH_LONG).show();
	        	
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

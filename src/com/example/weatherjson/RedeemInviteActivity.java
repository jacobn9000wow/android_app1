package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class RedeemInviteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redeem_invite);
	}
	
	public void submit (View button) {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.redeem_invite, menu);
		return true;
	}

/*private class RedeemTask extends UrlJsonAsyncTask {
		
		Context base_context;
		String r;
	
		public RedeemTask(Context context) {
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
	
		
	}*/

	
}

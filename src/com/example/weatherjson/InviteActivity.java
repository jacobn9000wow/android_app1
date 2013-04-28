package com.example.weatherjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

import com.example.weatherjson.RoomActivity.ReadRoomJSONFeedTask;
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
import android.widget.EditText;
import android.widget.Toast;

public class InviteActivity extends Activity {

	int room_id;
	int user_id;
	EditText token_field; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		
		token_field = (EditText) findViewById(R.id.editText1);
		
		//get user id
		UserData user_data = UserData.getInstance();
		user_id = user_data.get_id();
		
		//get room id
		Intent intent = getIntent();
		room_id = intent.getIntExtra("room_id", -1);
				
		if(room_id == -1) {
			//something went wrong!
		}
		
		String LOGIN_API_ENDPOINT_URL = "https://glacial-mountain-3555.herokuapp.com/newinvitemobile.json";
		
		NewInviteTask inviteTask = new NewInviteTask(InviteActivity.this);
		inviteTask.setMessageLoading("Getting token...");
        inviteTask.execute(LOGIN_API_ENDPOINT_URL);
		
		//String GET_REQUEST_LOGIN_API_ENDPOINT_URL = "https://glacial-mountain-3555.herokuapp.com/invitations/new." + room_id + ".json";
		
		//ReadInviteJSONFeedTask get_inv_data_task = new ReadInviteJSONFeedTask(this);
		
		//Toast.makeText(this, room_to_load, Toast.LENGTH_LONG).show();		
		
		//get_inv_data_task.setMessageLoading("Getting token...");
		//get_inv_data_task.execute(GET_REQUEST_LOGIN_API_ENDPOINT_URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite, menu);
		return true;
	}
	
	//http://ofps.oreilly.com/titles/9781118177679/network_programming.html
		public class ReadInviteJSONFeedTask extends AsyncTask <String, Void, String> {
			 
			String r;
			Context base_context;
			private String messageLoading;
			private ProgressDialog progressDialog = null;
				
			ReadInviteJSONFeedTask(Context c) {
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
							ReadInviteJSONFeedTask.this.cancel(true);
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
					
					Toast.makeText(base_context, r, Toast.LENGTH_LONG).show();
					
					JSONObject json = new JSONObject(result);								
					
					String token = json.getJSONObject("invitation").getString("token");
	            	
	            	token_field.setText(token);
	            		            			        				      
			            
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
			                r = response.toString();
			            }
			        } catch (Exception e) {
			            Log.d("readJSONFeed", e.getLocalizedMessage());
			        }
			        return stringBuilder.toString();
			    }

			}

private class NewInviteTask extends UrlJsonAsyncTask {
		
		Context base_context;
		String r;
		
	    public NewInviteTask(Context context) {
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
	                
	                
	                
	                roomObj.put("room_id", room_id);
	                roomObj.put("sender_id", user_id);
	                roomObj.put("sent_at", "");
	                roomObj.put("token", "");
	                holder.put("invitation", roomObj);
	                StringEntity se = new StringEntity(holder.toString());
	                post.setEntity(se);	
	                
	                //r=se.toString();
	               
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
	                
	                //r = response_string;
	                
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
	    	Toast.makeText(base_context, "room_id " + room_id + " user_id "+ user_id , Toast.LENGTH_LONG).show();
        	
	        try {
	            if (json.getBoolean("success")) {
	                
	            	
	            	//put the token in the text field
	            	
	            	String token = json.getJSONObject("invitation").getString("token");
	            	
	            	token_field.setText(token);
	            	
	            	Toast.makeText(base_context, "Invitation generated!" , Toast.LENGTH_LONG).show();
		        	
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

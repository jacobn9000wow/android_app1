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

import com.example.weatherjson.ShowUserActivity.ReadRoomJSONFeedTask;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class ShowRoomActivity extends Activity {
	
	int room_to_load;
	//private ExpandableListAdapter listAdapterPostContent;
	private ArrayAdapter<String> listAdapterPostContent;
	
	String post_content;
	
	//will be called by Async Task after downloading the room data
	protected void setup_views() {
		//Find the ListView resource.  
		ListView postsListView = (ListView) findViewById (R.id.listView1);
				
		//Find the post tree
		UserData user_data = UserData.getInstance();		
		JSONArray json_post_tree = user_data.get_post_tree(room_to_load);
		JSONObject json_post = new JSONObject(); //top level post + comments
		JSONObject top_level_post = new JSONObject();
		JSONArray second_level_comments = new JSONArray();
		JSONObject single_comment = new JSONObject();
						
		int length = json_post_tree.length();	//zero?
						
		final ArrayList<String> postsContentArray = new ArrayList<String>(length);
		final ArrayList<ArrayList<String>> commentsContentArray = new ArrayList<ArrayList<String>>(length);
		final ArrayList<String> postsTimesArray = new ArrayList<String>(length);
		final ArrayList<String> postsAuthorsArray = new ArrayList<String>(length);
		final ArrayList<String> postsNumberOfRepliesArray = new ArrayList<String>(length);
		//final ArrayList<Integer> postsIdArray = new ArrayList<Integer>(length);
				
		final ArrayList<String> postsTotalArray = new ArrayList<String>(length);
				
		for (int i = 0; i < length; i++) {
			try {
				json_post = json_post_tree.getJSONObject(i);
								
				top_level_post = json_post.getJSONObject("top_level_post");
				String author = json_post.getString("author");
				second_level_comments = json_post.getJSONArray("comments");
						
				String top_level_post_content = top_level_post.getString("content");
						
				//id of post
				//postsIdArray.add()
						
				//postsContentArray.add(json_post.getString("content"));
				postsContentArray.add(top_level_post_content);
						
				//number of replies
				String num_replies = second_level_comments.length() + " replies";
				postsNumberOfRepliesArray.add(num_replies);
						
				//author name
				postsAuthorsArray.add(author);
						
				//time
				String created_at = top_level_post.getString("created_at");
				postsTimesArray.add(created_at);
						
				String total = author + "\n" + created_at + "\n\n" + top_level_post_content + "\n" + num_replies;
				postsTotalArray.add(total);
						
				int number_of_comments = second_level_comments.length();
								
				//comments for this post
			//	for (int j = 0; j < number_of_comments; j++) {
			//		single_comment = second_level_comments.getJSONObject(j);
			//		commentsContentArray.get(i).add(single_comment.getString("content"));
			//	}
								
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
						
		//Custom adapter
		PostListAdapter postListAdapter = new PostListAdapter(this, postsAuthorsArray, postsContentArray, postsNumberOfRepliesArray, postsTimesArray);
						
		// Create ArrayAdapter using the room name list.  
		listAdapterPostContent = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, postsTotalArray);  
								
		//postsListView.setAdapter(listAdapterPostContent);
		postsListView.setAdapter(postListAdapter);
				
		postsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				//Find the post tree
				UserData user_data = UserData.getInstance();		
				JSONArray json_post_tree = user_data.get_post_tree(room_to_load);				
				JSONObject json_post = new JSONObject();
				Integer id_of_post = null;
				try {
					json_post = json_post_tree.getJSONObject((int) arg3).getJSONObject("top_level_post");
					id_of_post = json_post.getInt("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
						
				Intent intent = new Intent();
			    intent.setClass(ShowRoomActivity.this, PostActivity.class);
			            
			    Bundle bundle = new Bundle();
			    bundle.putInt("post_id", id_of_post);	 
			    bundle.putInt("room_id", room_to_load);
			            
			    intent.putExtras(bundle);
			            
			    //Toast.makeText(this, id_of_post, Toast.LENGTH_LONG).show();
			            
			    startActivity(intent);
			            
			    //finish();	
			}
					
		});
				
		//set the room's name			
		TextView roomname_view = (TextView)this.findViewById(R.id.textView1);
		roomname_view.setText(user_data.get_room_name_by_id(room_to_load));
				
				
			
	}
	
	protected void download_posts(String message) {
		String TASKS_URL = "https://glacial-mountain-3555.herokuapp.com/rooms/" + room_to_load + ".json";	  	
		
		ReadRoomJSONFeedTask get_room_data_task = new ReadRoomJSONFeedTask(ShowRoomActivity.this);	
		
		get_room_data_task.setMessageLoading(message);
		get_room_data_task.execute(TASKS_URL);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_room);
		
		//Find the room to show
		Intent intent = getIntent();
		room_to_load = intent.getIntExtra("room_id", -1);
		
		if(room_to_load == -1) {
			//something went wrong!
		}
		
		//String TASKS_URL = "https://glacial-mountain-3555.herokuapp.com/rooms/" + room_to_load + ".json";	  	
		
		//ReadRoomJSONFeedTask get_room_data_task = new ReadRoomJSONFeedTask(ShowRoomActivity.this);
		
		//Toast.makeText(this, room_to_load, Toast.LENGTH_LONG).show();		
		
		//get_room_data_task.setMessageLoading("Retrieving...");
		//get_room_data_task.execute(TASKS_URL);
		
		//setup_views();				
				
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		download_posts("Retrieving...");
	}

	//button - "Generate invitation"
	/*public void invite (View button) {
		
		Intent intent = new Intent();
        intent.setClass(ShowRoomActivity.this, InviteActivity.class);
        
        Bundle bundle = new Bundle();
        bundle.putInt("room_id", room_to_load);	          
        
        intent.putExtras(bundle);

        startActivity(intent);
	}*/
	
	//button1 - "Post"
	public void post(View button) {
		EditText postField = (EditText) findViewById(R.id.editText1);
	    post_content = postField.getText().toString();
	    String POST_API_ENDPOINT_URL = "https://glacial-mountain-3555.herokuapp.com/newpostmobile.json";
	    
	    if (post_content.length() == 0) {
	        // input fields are empty
	        Toast.makeText(this, "Post cannot be empty",
	            Toast.LENGTH_LONG).show();
	        return;
	    } else {
	    	
	    	PostTask postTask = new PostTask(ShowRoomActivity.this);
	    	postTask.setMessageLoading("Posting...");
	    	postTask.execute(POST_API_ENDPOINT_URL);
	    }
	    
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_room, menu);
		return true;
	}
	
	private class PostTask extends UrlJsonAsyncTask {
		
		Context base_context;
		String r;
	
		public PostTask(Context context) {
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
	                postObj.put("content", post_content);
	                postObj.put("room_id", room_to_load);
	                postObj.put("title", "");
	                postObj.put("url", "");
	                holder.put("post", postObj);
	                holder.put("target_room_id", room_to_load);
	                
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
	                
	                
	                json = new JSONObject(response_string);
	                
	                r = holder.toString();
	                
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
	                	            	
	            	// everything is ok
	                
		        	
		        	//Toast.makeText(base_context, "Post created", Toast.LENGTH_LONG).show();
		        	download_posts("Updating...");
		        	
		        	
	            }
	            else Toast.makeText(base_context, json.getString("info"), Toast.LENGTH_LONG).show();
	        	
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
				            
						setup_views();
						
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

}

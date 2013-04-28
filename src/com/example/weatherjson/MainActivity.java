package com.example.weatherjson;

import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	
	//ParcelableDefaultHttpClient http_client = new ParcelableDefaultHttpClient(); //using singleton instead

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //int txtLat = 100;
		//String txtLong ="100";
        
		//new ReadWeatherJSONFeedTask(this).execute("http://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo");
		
        //found a way to view all current invitations without being logged in!
        //new ReadWeatherJSONFeedTask(this).execute("https://glacial-mountain-3555.herokuapp.com/invitations.json");
	    
        //new ReadWeatherJSONFeedTask(this).execute("https://glacial-mountain-3555.herokuapp.com/sessions/create?name=arnold&password=nevermore.json");
	    
        //new ReadWeatherJSONFeedTask(this).execute("https://glacial-mountain-3555.herokuapp.com/sessions/create.json");
	    
        
        //try logging in -- sessions/create - POST instead of GET
        //String q = { name => 'arnold', password => 'nevermore' }.to_query('session');
        //{:a=>53,:b=>{:c=>7}}.to_query;
        //new POSTjson(this).execute("https://glacial-mountain-3555.herokuapp.com/sessions/create?session%5Bname%5D=arnold&session%5Bpassword%5D=nevermore.json");
	    
        //http://stackoverflow.com/questions/798710/how-to-turn-a-ruby-hash-into-http-params
        //new POSTjson(this).execute("https://glacial-mountain-3555.herokuapp.com/sessions/create?session[name]=arnold&session[password]=nevermore.json");
	    
        //new POSTjson(this).execute("https://glacial-mountain-3555.herokuapp.com/sessions.json");
	    
        //Existing Account, load login view
        Intent intent = new Intent(MainActivity.this,
        LoginActivity.class);
    
        startActivityForResult(intent, 0);
        
        finish();
        
    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

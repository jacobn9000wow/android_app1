package com.example.weatherjson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//http://ofps.oreilly.com/titles/9781118177679/network_programming.html
public class ReadWeatherJSONFeedTask extends AsyncTask <String, Void, String> {
	
	Context base_context;
	
	ReadWeatherJSONFeedTask(Context c) {
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
            JSONObject weatherObservationItems = new
                JSONObject(jsonObject.getString("weatherObservation"));

            Toast.makeText(base_context,
                    weatherObservationItems.getString("clouds") +
                    " - " + weatherObservationItems.getString("stationName"),
                    Toast.LENGTH_SHORT).show();
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

package com.example.weatherjson;

import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Parcel;
import android.os.Parcelable;

public class SingletonDefaultHttpClient extends DefaultHttpClient {

	private static SingletonDefaultHttpClient unique_instance_client = new SingletonDefaultHttpClient();
	
	private SingletonDefaultHttpClient() {} //constructor
	
	public static SingletonDefaultHttpClient getInstance() {	//does this really need to be synchronized? I'm not using threads, but Android probably is
		//if(unique_instance_client == null) {
		//	unique_instance_client = new SingletonDefaultHttpClient();
		//}
		return unique_instance_client;
	}

}

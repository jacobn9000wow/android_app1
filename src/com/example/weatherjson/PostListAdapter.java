package com.example.weatherjson;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostListAdapter extends BaseAdapter{

	private Activity activity;
	
	private ArrayList<String> authors;
	private ArrayList<String> contents;
	private ArrayList<String> number_replies;
	private ArrayList<String> times;
	
	private LayoutInflater inflater;//=null;
	
	public PostListAdapter(Activity a, ArrayList<String> au, ArrayList<String> c, ArrayList<String> n, ArrayList<String> t) {
		
		super();
		
		authors = au;
		contents = c;
		number_replies = n;
		times = t;
		
		activity = a;
		
		//inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.inflater = LayoutInflater.from(a);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.authors.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.contents.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.post_xml_row, null);
        
        TextView author = (TextView)vi.findViewById(R.id.author); // 
        TextView content = (TextView)vi.findViewById(R.id.postcontent); // 
        TextView time = (TextView)vi.findViewById(R.id.time); // 
        TextView num_comments = (TextView)vi.findViewById(R.id.num_comment); // 
        
        String au = authors.get(position);
        String c = contents.get(position);
        String n = number_replies.get(position);
        String t = times.get(position);
        
        // Setting all values in listview
        author.setText(au);
        content.setText(c);
        time.setText(t);
        num_comments.setText(n);
		
		return vi;
	}

}

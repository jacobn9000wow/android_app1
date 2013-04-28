package com.example.weatherjson;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomListAdapter extends BaseAdapter{

    private Activity activity;
	
	private ArrayList<String> room_names;
	private ArrayList<String> times;
	private ArrayList<String> num_members;
	
	private LayoutInflater inflater;//=null;
	
    public RoomListAdapter(Activity a, ArrayList<String> names, ArrayList<String> t, ArrayList<String> m) {
		
		super();
		
		room_names = names;
		times = t;
		num_members = m;
		
		activity = a;
		
		//inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.inflater = LayoutInflater.from(a);
	}
	
    @Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.room_names.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.room_names.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.room_xml_row, null);
        
        TextView name = (TextView)vi.findViewById(R.id.room_name); // 
        TextView time_of_last_post = (TextView)vi.findViewById(R.id.time_last_post); // 
        TextView members = (TextView)vi.findViewById(R.id.num_members); // 
        
        String n = room_names.get(position);        
        String m = num_members.get(position);
        String t = times.get(position);
        
        // Setting all values in listview
        name.setText(n);
        time_of_last_post.setText(t);
        members.setText(m);
		
		return vi;
	}

}

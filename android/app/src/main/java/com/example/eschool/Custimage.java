package com.example.eschool;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Custimage extends ArrayAdapter<String>  {

	 private Activity context;       //for to get current activity context
	    SharedPreferences sh;
	private String[]material_path,title,staff_name;
	
	 public Custimage(Activity context, String[] material_path, String[] title, String[] staff_name) {
	        //constructor of this class to get the values from main_activity_class

	        super(context, R.layout.cust_images, material_path);
	        this.context = context;
	        this.material_path = material_path;
		 	this.title = title;
		 	this.staff_name = staff_name;

	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	                 //override getView() method

	        LayoutInflater inflater = context.getLayoutInflater();
	        View listViewItem = inflater.inflate(R.layout.cust_images, null, true);
			//cust_list_view is xml file of layout created in step no.2

	        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView1);
	        TextView t1=(TextView)listViewItem.findViewById(R.id.tvtitle);
			TextView t2=(TextView)listViewItem.findViewById(R.id.tvstaff);


	        t1.setText("Title : " + title[position]);
			t2.setText("Staff Name : " + staff_name[position]);


	        sh=PreferenceManager.getDefaultSharedPreferences(getContext());
	        
	       String pth = "http://"+sh.getString("ip", "")+"/"+material_path[position];
	       pth = pth.replace("~", "");
//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();
	        
	        Log.d("-------------", pth);
	        Picasso.with(context)
	                .load(pth)
	                .placeholder(R.drawable.ic_launcher_background)
	                .error(R.drawable.ic_launcher_background).into(im);
	        
	        return  listViewItem;
	    }

		private TextView setText(String string) {
			// TODO Auto-generated method stub
			return null;
		}
}
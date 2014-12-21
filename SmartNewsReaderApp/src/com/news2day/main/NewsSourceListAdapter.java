package com.news2day.main;
 
import java.util.ArrayList;

import com.news2day.R;
import com.news2day.models.NewsSourceList;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class NewsSourceListAdapter extends BaseAdapter {
     
    private Context context;
    private ArrayList<NewsSourceList> newsSourceItems;
     
    public NewsSourceListAdapter(Context context, ArrayList<NewsSourceList> newsSourceItems){
        this.context = context;
        this.newsSourceItems = newsSourceItems;
    }
 
    public int getCount() {
        return newsSourceItems.size();
    }
 
    public Object getItem(int position) {       
        return newsSourceItems.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
          
        imgIcon.setImageResource(newsSourceItems.get(position).getSourceImage());        
        txtTitle.setText(newsSourceItems.get(position).getSourceTitle());
         
        // displaying count
        // check whether it set visible or not
        if(newsSourceItems.get(position).getCounterVisibility()){
            txtCount.setText(newsSourceItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
         
        return convertView;
    }
 
}

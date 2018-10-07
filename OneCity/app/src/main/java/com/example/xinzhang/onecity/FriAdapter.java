/*
test test test
*/
package com.example.xinzhang.onecity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class FriAdapter extends BaseAdapter {

    List<Map<String, Object>> data;
    Context context;
    LayoutInflater inflater;

    public FriAdapter(Context context, List<Map<String,Object>> data){
        this.context=context;
        this.data=data;
        this.inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    private final class Establish{
        public TextView time,name;
        public ImageView customerhead;
    }

    @Override
    public Object getItem(int position){
        return data.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Establish establish=null;
        if(convertView==null){
            establish=new Establish();
            convertView=inflater.inflate(R.layout.friendlistview,null);
            establish.name=(TextView)convertView.findViewById(R.id.frilist_name);
            establish.time=(TextView)convertView.findViewById(R.id.frilist_time);
            establish.customerhead=(ImageView)convertView.findViewById(R.id.frilist_userhead);
            convertView.setTag(establish);
        }
        else{
            establish=(Establish)convertView.getTag();
        }
        System.out.println(position);

        establish.time.setText((String)data.get(position).get("time"));
        establish.name.setText((String)data.get(position).get("name"));
        establish.customerhead.setBackgroundResource((Integer)data.get(position).get("customerhead"));

        return convertView;

    }

}

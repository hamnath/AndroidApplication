package com.example.vista.androidapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.activities.activity_list;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.common.downloadImageFromURL;
import com.example.vista.androidapplication.properties.activity_list_properties;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ActivityListAdapter extends ArrayAdapter<activity_list_properties> {

    private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    private final LinkedList <Drawable> mChacheController = new LinkedList <Drawable> ();
    public static final int MAX_CACHE_SIZE = 80;

    ArrayList<activity_list_properties> item;

    Context mContext;
    public ActivityListAdapter(Context context, int textViewResourceId, ArrayList<activity_list_properties> items)
    {
        super(context, textViewResourceId, items);
        this.item = items;
        this.mContext = context;
    }

    @Override
    public View getView(final int position,View convertView,ViewGroup parent) {
        // LayoutInflater inflater= (LayoutInflater)mContext.getLayoutInflater();
        // View rowView=inflater.inflate(R.layout.activity_list, null,true);

        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_list , null);
        }

        final activity_list_properties obj_activity_properties = item.get(position);

        //System.out.println("position" + position);

        if (obj_activity_properties != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            ImageView rt = (ImageView) v.findViewById(R.id.rightview);
            ImageView li = (ImageView) v.findViewById(R.id.listImage);
            if (tt != null)
            {
                tt.setText(obj_activity_properties.getTitle());
                //System.out.println("text "+ obj_activity_properties.getTitle());
            }

            if(bt != null)
            {
                if(obj_activity_properties.getActivityType().equals("1"))
                {
                    bt.setText("Requested : " + obj_activity_properties.getDate());
                }
                else
                {
                    bt.setText(obj_activity_properties.getDate());
                }
            }

            if(rt != null)
            {
                rt.setTag(obj_activity_properties.getId());
                //System.out.println("ID "+obj_activity_properties.getId());
            }

            if(li != null)
            {
                //System.out.println("LoadImageFromWebOperations 11 "+ obj_activity_properties.getImage());
                Drawable d = null;
                d = getDrawableFromCache(obj_activity_properties.getImage());

                if(d != null)
                {
                    li.setImageDrawable(d);
                    System.out.println("setImageDrawable  "+obj_activity_properties.getImage());
                }
                else
                {
                    //System.out.println("LoadImageFromWebOperations 22 "+obj_activity_properties.getImage());

                   /* d = LoadImageFromWebOperations(obj_activity_properties.getImage());
                    if(d != null)
                    {
                        li.setImageDrawable(d);
                        putDrawable(obj_activity_properties.getImage(),d);
                        System.out.println("setImageDrawable "+li);
                    }   */

                    downloadImageFromURL objdown = new downloadImageFromURL(obj_activity_properties.getImage(),li);
                    objdown.execute(obj_activity_properties.getImage());
                }
            }

            if(obj_activity_properties.getActivityType().equals("1"))
            {

                ImageView accept = (ImageView) v.findViewById(R.id.accept);
                accept.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View vv) {
                        // TODO Auto-generated method stub
                        // activity_list obj = new activity_list();
                        // obj.removeActivity(obj_activity_properties);
                       // acceptConnect("A",o.getItemId());
                       // System.out.println("accept"+item.remove(0) );
                    }
                });
                accept.setVisibility(View.VISIBLE);
                ImageView notnow = (ImageView) v.findViewById(R.id.notnow);
                notnow.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View vv) {
                        // TODO Auto-generated method stub
                        //removeActivity(o);
                       // acceptConnect("I",o.getItemId());
                        System.out.println("Reject");
                    }
                });
                notnow.setVisibility(View.VISIBLE);
            }
            else
            {
                ImageView accept = (ImageView) v.findViewById(R.id.accept);
                accept.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View vv) {
                        // TODO Auto-generated method stub
                        //removeActivity(o);
                        //acceptConnect("A",o.getItemId());
                    }
                });
                accept.setVisibility(View.GONE);
                ImageView notnow = (ImageView) v.findViewById(R.id.notnow);
                notnow.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View vv) {
                        // TODO Auto-generated method stub
                       // removeActivity(o);
                      //  acceptConnect("I",o.getItemId());
                    }
                });
                notnow.setVisibility(View.GONE);
            }

            rt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   // removeActivity(o);
                   // deleteActivity(o.getId());
                    System.out.println(" setOnClickListener ");

                }
            });

            OystorApp.obj_activity_list= obj_activity_properties;
            v.setId(Integer.parseInt(obj_activity_properties.getId()));
        }
        return v;
    }

    private Drawable getDrawableFromCache(String url) {
        if (mCache.containsKey(url)) {
            return mCache.get(url).get();
        }

        return null;
    }

    };




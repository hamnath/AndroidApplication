package com.example.vista.androidapplication.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.downloadImage;
import com.example.vista.androidapplication.common.downloadImageFromURL;
import com.example.vista.androidapplication.files_folders.files_folders_details;
import com.example.vista.androidapplication.properties.Philosopher;
import com.example.vista.androidapplication.properties.shared_list_properties;
import com.example.vista.androidapplication.shared.shared_file_details;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SharedListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<shared_list_properties>> _listDataChild;

    private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    private final LinkedList <Drawable> mChacheController = new LinkedList<Drawable>();
    public static final int MAX_CACHE_SIZE = 80;
    Fragment fragment;

    public SharedListAdapter(Context context, List<String> sharedlistHeader,
                                 HashMap<String, List<shared_list_properties>> sharedlistChild) {

        System.out.println("Entered Constructor ");
        this._context = context;
        this._listDataHeader = sharedlistHeader;
        this._listDataChild = sharedlistChild;

         System.out.println("Entered Constructor "+ _listDataChild.get("Shared by me").size());
         System.out.println("Entered _listDataHeader 1 " +  _listDataHeader.get(0));
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        System.out.println("Group View Entered" + isExpanded);

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.shared_file_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        System.out.println("groupPosition " + groupPosition);
        System.out.println("childPosition" + childPosition);

        //final String childText = (String) getChild(groupPosition, childPosition);
        ArrayList<String> result = (ArrayList<String>) getChild(groupPosition,childPosition);

        System.out.println("groupPosition11" + groupPosition);
        System.out.println("childPosition12" + childPosition);
        System.out.println("childText" + result.size());

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.shared_file_list_item, null);
        }

        TextView txtSubject = (TextView) convertView.findViewById(R.id.toptext);
        TextView txtSharedOn = (TextView) convertView.findViewById(R.id.bottomtext);
        TextView txtPeopleCount = (TextView) convertView.findViewById(R.id.peoplecount);
        TextView txtFileCount = (TextView) convertView.findViewById(R.id.filecount);
        ImageView imgUserImage = (ImageView) convertView.findViewById(R.id.listImage);

        txtSubject.setText(result.get(0));
        txtSharedOn.setText(result.get(1));
        txtFileCount.setText(result.get(2));

        if(result.get(3) != null)
            txtPeopleCount.setText(result.get(3));
        else
            txtPeopleCount.setText("1");

        if(imgUserImage != null)
        {
            if(result.get(4).equals("") ||result.get(4).equals("anyType{}"))
            {
                imgUserImage.setImageResource(R.drawable.no_user_image);
            }
            else
            {
                Drawable d = null;
                d = getDrawableFromCache(result.get(4));
                if(d != null)
                {
                    imgUserImage.setImageDrawable(d);
                }
                else
                {	System.out.println("users"+result.get(4));

                    try {
 	            	/* This method getting download from url and set to the image view */
                        downloadImage objdown = new downloadImage(result.get(4));
                        imgUserImage.setImageDrawable(objdown.execute(result.get(4)).get());
                    }catch (Exception ex)
                    {
                        System.out.println("Exception in download Image");
                    }
                }
            }
        }

        // set the ClickListener to handle the click event on child item
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("Entered View setOnClickListener");

                final String share_id =_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getShareId();
                final String user_image=_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getUserImage();
                final String Parent_Id=_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getParentId();

                System.out.println("setOnClickListener" + user_image);

                fragment = new shared_file_details();
                try {
                    if (fragment != null) {
                        final Context context = view.getContext();
                        FragmentManager fragmentManager =  ((Activity) context).getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("share_id", share_id);
                        args.putString("userImage", user_image);
                        args.putString("Parent_Id",Parent_Id);
                        fragment.setArguments(args);

                        ft.replace(R.id.frame_container, fragment);
                        ft.commit();
                    }
                }
                catch (Exception ex) {
                    System.out.println("Error file click in Grid View" + ex);
                }
            }
        });

        return convertView;
    }

    private Drawable getDrawableFromCache(String url) {
        if (mCache.containsKey(url)) {
            return mCache.get(url).get();
        }

        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        ArrayList<String> objResult = new ArrayList<String>();

        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getSubject());
        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getSharedOn());
        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getTotalFiles());
        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getUsersCount());
        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getUserImage());

        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getShareId());
        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getParentId());

        System.out.println("Getting Error on getSharedUserName");
        objResult.add(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosititon).getSharedUserName());

        return objResult;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        System.out.println("get child count"+this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size());
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}

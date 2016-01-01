package com.example.vista.androidapplication.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.common.downloadImage;
import com.example.vista.androidapplication.common.downloadImageFromURL;
import com.example.vista.androidapplication.contacts.contactlist;
import com.example.vista.androidapplication.contacts.editContact;
import com.example.vista.androidapplication.properties.contact_list_properties;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ContactListAdapter extends ArrayAdapter<contact_list_properties>
{
    private ArrayList<contact_list_properties> originalitems;
    private ArrayList<contact_list_properties> filtered;
    private ArrayList<contact_list_properties> fullitems;
    private Filter filter;

    private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    private final LinkedList <Drawable> mChacheController = new LinkedList<Drawable>();
    public static final int MAX_CACHE_SIZE = 80;
    contactlist obj_contact_list = new contactlist();

    private int count=0;
    public String FilterText = "";
    Context mContext;
    Fragment fragment = null;

    public ContactListAdapter(Context context, int listViewResourceId, ArrayList<contact_list_properties> items) {
        super(context, listViewResourceId, items);
        this.filtered = new ArrayList<contact_list_properties>(items);
        this.originalitems = items;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.contact_list, null);
        }

        if (count == 0) {
            fullitems = new ArrayList<contact_list_properties>(originalitems);
        }
        count++;
        final contact_list_properties obj_contact_list = originalitems.get(position);
        if (obj_contact_list != null) {
            TextView tt = (TextView)v.findViewById(R.id.toptext);
            TextView bt = (TextView)v.findViewById(R.id.bottomtext);
            ImageView li= (ImageView)v.findViewById(R.id.listImage);
            if (tt != null) {
                tt.setText(obj_contact_list.getname());
            }

            if (bt != null) {
                if (obj_contact_list.getis_email_visible().equals("Y")) {
                    bt.setText(obj_contact_list.getemail());
                } else if (obj_contact_list.getis_email_visible().equals("N")) {
//        	            	bt.setText("Email is Hidden");
                    bt.setText("");
                } else if (obj_contact_list.getis_email_visible().equals("anyType{}")) {
                    bt.setText(obj_contact_list.getemail());
                } else {
                    bt.setText("");
                }
            }

            if (li != null) {
                if (obj_contact_list.getis_photo_visible().equals("Y"))
                {
                    Drawable d = null;
                    d = getDrawableFromCache(obj_contact_list.getuser_image());
                    if (d != null) {
                        li.setImageDrawable(d);
                    } else {
                       //System.out.println("entered Load Image From Web Operations ");

                        /*d = LoadImageFromWebOperations(o.getuser_image());
                        if (d != null) {
                            li.setImageDrawable(d);
                            putDrawable(o.getuser_image(), d);
                        }*/

                     /* This method getting download from url and set to the image view */
                      //  downloadImageFromURL objdown = new downloadImageFromURL(obj_contact_list.getuser_image(),li);
                      //  objdown.execute(obj_contact_list.getuser_image());

                        if(obj_contact_list.getuser_image().equals("") ||obj_contact_list.getuser_image().equals("anyType{}"))
                        {
                            li.setImageResource(R.drawable.no_user_image);
                        }
                        try {
                            downloadImage objdown = new downloadImage(obj_contact_list.getuser_image());
                            li.setImageDrawable(objdown.execute(obj_contact_list.getuser_image()).get());
                        }
                        catch (Exception ex)
                        {
                            System.out.println("ex " +ex);
                        }
                    }
                } else {
                    li.setImageResource(R.drawable.no_user_image);
                }
            }

            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    fragment = new editContact();

                    try {
                        if (fragment != null) {

                            OystorApp.obj_edit_contacts_list = obj_contact_list;

                            final Context context = v.getContext();
                            FragmentManager fragmentManager =  ((Activity) context).getFragmentManager();
                            FragmentTransaction ft = fragmentManager.beginTransaction();

                           /* Bundle args = new Bundle();
                            args.putString("contact_id",  obj_contact_list.getcontact_id());
                            args.putString("user_id",  obj_contact_list.getuser_id());
                            args.putString("email",  obj_contact_list.getemail());
                            args.putString("name",  obj_contact_list.getname());
                            args.putString("contact_number",  obj_contact_list.getcontact_number());
                            args.putString("company", obj_contact_list.getcompany());
                            args.putString("designation", obj_contact_list.getdesignation());
                            //System.out.println("Secemail"+obj_contact_list.getsecondary_email());
                            args.putString("skypeName", obj_contact_list.getskype_name());
                            args.putString("secondary_email",  obj_contact_list.getsecondary_email());
                            //System.out.println("conact no email"+obj_contact_list.getcontact_number());
                            args.putString("user_image", obj_contact_list.getuser_image());
                            args.putString("is_email_visible", obj_contact_list.getis_email_visible());
                            args.putString("is_photo_visible", obj_contact_list.getis_photo_visible());
                            //System.out.println(obj_contact_list.getuser_image()+"ASUS");
                            fragment.setArguments(args);    */

                            ft.add(R.id.frame_container, fragment);
                            ft.addToBackStack("fragBack");
                            ft.commit();


                        }
                    }
                    catch (Exception ex)
                    {
                        System.out.println("Err" + ex);
                    }
                }
            });


            //Long press event handler

            v.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg) {
                    //  Toast.makeText(getContext(), "Long Clicked "+o.getemail(), Toast.LENGTH_SHORT).show();
                    OystorApp.obj_edit_contacts_list = obj_contact_list;
                    return false;
                }
            });



//            v.setOnLongClickListener(new View.OnLongClickListener() {
//
//                public boolean onLongClick(View v) {
//                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
//
//                //Inflating the Popup using xml file
//                popupMenu.getMenuInflater().inflate(R.menu.context_resource, popupMenu.getMenu());
//
//                 //registering popup with OnMenuItemClickListener
//                 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                        public boolean onMenuItemClick(MenuItem item) {
//                            Toast.makeText(getContext(),"Button Clicked : " + item.getItemId() + item.getTitle() + o.getname(),Toast.LENGTH_SHORT).show();
//                            return true;
//                        }
//                 });
//
//                 popupMenu.show();//showing popup menu
//                 return true;    // <- set to true
//                }
//            });


        }
        return v;
    }


    @Override
    public Filter getFilter()
    {
        if(filter == null)
            filter = new ContactListAdapterFilter();
        return filter;
    }

    private Drawable getDrawableFromCache(String url) {
        if (mCache.containsKey(url)) {
            return mCache.get(url).get();
        }

        return null;
    }

    private Drawable LoadImageFromWebOperations(String path)
    {
        try{
            Drawable d = null;
            System.out.println("path " + path);
            InputStream is = (InputStream) new URL(path).getContent();
            d = Drawable.createFromStream(is, "src name");
            //Drawable d = Drawable.createFromPath(path);
            return d;
        }catch (Exception e) {
            System.out.println("Exception  LoadImageFromWebOperations="+e);
            return null;
        }
    }

    private class ContactListAdapterFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterText = (String) constraint;
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<contact_list_properties> filt = new ArrayList<contact_list_properties>();
                ArrayList<contact_list_properties> lItems = new ArrayList<contact_list_properties>(fullitems);
                for(int i = 0, l = lItems.size(); i < l; i++)
                {
                    contact_list_properties m = lItems.get(i);
                    if(m.getname().toLowerCase().contains(constraint))
                        filt.add(m);
                }
                result.count = filt.size();
                result.values = filt;
            }
            else
            {
                ArrayList<contact_list_properties> list = new ArrayList<contact_list_properties>(fullitems);
                result.values = list;
                result.count = list.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            filtered = (ArrayList<contact_list_properties>)results.values;
            originalitems.clear();
            for(int i = 0, l = filtered.size(); i < l; i++)
                originalitems.add(filtered.get(i));

            notifyDataSetChanged();
        }
    }
}


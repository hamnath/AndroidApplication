package com.example.vista.androidapplication.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.demo;
import com.example.vista.androidapplication.files_folders.files_folder_list;
import com.example.vista.androidapplication.files_folders.files_folders_details;
import com.example.vista.androidapplication.properties.files_folders_properties;

import java.util.ArrayList;

public class FilesFoldersAdapter extends ArrayAdapter<files_folders_properties> {
    private ArrayList<files_folders_properties> originalitems;
    private ArrayList<files_folders_properties> filtered;
    private ArrayList<files_folders_properties> fullitems;
    public String currentFolderId = "0", previousFolderId = "0", rootFolderId = "0";
    public ArrayList<Path> currentPaths = new ArrayList<Path>();
    private OystorApp OA = new OystorApp();
    private Filter filter;
    private int count = 0;
    Context mContext;
    files_folder_list obj_files_folder_list = new files_folder_list();
    Fragment fragment = null;

    public FilesFoldersAdapter(Context context, int textViewResourceId, ArrayList<files_folders_properties> items) {
        super(context, textViewResourceId, items);
        this.filtered = new ArrayList<files_folders_properties>(items);
        this.originalitems = items;
        this.mContext = context;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.folder_list, null);

        }

        if (count == 0) {
            fullitems = new ArrayList<files_folders_properties>(originalitems);
        }
        count++;
        System.out.println("getView 1");
        final files_folders_properties o = originalitems.get(position);
        if (o != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            TextView rt = (TextView) v.findViewById(R.id.righttext);
            ImageView li = (ImageView) v.findViewById(R.id.listImage);

            if (o.getRowType().equals("D")) {
                if (tt != null) {
                    tt.setText(o.getName());
                }
                if (bt != null) {
                    if (o.getNoOfFiles().equals("0") && o.getNoOfFolders().equals("0"))
                        bt.setText("No Files and  Folders");
                    else
                        bt.setText(o.getNoOfFiles() + " Files  " + o.getNoOfFolders() + " Folders");
                }
                if (rt != null) {

                    if (o.getisShared().equals("Y")) {
                        rt.setText(o.getCreatedby());
                    } else
                        rt.setText("");

                }

                System.out.println("ishsared 1" + o.getisShared());
                if (li != null) {
                    if (o.getisShared().equals("Y")) {
                        System.out.println("ishsared 2" + o.getisShared());
                        li.setImageResource(R.drawable.folder_share);
                    } else
                        li.setImageResource(R.drawable.folder);
                }

               v.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        String folderId = "";
                        String title = "";
                        System.out.println("On Click Event Entered");
                        OystorApp.folderId = o.getId();
                        OystorApp.is_shared = o.getisShared();

                        fragment = new files_folder_list();
                        try {
                            if (fragment != null) {
                                final Context context = parent.getContext();
                                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.add(R.id.frame_container, fragment);
                                ft.addToBackStack("fragBack");
                                ft.commit();
                            }
                        } catch (Exception ex) {
                            System.out.println("Error folder click in List View" + ex);
                        }
                    }
                });

            } else {
                if (tt != null) {
                    tt.setText(o.getName());
                }
                if (bt != null) {
                    bt.setText(o.getSize());
                }
                if (rt != null) {
                    rt.setText(o.getDate());
                }
                if (li != null) {
                    String Ext = "drawable/file_extension_" + o.getExt().toLowerCase();
                    System.out.println("Img Ext" + Ext);
                    int imageResource = this.mContext.getResources().getIdentifier(Ext, null, this.mContext.getPackageName());
                    System.out.println("Img Resource" + imageResource);

                    if (imageResource > 0) {
                        li.setImageResource(imageResource);
                    } else {
                        li.setImageResource(R.drawable.file_extension_default);
                    }
                }

                v.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        System.out.println("Entered View Doc setOnClickListener" );
                        System.out.println("Entered View Doc setOnClickListener" +v);

                                fragment = new files_folders_details();
                        try {
                            if (fragment != null) {
                                Context context = parent.getContext();
                                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();

                                Bundle args = new Bundle();
                                args.putString("docId", o.getId());
                                args.putString("name", o.getFullname());
                                args.putString("size", o.getSize());
                                args.putString("type", o.getTypePrivate());
                                args.putString("path", o.getPath());
                                args.putString("uploadedDate", o.getDate());
                                args.putString("expiryDate", o.getExpiry());
                                args.putString("doctype", o.getType());

                                fragment.setArguments(args);

                                ft.add(R.id.frame_container, fragment);
                                ft.addToBackStack("fragBack");
                                ft.commit();
                            }
                        } catch (Exception ex) {
                            System.out.println("Error file click in List View" + ex);
                        }

                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg) {
                        System.out.println("Long Click");
                        //Toast.makeText( getContext()  , "Long Clicked in files and folder "  , Toast.LENGTH_SHORT).show();

                        OystorApp.docId = o.getId();
                        return false;
                    }

                });
            }
        }
        return v;
    }


    public void filesLoad()
    {

    }

    public class Path {
        private String Id, Name;

        public String getId() {
            return Id;
        }
        public String getName() {
            return Name;
        }

        public Path(String getId, String getName)
        {
            Id = getId;
            Name = getName;
        }
    }

}


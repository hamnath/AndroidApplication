package com.example.vista.androidapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.vista.androidapplication.adapter.GridViewAdapter;
import com.example.vista.androidapplication.adapter.ImageItem;

import java.util.ArrayList;

//Class for Grid View

public class Gridmainactivity extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(Gridmainactivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
    }   */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid, container, false);

        System.out.println("Welcome to Entered the GridView");

       // gridView = (GridView)rootView.findViewById(R.id.gridView);
       // gridAdapter = new GridViewAdapter(rootView.getContext(), R.layout.grid_item_layout, getData());
       // gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                System.out.println("Welcome to item GridView");

                //Create intent
               /* Intent intent = new Intent(rootView.getContext(), DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());  */

                Fragment fragment = null;

                fragment = new DetailsActivity();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    android.app.FragmentTransaction ft= fragmentManager.beginTransaction();
                   // ft.replace(R.id.frame_container, fragment).commit();
                    Bundle args = new Bundle();
                    args.putString("title",  item.getTitle());
                    args.putParcelable("image", item.getImage());
                    fragment.setArguments(args);
                    ft.replace(R.id.frame_container, fragment);
                    ft.commit();





                    // update selected item and title, then close the drawer

                }
                //Start details activity
                //startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }
}

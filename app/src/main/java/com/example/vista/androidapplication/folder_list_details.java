package com.example.vista.androidapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vista.androidapplication.common.OystorApp;

public class folder_list_details extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.files_folder_details, container, false);

        Bundle bundle = this.getArguments();

        TextView titleTextView = (TextView) rootView.findViewById(R.id.title);
        titleTextView.setText(bundle.getString("title"));

         ImageView imageView = (ImageView)rootView.findViewById(R.id.image);
        //Bitmap bmp =bundle.getParcelable("image");
        //imageView.setImageBitmap(bmp);
         imageView.setImageResource(R.drawable.folder);

        return rootView;
    }




}

package com.example.vista.androidapplication;

//Class for Grid View

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vista.androidapplication.common.OystorApp;

public class DetailsActivity extends Fragment {

     /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_activity, container, false);

        // String title = getIntent().getStringExtra("title");
        //  Bitmap bitmap = getIntent().getParcelableExtra("image");

        Bundle bundle = this.getArguments();

        TextView titleTextView = (TextView) rootView.findViewById(R.id.title);
        titleTextView.setText(bundle.getString("title"));

        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        Bitmap bmp = bundle.getParcelable("image");
        imageView.setImageBitmap(bmp);

        return rootView;
    }



}

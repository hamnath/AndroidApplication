package com.example.vista.androidapplication.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vista.androidapplication.R;


import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class downloadImage extends AsyncTask<String, String, Drawable> {
    private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    private final LinkedList<Drawable> mChacheController = new LinkedList<Drawable>();
    public static final int MAX_CACHE_SIZE = 80;

    String imageURL;

    public downloadImage(String url) {
        imageURL = url;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        try {
            Drawable d = null;

            System.out.println("doInBackground Path " + imageURL);
            InputStream is = (InputStream) new URL(imageURL).getContent();
            d = Drawable.createFromStream(is, "src name");
            //Drawable d = Drawable.createFromPath(path);
            //System.out.println("createFromStream LoadImageFromWebOperations="+d);

            //Bitmap bm = BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.no_user_image);
            //roundedImage = new RoundImage(bm);

            return d;
        } catch (Exception e) {
            System.out.println("Exception doInBackground LoadImageFromWebOperations=" + e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable result) {
         System.out.println("Put Drawable"+result);
        //imgview.setImageDrawable(result);
        //putDrawable(imageURL,result);
    }

    public synchronized void putDrawable(String url,Drawable drawable)
    {
        int chacheControllerSize = mChacheController.size();
        if (chacheControllerSize > MAX_CACHE_SIZE)
            mChacheController.subList(0, MAX_CACHE_SIZE/2).clear();

        mChacheController.addLast(drawable);
        mCache.put(url, new SoftReference<Drawable>(drawable));
    }
}
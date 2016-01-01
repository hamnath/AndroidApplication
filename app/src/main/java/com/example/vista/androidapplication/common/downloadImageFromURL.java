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

public class downloadImageFromURL extends AsyncTask<String, String, Drawable> {
    private final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    private final LinkedList<Drawable> mChacheController = new LinkedList<Drawable>();
    public static final int MAX_CACHE_SIZE = 80;

    String imageURL;
    ImageView imgview;


    public View getView(int position, View convertView, ViewGroup parent) {



        return convertView;
    }

    public downloadImageFromURL(String url,ImageView li) {
        // TODO Auto-generated constructor stub
        imageURL=url;
        imgview=li;
    }

    RoundImage roundedImage;
    @Override
    protected Drawable doInBackground(String... params) {
        try{
            Drawable d = null;

            System.out.println("doInBackground Path " + imageURL);
            InputStream is = (InputStream) new URL(imageURL).getContent();
            d = Drawable.createFromStream(is, "src name");
            //Drawable d = Drawable.createFromPath(path);
            //System.out.println("createFromStream LoadImageFromWebOperations="+d);

            //Bitmap bm = BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.no_user_image);
            //roundedImage = new RoundImage(bm);


            return d;
        }catch (Exception e) {
            System.out.println("Exception doInBackground LoadImageFromWebOperations="+e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable result) {
        //System.out.println("Put Drawable"+result);
        imgview.setImageDrawable(result);
        // imgview.setImageBitmap(getRoundedShape(decodeFile(cntx, R.drawable.no_user_image),200));
        putDrawable(imageURL, result);
    }



    public synchronized void putDrawable(String url,Drawable drawable)
    {
        int chacheControllerSize = mChacheController.size();
        if (chacheControllerSize > MAX_CACHE_SIZE)
            mChacheController.subList(0, MAX_CACHE_SIZE/2).clear();

        mChacheController.addLast(drawable);
        mCache.put(url, new SoftReference<Drawable>(drawable));
    }

    static Context mcontext;
    Activity cntx;
    public static Bitmap decodeFile(Context context,int resId) {
        try {
// decode image size
            mcontext=context;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(mcontext.getResources(), resId, o);


// Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 200;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true)
            {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
// decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeResource(mcontext.getResources(), resId, o2);
        } catch (Exception e) {
        }
        return null;
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }


}
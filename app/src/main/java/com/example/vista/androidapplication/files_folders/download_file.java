package com.example.vista.androidapplication.files_folders;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.Common;
import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.common.OystorApp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class download_file extends AsyncTask<Context, Integer, String> {
    public ProgressDialog progloader;
    public OystorApp OA;
    public String downloadFolder, s3accesskey, s3secretkey, bucketname, fileName, fileLoc, path, size, rootpath, fileType="";
    public int tabid,totalSize,APPID = Constants.APPID;
    private Context activity;
    public Notification notification = null;
    public NotificationManager mManager = null;
    Fragment fragment;
    boolean isView = false;
    public download_file(Context activityRec, boolean isView) {

        this.activity = activityRec;
        this.isView =isView;

        progloader = new ProgressDialog(this.activity);
        progloader.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progloader.setMessage("Processing your Request");
        progloader.setMax(0);
        progloader.show();
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    protected String doInBackground(Context... context)
    {
        try {
            System.out.println("On doInBackground Download file");

            String extStorageDirectory = rootpath;
            if(path.equals("anyType{}"))
            {
                path = "";
            }
            else
            {
                path = path.replaceAll(OA.specialCharExp, OA.specialCharRep);
            }
            fileName = fileName.replaceAll(OA.specialCharExp, OA.specialCharRep).toLowerCase();

            String filepath = extStorageDirectory + downloadFolder + "/" + path;
            OystorApp.download_file_path = filepath;

            File myDownloads = new File(filepath);
            myDownloads.mkdirs();
            if(!path.equals(""))
            {
                filepath += "/";
            }

            AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( s3accesskey, s3secretkey ) );
            GetObjectRequest request = new GetObjectRequest(bucketname,fileLoc);
            S3Object object = s3Client.getObject(request);

            InputStream reader = new BufferedInputStream(object.getObjectContent());

            File file = new File(filepath + fileName);
            OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

            Double total = Double.parseDouble(size) * 1024 / 100;
            int percent = 0;
            int read = -1;
            int count = 0;
            int totalCount = 0;
            int sizeinbytes = (int) (Double.parseDouble(size) * 1024);

            totalSize = sizeinbytes;
            progloader.setMax(totalSize);

            // Code for Notification Content
            mManager = (NotificationManager)this.activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notification = new Notification(R.drawable.icon, "Downloading", System.currentTimeMillis());

            RemoteViews contentView = new RemoteViews(this.activity.getPackageName(), R.layout.custom_notification_layout);
            contentView.setProgressBar(R.id.progressBar, totalSize, 0, false);
            contentView.setTextViewText(R.id.text, " Downloading  - " + fileName);
            notification.contentView = contentView;

            //Intent notificationIntent = new Intent(this.activity, downloads1.class);
            //notificationIntent.putExtra("openPath", path);

            System.out.println("fragment downloads");

//            fragment = new downloads();
//            if(fragment != null)
//            {
//                System.out.println("Entered Fragment......");
//                FragmentManager fragmentManager = ((Activity) activity).getFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.add(R.id.frame_container, fragment);
//                ft.addToBackStack("fragBack");
//                ft.commit();
//
//            }

             System.out.println("fragment downloads 222");
            //PendingIntent contentIntent = PendingIntent.getActivity(this.activity, (int) System.currentTimeMillis(), notificationIntent, notification.FLAG_AUTO_CANCEL);
//            notification.contentIntent = contentIntent;

            APPID = (int) System.currentTimeMillis();
            mManager.notify(APPID, notification);
            // Code for Notification Content

            while ( ( read = reader.read() ) != -1 ) {
                writer.write(read);
                count++;
                totalCount++;
                if(count > total)
                {
                    count = 0;
                    percent++;
                    publishProgress(totalCount);
                }
            }

            writer.flush();
            writer.close();
            reader.close();
            progloader.dismiss();
            return filepath + fileName;
        }
        catch ( Exception e ) {
            progloader.dismiss();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        try {
            if(percent[0] < 10 && percent[0] > 0)
            {
                if(fileType.equals("3") || fileType.equals("4"))
                {
                    progloader.setMessage("Streaming");
                }
                else
                {
                    progloader.setMessage("Downloading");
                }
            }

            progloader.setProgress(percent[0]);
            int temp = (int) percent[0] * 100/totalSize;
            if( temp%10 == 0)
            {
                notification.contentView.setProgressBar(R.id.progressBar, totalSize, percent[0], false);
                // notify the notification manager on the update.
                mManager.notify(APPID, notification);
            }

            if(temp >= 99)
            {
                notification.contentView = new RemoteViews(activity.getPackageName(), R.layout.custom_notification_done);
                notification.contentView.setTextViewText(R.id.textname, fileName);
                notification.contentView.setImageViewResource(R.id.oystoricon, R.drawable.icon);
                mManager.notify(APPID, notification);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        progloader.setMessage("Downloaded Successfully");
      	progloader.dismiss();

        if(isView) {
            Common obj_common = new Common();
            obj_common.viewDocument(this.activity, result);
        }
    }
}
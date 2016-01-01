package com.example.vista.androidapplication.files_folders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;
import com.example.vista.androidapplication.shared.shared_file_list;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;

public class add_share extends AsyncTask<String, String,files_folders_properties > {
    public OystorApp OA = new OystorApp();

    Context mContext;
    files_folders_properties obj_files_folders_properties;
    Fragment fragment;

    public add_share(files_folders_properties objfiles_folders_properties, Context context) {
        this.mContext = context;
        this.obj_files_folders_properties = objfiles_folders_properties;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected files_folders_properties doInBackground(String... params) {

        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "shareFile");
        request.addProperty("accessKey", OystorApp.accessKey);

       // System.out.println("subject" + obj_files_folders_properties.getsubject());
       //System.out.println("message" + obj_files_folders_properties.getmessage());

        request.addProperty("subject", obj_files_folders_properties.getsubject());
        request.addProperty("message", obj_files_folders_properties.getmessage());

        if(obj_files_folders_properties.getisForward())
        {
            request.addProperty("canForward", "Y");
        }
        else
        {
            request.addProperty("canForward", "N");
        }

        request.addProperty("docsId", obj_files_folders_properties.getdocId());

        //System.out.println("getdocId" + obj_files_folders_properties.getdocId());

        request.addProperty("sharedToUsersId", obj_files_folders_properties.getselectedUserIds());
        request.addProperty("sharedToContactsId", obj_files_folders_properties.getselectedContactIds());
        request.addProperty("expiryDates", obj_files_folders_properties.getexpiryDates());
        request.addProperty("rights", obj_files_folders_properties.getrights());
        request.addProperty("is_password", obj_files_folders_properties.getis_password());
        request.addProperty("password", obj_files_folders_properties.getpassword());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);

        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");

            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();
            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
            {
                String values = (String) response.getProperty("returnValues").toString();
                System.out.println("Values result " + values);
                obj_files_folders_properties.setShareId(values);
                return obj_files_folders_properties;
            }
            else if(exitMode.equals("2"))
            {
                OA.alert(response.getProperty("returnMessage").toString(), "Error", mContext);
            }
            else if(exitMode.equals("1"))
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(mContext);
            }
            else
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(mContext);
            }
        }
        catch(ConnectException e)
        {
            OA.internetProblem(mContext);
        }
        catch(SocketException e)
        {
            OA.internetProblem(mContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(files_folders_properties result) {
        //super.onPostExecute(result);

        if(result.getShareId() != null) {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    fragment = new shared_file_list();

                    if (fragment != null) {
                        FragmentManager fragmentManager =  ((Activity) mContext).getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.add(R.id.frame_container, fragment);
                        ft.addToBackStack("fragBack");
                        ft.commit();
                    }
                }
            });

            alertDialog.setTitle("Attention");
            alertDialog.setMessage("Shared Successfully");
            alertDialog.show();
        }
    }
}

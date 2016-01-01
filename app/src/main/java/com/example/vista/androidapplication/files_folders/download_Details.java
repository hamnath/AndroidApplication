package com.example.vista.androidapplication.files_folders;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;
import com.example.vista.androidapplication.properties.shared_list_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class download_Details extends AsyncTask<String, String, files_folders_properties>
{

    OystorApp OA= new OystorApp();
    public String docId = "";
    Context mcontext ;

    public download_Details(Context context, String docId)
    {
        this.docId = docId;
        this.mcontext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected files_folders_properties doInBackground(String... params)
    {

        System.out.println("Entered on Do in Background");

        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getDownloadFileDetailsIphone");
        request.addProperty("accessKey", OystorApp.accessKey);
        request.addProperty("docId", docId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);

        files_folders_properties obj_files_folders_properties = new files_folders_properties();

        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");

            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();
            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
            {
                SoapObject values = (SoapObject) response.getProperty("returnValues");

                obj_files_folders_properties.sets3AccessKey(values.getProperty("accessKey").toString());
                obj_files_folders_properties.sets3SecretKey(values.getProperty("secretKey").toString());
                obj_files_folders_properties.setbucketName(values.getProperty("bktName").toString());
                obj_files_folders_properties.setfilename(values.getProperty("filename").toString() + "." + values.getProperty("extension").toString());
                obj_files_folders_properties.setfileLoc(values.getProperty("fileLoc").toString());
                obj_files_folders_properties.setPath(values.getProperty("path").toString());
                obj_files_folders_properties.setSize(values.getProperty("size").toString());

            }
            else if(exitMode.equals("2"))
            {
                OA.alert(response.getProperty("returnMessage").toString(), "Error", mcontext );
            }
            else if(exitMode.equals("1"))
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(mcontext);
            }
            else
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(mcontext);
            }
        }
        catch(ConnectException e)
        {
            OA.internetProblem(mcontext);
        }
        catch(SocketException e)
        {
            OA.internetProblem(mcontext);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return obj_files_folders_properties;
    }

    @Override
    protected void onPostExecute(files_folders_properties obj_files_folders_properties) {
        super.onPostExecute(obj_files_folders_properties);

        System.out.println("On Post Result" + obj_files_folders_properties);

    }
}

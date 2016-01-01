package com.example.vista.androidapplication.files_folders;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import java.net.ConnectException;
import java.net.SocketException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;

public class create_folder extends AsyncTask<String, String,String>  {
    private OystorApp OA = new OystorApp();
    String currentFolderId="0",folderboxText;
    public Context Context1;

    public create_folder(String folderboxText, Context context) {
        // TODO Auto-generated constructor stub

        this.folderboxText=folderboxText;
        Context1=context;
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "createDirectory");
        request.addProperty("accessKey", OystorApp.accessKey);
        request.addProperty("folderId", currentFolderId);
        request.addProperty("folderName", folderboxText);
        request.addProperty("folderDesc", "");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
        envelope.encodingStyle = "UTF-8";
        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");

            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();
            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
            {
                String values = (String) response.getProperty("returnValues").toString();
                System.out.println(values+"Values");
            }
            else if(exitMode.equals("2"))
            {
                OA.alert(response.getProperty("returnMessage").toString(),"Error",Context1 );
            }
            else if(exitMode.equals("1"))
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(Context1);
            }
            else
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(Context1);
            }
        }
        catch(ConnectException e)
        {
            OA.internetProblem(Context1);
        }
        catch(SocketException e)
        {
            OA.internetProblem(Context1);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return currentFolderId;
    }

    @Override
    public void onPostExecute(String currentFolderId){
        // execution of result of Long time consuming operation
        // In this example it is the return value from the web service
        //OA.loaderHide();
    }
}

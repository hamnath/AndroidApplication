package com.example.vista.androidapplication.activities;


import android.content.Context;
import android.os.AsyncTask;
import com.example.vista.androidapplication.common.OystorApp;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;

public class clear_activity_list extends AsyncTask<String, String,String> {

    private OystorApp OA = new OystorApp();
    String activityId = "";

    Context context;

    public clear_activity_list(Context context,String activityId) {
        this.context = context;
        this.activityId = activityId;
        System.out.println("Activity ID::::::::::: " +activityId);
    }

    @Override
    protected void onPreExecute() {
        OA.loaderShow(context);
    }


    String methodName = "";
    @Override
    protected String doInBackground(String... params) {

        if(activityId != "0")
            methodName = "deleteActivity";
        else
            methodName = "deleteAllActivity";

        System.out.println("Method name " + methodName);

        SoapObject request = new SoapObject(OystorApp.NAMESPACE, methodName);
        request.addProperty("accessKey", OystorApp.accessKey);

        if(activityId != "")
            request.addProperty("activityId", activityId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");

            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();
            System.out.println(returnCode +" Response "+ returnCode);
            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
            {
                String values = (String) response.getProperty("returnValues").toString();
                System.out.println("Values" + values);
                if(!values.equals("1"))
                {
                    //Problem Occured
                }
            }
            else if(exitMode.equals("2"))
            {
                 	OA.alert(response.getProperty("returnMessage").toString(), "Error", context);
            }
            else if(exitMode.equals("1"))
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                //	OA.logout(this);
            }
            else
            {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                //	OA.logout(this);
            }
        }
        catch(ConnectException e)
        {
             OA.internetProblem(context);
        }
        catch(SocketException e)
        {
              OA.internetProblem(context);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return activityId;
    }

    @Override
    protected void onPostExecute(String activityid) {
        System.out.println("responseMsg"+ activityid);
        OA.loaderHide();
    }
}

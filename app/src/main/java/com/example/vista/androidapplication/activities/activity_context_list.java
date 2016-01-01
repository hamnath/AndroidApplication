package com.example.vista.androidapplication.activities;

import android.os.AsyncTask;

import com.example.vista.androidapplication.common.OystorApp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;


public class activity_context_list extends AsyncTask<String, String,String> {


    private OystorApp OA = new OystorApp();
    String type,ItemId;
    public activity_context_list(String type, String itemId) {
        // TODO Auto-generated constructor stub
        System.out.println(type+"Type24");
        System.out.println(itemId+"ItemId24");
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub

        type=params[0];
        ItemId=params[1];

        System.out.println(type + "123Type");
        System.out.println(ItemId + "123ItemId");

//        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "updateConnectionRequest");
//        request.addProperty("accessKey", OystorApp.accessKey);
//        request.addProperty("type", type);
//        request.addProperty("requestId", ItemId);
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        //    envelope.dotNet=true;
//        envelope.setOutputSoapObject(request);
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
//        try {
//            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
//            SoapObject responses = (SoapObject) envelope.bodyIn;
//            SoapObject response = (SoapObject) responses.getProperty("response");
//            String returnCode = response.getProperty("returnCode").toString();
//            String exitMode = response.getProperty("exitMode").toString();
//            System.out.println(returnCode+"returnCode");
//            System.out.println(exitMode+"exitMode");
//            System.out.println(exitMode+"exitMode");
//
//            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
//            {
//                String values = (String) response.getProperty("returnValues").toString();
//                System.out.println(values+"exitMode");
//                if(!values.equals("1"))
//                {
//
//                }
//            }
//            else if(exitMode.equals("2"))
//            {
//                //		OA.alert(response.getProperty("returnMessage").toString(), "Error", this);
//            }
//            else if(exitMode.equals("1"))
//            {
//                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
//
//            }
//            else
//            {
//                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
//
//            }
//        }
//        catch(ConnectException e)
//        {
//            //	OA.internetProblem(this);
//        }
//        catch(SocketException e)
//        {
//            //	OA.internetProblem(this);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        return ItemId;
    }

    @Override
    public void onPostExecute(String ItemId){
        // execution of result of Long time consuming operation
        // In this example it is the return value from the web service

        System.out.println("responseMsg");
        System.out.println("On post result" + ItemId);
        //   OA.loaderHide(getActivity());
    }

}

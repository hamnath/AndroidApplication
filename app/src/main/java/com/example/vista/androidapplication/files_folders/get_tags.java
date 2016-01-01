package com.example.vista.androidapplication.files_folders;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vista.androidapplication.common.OystorApp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;

public class get_tags  extends AsyncTask<String, String, String> {

    String docId;
    OystorApp OA = new OystorApp();
    Context mcontext;

    public get_tags(String docId, Context context) {
        this.docId = docId;
        this.mcontext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getDocumentTags");
        request.addProperty("accessKey", OystorApp.accessKey);
        request.addProperty("docId", docId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
        String name = " - ";

        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");
            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();
            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
            {
                SoapObject values = (SoapObject) response.getProperty("returnValues");

                int totalCount = values.getPropertyCount();
                name = "";
                for (int detailCount = 0; detailCount < totalCount; detailCount++) {
                    if(detailCount != totalCount-1)
                    {
                        SoapObject docVals = (SoapObject) values.getProperty(detailCount);
                        if(detailCount > 0)
                        {
                            name += " , ";
                        }

                        if(!docVals.getProperty("tag").toString().equals("anyType{}"))
                        {
                            name +=  docVals.getProperty("tag").toString();
                        }
                    }
                }
            }
            else if(exitMode.equals("2"))
            {
                OA.alert(response.getProperty("returnMessage").toString(), "Error", mcontext);
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
        return name;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        System.out.println("result");
    }
}

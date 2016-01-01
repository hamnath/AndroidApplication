package com.example.vista.androidapplication.files_folders;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;


public class add_tags extends AsyncTask<String, String, String> {

    String docId, tags;
    Context mcontext;
    OystorApp OA = new OystorApp();

    public add_tags(String docId, String tags, Context context) {
        this.docId = docId;
        this.tags = tags;
        this.mcontext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        OA.loaderShow(mcontext);
    }

    @Override
    protected String doInBackground(String... params) {

        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "addDocumentTags");
        request.addProperty("accessKey", OystorApp.accessKey);
        request.addProperty("docId", docId);
        request.addProperty("tags", tags);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
        envelope.encodingStyle = "UTF-8";
        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");
            String exitMode = response.getProperty("exitMode").toString();

            String returnCode = response.getProperty("returnCode").toString();

            if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                String values = (String) response.getProperty("returnValues").toString();
            } else if (exitMode.equals("2")) {
                OA.alert(response.getProperty("returnMessage").toString(), "Error", mcontext);
            } else if (exitMode.equals("1")) {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(mcontext);
            } else {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                OA.logout(mcontext);
            }
        } catch (ConnectException e) {
            OA.internetProblem(mcontext);
        } catch (SocketException e) {
            OA.internetProblem(mcontext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        OA.loaderHide();
    }

}





package com.example.vista.androidapplication.contacts;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.contact_list_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;

public class AsyncTaskaddContact extends AsyncTask<contact_list_properties,String ,String > {
    contact_list_properties obj_contact_property;
    public Context context;
    public OystorApp OA = new OystorApp();

    public AsyncTaskaddContact(Context context, contact_list_properties obj_contact_property)
    {
        this.context= context;
        this.obj_contact_property = obj_contact_property;
    }


    protected void onPreExecute() {
        //    OA.loaderShow(getActivity());
    }

    String responseMsg = "";
    @Override
    protected String doInBackground(contact_list_properties... params) {

        obj_contact_property = (contact_list_properties) params[0];
        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "insertContact");

        request.addProperty("accessKey", OystorApp.accessKey);
        request.addProperty("emailId", obj_contact_property.getemail());
        request.addProperty("contactName", obj_contact_property.getname());
        request.addProperty("contactNumber", obj_contact_property.getcontact_number());
        request.addProperty("company", obj_contact_property.getcompany());
        request.addProperty("designation", obj_contact_property.getdesignation());
        request.addProperty("secEmail", obj_contact_property.getsecondary_email());
        request.addProperty("skypeName", obj_contact_property.getskype_name());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");

            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();

            if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                String values = (String) response.getProperty("returnValues").toString();
                responseMsg =values;
                System.out.println("Contact Added Successfully" + values);
            } else if (exitMode.equals("2")) {
               // OA.alert(response.getProperty("returnMessage").toString(), "Error", context);
                responseMsg = response.getProperty("returnMessage").toString();
            } else if (exitMode.equals("1")) {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                //	OA.logout(c.getActivity());
            } else {
                OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                //OA.logout(context);
            }
        } catch (ConnectException e) {
            OA.internetProblem(context);
        } catch (SocketException e) {
            OA.internetProblem(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseMsg;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub

        System.out.println(result);
        if(result != "")
        {
            OA.alert("Contact Added Successfully "," Contact ",context);
        }

    }


}
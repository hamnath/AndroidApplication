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
import java.util.ArrayList;

public class get_contacts  extends AsyncTask<String, String, ArrayList<String[]>>
{
    String searchKey ;
    Context mContext;
    protected String[] availableUserIds = {""};
    protected String[] availableContactIds = {""};
    ArrayList<String[]> arr_contacts = new ArrayList<String[]>();
    String[] res = new String[0];

    public OystorApp OA = new OystorApp();

    public get_contacts(Context context, String Search) {
        this.mContext = context;
        this.searchKey = Search;
    }

    @Override
    protected void onPreExecute() {

        System.out.println("Entered get Contacts");
        super.onPreExecute();
    }

    @Override
    protected ArrayList<String[]> doInBackground(String... params)
    {
        SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getContactSearch");
        request.addProperty("accessKey", OystorApp.accessKey);
        request.addProperty("search", searchKey);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
        String[] res = new String[0];
        try {
            androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
            SoapObject responses = (SoapObject) envelope.bodyIn;
            SoapObject response = (SoapObject) responses.getProperty("response");

            String returnCode = response.getProperty("returnCode").toString();
            String exitMode = response.getProperty("exitMode").toString();
            if(response.getPropertyCount() > 0 && returnCode.equals("1"))
            {
                SoapObject values = (SoapObject) response.getProperty("returnValues");

                String email,name,contact_id,contact_user_id,emailVisible;

                int totalCount = values.getPropertyCount();
                String[] result = new String[totalCount];
                String[]  newavailableContactIds = new String[totalCount];
                String[]  newavailableUserIds = new String[totalCount];

                for (int detailCount = 0; detailCount < totalCount; detailCount++) {

                    SoapObject docVals = (SoapObject) values.getProperty(detailCount);
                    emailVisible = docVals.getProperty("is_email_visible").toString();
                    if(emailVisible.equals("Y"))
                    {
                        email = " (" + docVals.getProperty("email").toString() + ")";
                    }
                    else if(emailVisible.equals("N"))
                    {
                        email = " ";
                    }
                    else if(emailVisible.equals("anyType{}"))
                    {
                        email = " (" + docVals.getProperty("email").toString() + ")";
                    }
                    else
                    {
//			        	email = " (Email is Hidden)";
                        email = " ";
                    }

                    name=  docVals.getProperty("name").toString();
                    if(name.equals("anyType{}"))
                    {
                        name = "";
                    }
                    contact_id=  docVals.getProperty("contact_id").toString();
                    contact_user_id=  docVals.getProperty("contact_user_id").toString();
                    result[detailCount] = name + email;

                    System.out.println("name + Email" + name + " dddd " + email);

                    newavailableUserIds[detailCount] = contact_user_id;
                    newavailableContactIds[detailCount] = contact_id;
                }
                availableContactIds = newavailableContactIds;
                availableUserIds = newavailableUserIds;

                arr_contacts.add(result);
                arr_contacts.add(availableContactIds);
                arr_contacts.add(availableUserIds);

                return arr_contacts;
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
    protected void onPostExecute(ArrayList<String[]> s) {
        super.onPostExecute(s);
    }
}

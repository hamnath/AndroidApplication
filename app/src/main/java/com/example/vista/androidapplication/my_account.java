package com.example.vista.androidapplication;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vista.androidapplication.common.OystorApp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.HashMap;

public class my_account extends Fragment {

    OystorApp OA = new OystorApp();
    TextView emailView;
    TextView planView;
    TextView usageView;
    TextView appversionView;
    TextView nameView;
    TextView helpmeonoystorView;
    TextView sharewithfriendsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.myaccount, container, false);

        emailView = (TextView)rootView.findViewById(R.id.email);
        planView = (TextView)rootView.findViewById(R.id.plan);
        usageView = (TextView)rootView.findViewById(R.id.usage);
        appversionView = (TextView)rootView.findViewById(R.id.appversion);
        nameView = (TextView)rootView.findViewById(R.id.name);
        helpmeonoystorView = (TextView)rootView.findViewById(R.id.helpmeonoystor);
        sharewithfriendsView   = (TextView)rootView.findViewById(R.id.sharewithfriends);

        if(OA.loginAuthentication()) {
            String accesskey = OystorApp.accessKey;

            System.out.println("Login Authentication" + accesskey);

            AsyncTaskRunner runner = new AsyncTaskRunner(accesskey);
            runner.execute(accesskey);
        }

        return rootView;
    }

    public class AsyncTaskRunner extends AsyncTask <String, String, HashMap<String,String>> {

        public AsyncTaskRunner(String accesskey) {
            // TODO Auto-generated constructor stub
            System.out.println("accesskey" + accesskey);
        }

        protected void onPreExecute() {
            OA.loaderShow(getActivity());
        }

        @Override
        public HashMap<String,String> doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("params" + params[0]);

            String accessKey = params[0];
            Object bitmap;

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getPlanVersionDetails");
            System.out.println("getPlanVersionDetails params" + accessKey);
            request.addProperty("accessKey", OystorApp.accessKey);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
            HashMap<String,String> res = new HashMap<String,String>();

            try {
                androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
                SoapObject responses = (SoapObject) envelope.bodyIn;
                SoapObject response = (SoapObject) responses.getProperty("response");
                String returnCode = response.getProperty("returnCode").toString();
                String exitMode = response.getProperty("exitMode").toString();
                System.out.println("response spaceUsed" +response);
                if(response.getPropertyCount() > 0 && returnCode.equals("1"))
                {
                    SoapObject values = (SoapObject) response.getProperty("returnValues");

                    System.out.println("response spaceUsed" +values.getProperty("spaceUsed"));

                    res.put("email", values.getProperty("email").toString());
                    res.put("planName", values.getProperty("planName").toString());
                    res.put("totalSpace", values.getProperty("totalSpace").toString());
                    res.put("spaceUsed", values.getProperty("spaceUsed").toString());
                    res.put("version", values.getProperty("version").toString());
                    res.put("versionName", values.getProperty("versionName").toString());
                    res.put("releasedDate", values.getProperty("releasedDate").toString());
                    res.put("userName", values.getProperty("name").toString());

                    return res;
                }
                else if(exitMode.equals("2"))
                {
                    //OA.alert(response.getProperty("returnMessage").toString(), "Error", this);
                }
                else if(exitMode.equals("1"))
                {
                    OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                    OA.logout(getActivity());
                }
                else
                {
                    OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                    OA.logout(getActivity());
                }
            }   catch(ConnectException e)
            {
                OA.internetProblem(getActivity());
            }
            catch(SocketException e)
            {
                OA.internetProblem(getActivity());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return res;

        }

        /*passing access key to on postexecute*/
        public void onPostExecute(final HashMap<String,String> details) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service

            System.out.println("Entered");
            System.out.println("On post result" + details.size());

            emailView.setText(details.get("email"));
            final Object emailId = emailView;

            planView.setText(details.get("planName"));

            Double total = Double.parseDouble(details.get("totalSpace"));

            Double space_used = 0.0;
            space_used = Double.parseDouble(details.get("spaceUsed"));

            try {
                usageView.setText("Used " + OA.sizeRound(space_used) + " of " + OA.sizeRoundInputMB(total));
            } catch (Exception e) {
                e.printStackTrace();
            }

            appversionView.setText("Version" + " " + details.get("version") + "");
            nameView.setText(details.get("userName") + "");


            sharewithfriendsView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    OA.sendEmail(getActivity(), new String[]{}, "Sending Email", "Join me at Oystor!", "I've been using Oystor and thought you might like to try it out. Here's an invitation to create a FREE Oystor account. To accept this invitation Goto https://www.oystor.com/oystor/register_page.\n\n \tOystor is an easy cloud application for you to scan, store, search and share documents securely. With Oystor, working on the go will never be the same again.");
                }
            });

            helpmeonoystorView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    OA.sendEmail(getActivity(), new String[]{"support@oystor.com"}, "Sending Email", "Help me on Oystor", "I need help with Oystor. \n\nExplain your problem here. \n\nYour Oystor Account - " + details.get("email"));
                }
            });
            OA.loaderHide();
        }
    }
 }

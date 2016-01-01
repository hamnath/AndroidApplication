package com.example.vista.androidapplication.shared;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.adapter.SharedListAdapter;
import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.shared_list_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class shared_file_list extends Fragment {

    public OystorApp OA = new OystorApp();
    SharedListAdapter obj_shared_list_adapter;

    ArrayList<shared_list_properties> arr_shared_by_me;
    ArrayList<shared_list_properties> arr_shared_with_me;

    ExpandableListView obj_exp_list_view;
    List<String> shared_list_header;
    HashMap<String, List<shared_list_properties>> shared_list_child;
    ArrayList<shared_list_properties> shared_obj_byme;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shared_list_header = new ArrayList<String>();
        shared_list_child = new HashMap<String, List<shared_list_properties>>();

        View rootView = inflater.inflate(R.layout.main_shared_file_list, container, false);
        obj_exp_list_view = (ExpandableListView) rootView.findViewById(R.id.SharedFileList);

        // preparing list data
        if (OA.loginAuthentication()) {
            String accesskey = OystorApp.accessKey;
            System.out.println("Login  Authentication" + accesskey);


            //Code for getting Shared by me details
            arr_shared_by_me = new ArrayList<shared_list_properties>();

            try {
                AsyncTaskRunner1 runner1 = new AsyncTaskRunner1(accesskey);
                arr_shared_by_me = runner1.execute(accesskey).get();
                System.out.println("arr_shared_with_me   " + arr_shared_by_me.size());
            }
            catch (Exception ex)
            {
                System.out.println("Error " +ex);
            }

            // Adding child data
            shared_list_header.add("Shared by me");
            shared_list_child.put(shared_list_header.get(0), arr_shared_by_me);
            obj_shared_list_adapter = new SharedListAdapter(getActivity(), shared_list_header, shared_list_child);

            if(obj_shared_list_adapter != null) {
                // setting list adapter
                obj_exp_list_view.setAdapter(obj_shared_list_adapter);
            }

//            obj_exp_list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//                {
//                    System.out.println("Clicked Test ;;;;;;;;;;;;;;;;;;");
//                    return true;
//                }
//            });


            // Code for getting Shared with me file details
            arr_shared_with_me = new ArrayList<shared_list_properties>();

            try {
                AsyncTaskRunner2 runner2 = new AsyncTaskRunner2(accesskey);
                arr_shared_with_me = runner2.execute(accesskey).get();
                System.out.println("arr_shared_with_me   " + arr_shared_by_me.size());
            }
            catch (Exception ex)
            {
                System.out.println("Error " +ex);
            }

            shared_list_header.add("Shared with me");
            shared_list_child.put(shared_list_header.get(1), arr_shared_with_me);
            obj_shared_list_adapter = new SharedListAdapter(getActivity(), shared_list_header, shared_list_child);

            // setting list adapter
            obj_exp_list_view.setAdapter(obj_shared_list_adapter);

            OA.loaderHide();
        }

        return rootView;
    }

    public class AsyncTaskRunner1 extends AsyncTask<String, String, ArrayList<shared_list_properties>> {

        public AsyncTaskRunner1(String accesskey) {
            System.out.println(accesskey + " AsyncTaskRunner1 acceskey");
        }

        protected void onPreExecute()
        {
             OA.loaderShow(getActivity());
        }

        @Override
        protected ArrayList<shared_list_properties> doInBackground(
                String... paramVarArgs) {
            // TODO Auto-generated method stub
            System.out.println("soap");
            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "filesSharedByMe");
            request.addProperty("accessKey", OystorApp.accessKey);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //  envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
            try {
                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                SoapObject responses = (SoapObject) envelope.bodyIn;
                SoapObject response = (SoapObject) responses.getProperty("response");

                String returnCode = response.getProperty("returnCode").toString();
                String exitMode = response.getProperty("exitMode").toString();
                if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                    System.out.println("entered");
                    SoapObject values = (SoapObject) response.getProperty("returnValues");

                    String share_id, subject, parent_id, shared_on, total_files, userImage, user_count, shared_user_name;

                    int totalCount = values.getPropertyCount();
                    final String[] user_images = new String[totalCount];
                    int i = 0;
                    for (int detailCount = 0; detailCount < totalCount; detailCount++) {

                        SoapObject docVals = (SoapObject) values.getProperty(detailCount);

                        share_id = docVals.getProperty("share_id").toString();
                        subject = docVals.getProperty("subject").toString();
                        parent_id = docVals.getProperty("parent_id").toString();
                        shared_on = docVals.getProperty("shared_on").toString();
                        total_files = docVals.getProperty("total_files").toString();
                        userImage = docVals.getProperty("user_image").toString();
                        user_count = docVals.getProperty("usersCount").toString();
                        //shared_user_name = docVals.getProperty("shared_user_name").toString();


                        shared_list_properties obj_shared_list_properties = new shared_list_properties();

                        obj_shared_list_properties.setShareId(share_id);
                        obj_shared_list_properties.setSubject(subject);
                        obj_shared_list_properties.setParentId(parent_id);
                        obj_shared_list_properties.setSharedOn(shared_on);
                        obj_shared_list_properties.setTotalFiles(total_files);
                        //obj_shared_list_properties.setSharedUserName(shared_user_name);
                        obj_shared_list_properties.setUserImage(userImage);
                        obj_shared_list_properties.setUsersCount(user_count);

                        System.out.println("share_id" + share_id);
                        System.out.println("subject" + subject);
                        System.out.println("parent_id" + parent_id);
                        System.out.println("shared_on" + shared_on);
                        System.out.println("total_files" + total_files);
                        //System.out.println("shared_user_name" + shared_user_name);
                        System.out.println("user_image" + userImage);
                        System.out.println("user_count" + user_count);

                        obj_shared_list_properties.setTotalFiles(total_files);

                        arr_shared_by_me.add(obj_shared_list_properties);
                    }

                    return arr_shared_by_me;

                } else {
                    OA.attentionBox(response.getProperty("returnMessage").toString(), "Alert", getActivity());
                }

            } catch (ConnectException e) {
                if (OA.isInternetAvailable(getActivity())) {
                     OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());
                } else {
                    //  OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (SocketException e) {
                if (OA.isInternetAvailable(getActivity())) {
                      OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());

                } else {
                      OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (Exception e) {
                 //Toast.makeText(e.getMessage(),1000);
                System.out.println(e.getMessage() + "msg3");

                e.printStackTrace();
            }
            System.out.println("arr_shared_by_me" + arr_shared_by_me);
            return null;

        }

        public void onPostExecute(ArrayList<shared_list_properties> obj_shared_list) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service
            System.out.println("Entered");
            System.out.println("On post result" + obj_shared_list.size());

            // Adding child data
//            shared_list_header.add("Shared by me");
//            shared_list_child.put(shared_list_header.get(0), obj_shared_list);
//
//            obj_shared_list_adapter = new SharedListAdapter(getActivity(), shared_list_header, shared_list_child);
//
////            // setting list adapter
////            if (obj_shared_list.size() != 0) {
////                obj_exp_list_view = (ExpandableListView) getActivity().findViewById(R.id.SharedFileList);
////                obj_exp_list_view.setAdapter(obj_shared_list_adapter);
////            }
////            else {
////                obj_exp_list_view = (ExpandableListView) getActivity().findViewById(R.id.SharedFileList);
////                obj_exp_list_view.setAdapter(obj_shared_list_adapter);
////
////            }
//
//            // setting list adapter
//            obj_exp_list_view = (ExpandableListView)getActivity().findViewById(R.id.SharedFileList);
//            obj_exp_list_view.setAdapter(obj_shared_list_adapter);
//
//            obj_exp_list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//                {
//                    System.out.println("Clicked Test ;;;;;;;;;;;;;;;;;;");
//                    return true;
//                }
//            });
        }
    }

    public class AsyncTaskRunner2 extends AsyncTask<String, String, ArrayList<shared_list_properties>> {

        public AsyncTaskRunner2(String accesskey) {
            // TODO Auto-generated constructor stub
            System.out.println(accesskey + " AsyncTaskRunner2 acceskey");
        }

        @Override
        protected ArrayList<shared_list_properties> doInBackground(
                String... paramVarArgs) {
            // TODO Auto-generated method stub
            System.out.println("soap");
            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "filesSharedWithMe");
            request.addProperty("accessKey", OystorApp.accessKey);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //  envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
            try {
                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                SoapObject responses = (SoapObject) envelope.bodyIn;
                SoapObject response = (SoapObject) responses.getProperty("response");

                String returnCode = response.getProperty("returnCode").toString();
                String exitMode = response.getProperty("exitMode").toString();
                if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                    System.out.println("entered");
                    SoapObject values = (SoapObject) response.getProperty("returnValues");

                    String share_id, subject, parent_id, shared_on, total_files, userImage, user_count, shared_user_name;

                    int totalCount = values.getPropertyCount();
                    final String[] user_images = new String[totalCount];
                    int i = 0;
                    for (int detailCount = 0; detailCount < totalCount; detailCount++) {

                        SoapObject docVals = (SoapObject) values.getProperty(detailCount);

                        share_id = docVals.getProperty("share_id").toString();
                        subject = docVals.getProperty("subject").toString();
                        parent_id = docVals.getProperty("parent_id").toString();
                        shared_on = docVals.getProperty("shared_on").toString();
                        total_files = docVals.getProperty("total_files").toString();
                        userImage = docVals.getProperty("user_image").toString();
                        //user_count = docVals.getProperty("usersCount").toString();
                        shared_user_name = docVals.getProperty("shared_user_name").toString();


                        shared_list_properties obj_shared_list_properties = new shared_list_properties();

                        obj_shared_list_properties.setShareId(share_id);
                        obj_shared_list_properties.setSubject(subject);
                        obj_shared_list_properties.setParentId(parent_id);
                        obj_shared_list_properties.setSharedOn(shared_on);
                        obj_shared_list_properties.setTotalFiles(total_files);
                        obj_shared_list_properties.setSharedUserName(shared_user_name);
                        obj_shared_list_properties.setUserImage(userImage);
                        //obj_shared_list_properties.setUsersCount(user_count);

                        System.out.println("share_id" + share_id);
                        System.out.println("subject" + subject);
                        System.out.println("parent_id" + parent_id);
                        System.out.println("shared_on" + shared_on);
                        System.out.println("total_files" + total_files);
                        System.out.println("shared_user_name" + shared_user_name);
                        System.out.println("user_image" + userImage);
                        //System.out.println("user_count" + user_count);

                        obj_shared_list_properties.setTotalFiles(total_files);

                        arr_shared_with_me.add(obj_shared_list_properties);
                    }

                } else {
                    OA.attentionBox(response.getProperty("returnMessage").toString(), "Alert", getActivity());
                }

            } catch (ConnectException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());
                } else {
                     OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (SocketException e) {
                if (OA.isInternetAvailable(getActivity())) {
                   OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());

                } else {
                     OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (Exception e) {
                //Toast.makeText(e.getMessage(),1000);
                System.out.println(e.getMessage() + "msg3");

                e.printStackTrace();
            }
            System.out.println("arr_shared_with_me" + arr_shared_with_me);
            return arr_shared_with_me;

        }

        public void onPostExecute(ArrayList<shared_list_properties> obj_shared_list) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service
            System.out.println("Entered");
            System.out.println("On post result" + obj_shared_list.size());

//            shared_list_header.add("Shared with me");
//            shared_list_child.put(shared_list_header.get(1), obj_shared_list);
//
//            obj_shared_list_adapter = new SharedListAdapter(getActivity(), shared_list_header, shared_list_child);
//            System.out.println("On post result obj_shared_list ");
//
//            // setting list adapter
//            obj_exp_list_view = (ExpandableListView)getActivity().findViewById(R.id.SharedFileList);
//            obj_exp_list_view.setAdapter(obj_shared_list_adapter);
//
//            OA.loaderHide();
        }
    }
}



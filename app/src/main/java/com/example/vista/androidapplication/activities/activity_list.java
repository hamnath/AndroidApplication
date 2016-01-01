package com.example.vista.androidapplication.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.amazonaws.javax.xml.stream.xerces.util.ShadowedSymbolTable;
import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.adapter.ActivityListAdapter;
import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.contacts.addContact;
import com.example.vista.androidapplication.contacts.editContact;
import com.example.vista.androidapplication.properties.activity_list_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class activity_list extends ListFragment {

    private OystorApp OA = new OystorApp();
    ArrayList<activity_list_properties> arrList_activityList;
    ActivityListAdapter obj_activity_adapter;
    String accesskey = "";
    String responseMsg = "";
    activity_list_properties obj_activity_property;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        arrList_activityList = new ArrayList<activity_list_properties>();
        System.out.println("arrList_activityList" + arrList_activityList.size());

        if (OA.loginAuthentication()) {
            accesskey = OystorApp.accessKey;
            System.out.println("activity_list Login Authentication" + accesskey);
            AsyncTaskRunner runner = new AsyncTaskRunner(accesskey);
            runner.execute(accesskey);
        }
        View rootView = inflater.inflate(R.layout.main_activity_list, container, false);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Activities");
        MenuInflater inflaters = getActivity().getMenuInflater();
        inflaters.inflate(R.menu.activity_menu, menu);
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        super.onContextItemSelected(item);
//        System.out.println("activity Item Context Menu");
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        ImageView emptyView;
//        switch (item.getItemId()) {
//
//            case R.id.activity_item_1:
//                System.out.println("activity Item Get ID" + OystorApp.obj_activity_list.getId());
//                int listPosition = info.position;
//
//                System.out.println("Size position List " + OystorApp.obj_activity_array_List.size());
//
//                if (OystorApp.obj_activity_array_List.size() != 0) {
//
//                    System.out.println("Size Array List " + OystorApp.obj_activity_array_List.size());
//                    obj_activity_property = OystorApp.obj_activity_array_List.get(listPosition);
//                }
//
//                if (OystorApp.obj_activity_adapter.getCount() > 0) {
//                    System.out.println("obj_activity_adapter entered 111" + OystorApp.obj_activity_adapter.getCount());
//                    clear_activity_list obj_clear_activity_list = new clear_activity_list(getActivity(), OystorApp.obj_activity_list.getId());
//                    obj_clear_activity_list.execute(OystorApp.obj_activity_list.getId());
//
//                    OystorApp.obj_activity_adapter.remove(obj_activity_property);
//                    System.out.println("obj_activity_adapter Size " + OystorApp.obj_activity_adapter.getCount());
//
//                    if (OystorApp.obj_activity_adapter.getCount() != 0)
//                        setListAdapter(OystorApp.obj_activity_adapter);
//                    else {
//                        emptyView = (ImageView) getListView().getEmptyView();
//                        emptyView.setImageResource(R.drawable.noactivityfound);
//                    }
//                }
//
//                return true;
//
//            case R.id.activity_item_2:
//
//                clear_activity_list obj_clear_activity_list = new clear_activity_list(getActivity(), "0");
//                obj_clear_activity_list.execute("0");
//                obj_activity_adapter.clear();
//
//                setListAdapter(obj_activity_adapter);
//                emptyView = (ImageView) getListView().getEmptyView();
//                emptyView.setImageResource(R.drawable.noactivityfound);
//
//                return true;
//
//            case R.id.contact_item_1:
//                fragment = new addContact();
//
//                if (fragment != null) {
//
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction ft = fragmentManager.beginTransaction();
//                    ft.add(R.id.frame_container, fragment);
//                    ft.addToBackStack("fragBack");
//                    ft.commit();
//                }
//
//                return true;
//
//            case R.id.contact_item_2:
//                fragment = new editContact();
//                if (fragment != null)
//                {
//                    FragmentManager fragmentManager =  getFragmentManager();
//                    FragmentTransaction ft = fragmentManager.beginTransaction();
//                    ft.add(R.id.frame_container, fragment);
//                    ft.addToBackStack("fragBack");
//                    ft.commit();
//                }
//                return true;
//
//        }
//        return true;
//    }

    ImageView emptyView;
    public void clearActivity(int listPosition) {

        System.out.println("activity Item Get ID" + OystorApp.obj_activity_list.getId());
        //int listPosition = info.position;

        System.out.println("Size position List " + OystorApp.obj_activity_array_List.size());

        if (OystorApp.obj_activity_array_List.size() != 0) {

            System.out.println("Size Array List " + OystorApp.obj_activity_array_List.size());
            obj_activity_property = OystorApp.obj_activity_array_List.get(listPosition);
        }

        if (OystorApp.obj_activity_adapter.getCount() > 0) {
            System.out.println("obj_activity_adapter entered 111" + OystorApp.obj_activity_adapter.getCount());
            clear_activity_list obj_clear_activity_list = new clear_activity_list(this.getActivity(), OystorApp.obj_activity_list.getId());
            obj_clear_activity_list.execute(OystorApp.obj_activity_list.getId());

            OystorApp.obj_activity_adapter.remove(obj_activity_property);
            System.out.println("obj_activity_adapter Size " + OystorApp.obj_activity_adapter.getCount());

            if (OystorApp.obj_activity_adapter.getCount() != 0)
                setListAdapter(OystorApp.obj_activity_adapter);
            else {
                emptyView = (ImageView) getListView().getEmptyView();
                emptyView.setImageResource(R.drawable.noactivityfound);
            }
        }
    }

    public void clearAllActivity()
    {
        clear_activity_list obj_clear_activity_list = new clear_activity_list(this.getActivity(), "0");
        obj_clear_activity_list.execute("0");
        obj_activity_adapter.clear();

        emptyView = (ImageView) getListView().getEmptyView();
        emptyView.setImageResource(R.drawable.noactivityfound);

    }

//

//    public void removeActivity(activity_list_properties obj_activity_property)
//    {
//        // System.out.println("removeActivity" + listPosition);
//        // System.out.println("removeActivity" + obj_activity_list.getTitle() + "dadadf " + obj_activity_list.getId() );
//
//        if(obj_activity_adapter != null)
//            obj_activity_adapter.remove(obj_activity_property);
//
//        setListAdapter(OystorApp.obj_activity_adapter);
//        OystorApp.obj_activity_adapter.remove(obj_activity_property);
//
//        //obj_activity_adapter.clear();
//
//        // obj_activity_property= arrList_activityList.get(listPosition);
//        //obj_activity_adapter.remove(obj_activity_property);
//
//        //ImageView empty = (ImageView) getListView().getEmptyView();
//        //empty.setImageResource(R.drawable.noactivityfound);
//    }

    public class AsyncTaskRunner extends AsyncTask<String, String, ArrayList<activity_list_properties>>{

        public AsyncTaskRunner(String accesskey) {
            // TODO Auto-generated constructor stub
        }

        protected void onPreExecute() {
            OA.loaderShow(getActivity());
        }

        @Override
        public ArrayList<activity_list_properties> doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("params" + params[0]);
            String accessKey = params[0];
            Object bitmap;

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getActivitiesList");
            request.addProperty("accessKey", accessKey);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //	envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
            String total = "0";
            try {
                System.out.println(OystorApp.URL + "url");

                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                SoapObject responseInner = (SoapObject) envelope.bodyIn;

                SoapObject response = (SoapObject) responseInner.getProperty("response");
                String returnCode = response.getProperty("returnCode").toString();

                if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                    SoapObject values = (SoapObject) response.getProperty("returnValues");

                    System.out.print("Return Response" + values.toString());

                    String activity_id,user_image,display_title,activityDate,activity_type_id,item_id;
                    int totalCount = values.getPropertyCount();

                    System.out.print("Return values 123" + totalCount);

                    final String[] user_images = new String[totalCount];
                    int i = 0;

                    if(totalCount !=0) {
                        for (int detailCount = 0; detailCount < totalCount; detailCount++) {
                            SoapObject docVals = (SoapObject) values.getProperty(detailCount);

                            activity_id = docVals.getProperty("activity_id").toString();
                            user_image = docVals.getProperty("user_image").toString();
                            display_title = docVals.getProperty("display_title").toString();
                            activityDate = docVals.getProperty("activityDate").toString();
                            activity_type_id = docVals.getProperty("activity_type_id").toString();
                            item_id = docVals.getProperty("item_id").toString();

                            obj_activity_property = new activity_list_properties();

                            System.out.println("********* obj_activity_property activity_id" + activity_id);
                            System.out.println("user_image" + user_image);
                            System.out.println("display_title" + display_title);
                            System.out.println("activityDate" + activityDate);
                            System.out.println("activity_type_id" + activity_type_id);
                            System.out.println("item_id" + item_id);

                            obj_activity_property.setId(activity_id);
                            obj_activity_property.setImage(user_image);
                            obj_activity_property.setTitle(display_title);
                            obj_activity_property.setDate(activityDate);
                            obj_activity_property.setActivityType(activity_type_id);
                            obj_activity_property.setItemId(item_id);

                            arrList_activityList.add(obj_activity_property);
                            user_images[i] = user_image;
                            i++;
                        }
                            OystorApp.obj_activity_array_List = arrList_activityList;
                    }
                    else {
                        System.out.println("Files Not Found");
                    }
                } else {
                   // OA.attentionBox(response.getProperty("returnMessage").toString(), "Alert", getActivity());
                    responseMsg = response.getProperty("returnMessage").toString();
                }
            } catch (ConnectException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    responseMsg = Constants.API_SERVER_DOWN;
                   // OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());
                } else {
                    responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                    //OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (SocketException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    responseMsg = Constants.API_SERVER_DOWN;
                   // OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());

                } else {
                    responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                  //  OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (Exception e) {
                //Toast.makeText(e.getMessage(),1000);
                System.out.println(e.getMessage() + "msg3");

                e.printStackTrace();
            }
            System.out.println(accessKey + "assume");
            return arrList_activityList;
        }

        /*passing access key to on postexecute*/
        public void onPostExecute(ArrayList<activity_list_properties> obj_act) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service
            OystorApp.obj_activity_array_List = obj_act;
            System.out.println("**** On post result entered ****" + obj_act.size());

            ListView list = (ListView) getListView();
            //unregisterForContextMenu(list);
            registerForContextMenu(list);
            //list.setOnCreateContextMenuListener(getActivity());

            if(obj_act.size() != 0 ) {
                obj_activity_adapter = new ActivityListAdapter(getActivity(), R.layout.activity_list, obj_act);
                setListAdapter(obj_activity_adapter);
                OystorApp.obj_activity_adapter = obj_activity_adapter;
            }
            else {
                    if(responseMsg != "") {
                        OA.attentionBox(responseMsg, "Alert", getActivity());
                    }else {
                        System.out.println("Entered activities obj_activity_adapter");
                        setListAdapter(obj_activity_adapter);

                        ImageView emptyView = (ImageView) getListView().getEmptyView();
                        emptyView.setImageResource(R.drawable.noactivityfound);
                    }
            }
            OA.loaderHide();
        }
    }


}
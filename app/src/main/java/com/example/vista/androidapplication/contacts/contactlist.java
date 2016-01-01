package com.example.vista.androidapplication.contacts;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.adapter.ContactListAdapter;
import com.example.vista.androidapplication.properties.contact_list_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class contactlist extends ListFragment  {
    private OystorApp OA = new OystorApp();
    ArrayList<contact_list_properties> arr_obj_activity_list = null;
    ContactListAdapter obj_contact_adapter;

    public final Map<String, SoftReference<Drawable>> mCache = new HashMap<String, SoftReference<Drawable>>();
    public final LinkedList<Drawable> mChacheController = new LinkedList<Drawable>();
    public static final int MAX_CACHE_SIZE = 80;
    String responseMsg = "";


    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //System.out.println("Entered Contact List onCreateView");
        arr_obj_activity_list = new ArrayList<contact_list_properties>();

        if (OA.loginAuthentication()) {
            String accesskey = OystorApp.accessKey;

            System.out.println("Login Authentication" + accesskey);

            AsyncTaskRunner runner = new AsyncTaskRunner(accesskey);
            runner.execute(accesskey);
        }

        View rootView = inflater.inflate(R.layout.main_contact_list, container, false);
        System.out.println("******* registerForContextMenu Entered ******** ");

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {

        //super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Contacts");
        //menu.setHeaderIcon(R.drawable.no_user_image);
        MenuInflater inflaters = getActivity().getMenuInflater();
        inflaters.inflate(R.menu.contact_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        //super.onContextItemSelected(item);
        System.out.println("Contact List Context Menu");

        switch (item.getItemId())
        {
            case R.id.contact_item_1:
                fragment = new addContact();

                if (fragment != null) {

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.add(R.id.frame_container, fragment);
                    ft.addToBackStack("fragBack");
                    ft.commit();
                }

                return true;

            case R.id.contact_item_2:
                fragment = new editContact();
                    if (fragment != null)
                    {
                        FragmentManager fragmentManager =  getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.add(R.id.frame_container, fragment);
                        ft.addToBackStack("fragBack");
                        ft.commit();
                    }
                return true;
        }
        return true;
    }

    // This class is calling web service API and set result to Listview.
    public class AsyncTaskRunner extends AsyncTask<String, String, ArrayList<contact_list_properties>> {

        public AsyncTaskRunner(String accesskey) {
            // TODO Auto-generated constructor stub
        }

        protected void onPreExecute() {
            OA.loaderShow(getActivity());
        }

        @Override
        public ArrayList<contact_list_properties> doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("params" + params[0]);

            String accessKey = params[0];
            Object bitmap;

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getContactsByAccessKey");
            request.addProperty("accessKey", OystorApp.accessKey);
            request.addProperty("searchString", "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
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

                    String contact_id, name, email, user_id, is_photo_visible, is_email_visible, group_name, user_image;
                    String secondary_email, company, designation, contact_number, skype_name;
                    int totalCount = values.getPropertyCount();

                    final String[] user_images = new String[totalCount];
                    int i = 0;

                    if (totalCount != 0) {
                        for (int detailCount = 0; detailCount < totalCount; detailCount++) {
                            SoapObject docVals = (SoapObject) values.getProperty(detailCount);

                            contact_id = docVals.getProperty("id").toString();
                            name = docVals.getProperty("name").toString();
                            email = docVals.getProperty("email").toString();
                            user_id = docVals.getProperty("user_id").toString();
                            is_photo_visible = docVals.getProperty("is_photo_visible").toString();
                            is_email_visible = docVals.getProperty("is_email_visible").toString();
                            group_name = docVals.getProperty("group_name").toString();
                            user_image = docVals.getProperty("user_image").toString();
                            secondary_email = docVals.getProperty("secondary_email").toString();
                            company = docVals.getProperty("company").toString();
                            designation = docVals.getProperty("designation").toString();
                            contact_number = docVals.getProperty("contact_number").toString();
                            skype_name = docVals.getProperty("skype_name").toString();

                            contact_list_properties obj_contact_property = new contact_list_properties();

                            System.out.println("contact_id  " + contact_id);
                            System.out.println("name" + name);
                            System.out.println("email" + email);
                            System.out.println("user_id  " + user_id);
                            System.out.println("group_name" + group_name);
                            System.out.println("user_image" + user_image);

                            obj_contact_property.setcontact_id(contact_id);
                            if (name.equals("anyType{}")) {
                                obj_contact_property.setname(" ");
                            } else {
                                obj_contact_property.setname(name);
                            }

                            if (email.equals("anyType{}")) {
                                obj_contact_property.setemail(" ");
                            } else {
                                obj_contact_property.setemail(email);
                            }

                            obj_contact_property.setuser_id(user_id);
                            obj_contact_property.setis_photo_visible(is_photo_visible);
                            obj_contact_property.setis_email_visible(is_email_visible);
                            obj_contact_property.setgroup_name(group_name);
                            obj_contact_property.setuser_image(user_image);
                            obj_contact_property.setsecondary_email(secondary_email);
                            obj_contact_property.setcompany(company);
                            obj_contact_property.setdesignation(designation);
                            obj_contact_property.setcontact_number(contact_number);
                            obj_contact_property.setskype_name(skype_name);

                            arr_obj_activity_list.add(obj_contact_property);

                            if (is_photo_visible.equals("Y")) {
                                user_images[i] = user_image;
                                i++;
                            }
                        }
                    } else {
                        System.out.println("no contact found");
                    }
                } else {
                    // OA.attentionBox(response.getProperty("returnMessage").toString(), "Alert", getActivity());
                    responseMsg = response.getProperty("returnMessage").toString();
                }

            } catch (ConnectException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    // OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());
                    responseMsg = Constants.API_SERVER_DOWN;
                } else {
                    // OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                    responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                }
            } catch (SocketException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    responseMsg = Constants.API_SERVER_DOWN;
                    // OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());

                } else {
                    //OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                    responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                }
            } catch (Exception e) {
                //Toast.makeText(e.getMessage(),1000);
                System.out.println(e.getMessage() + "msg3");

                e.printStackTrace();
            }
            System.out.println(accessKey + "assume");

           /* for(int i=0; i<m_orders.size();i++) {
                System.out.println( "Print ln "  + m_orders.get(i).getId().toString());
                System.out.println( "Print ln "  + m_orders.get(i).getTitle().toString());
            }   */

            return arr_obj_activity_list;

        }

        /*passing access key to on postexecute*/
        public void onPostExecute(ArrayList<contact_list_properties> obj_act) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service

            System.out.println("On post result" + obj_act.size());
            System.out.println("Entered On Contact List registerForContextMenu");

            ListView list = (ListView) getListView();
            registerForContextMenu(list);
            //list.setOnCreateContextMenuListener(getActivity());

            if (obj_act.size() != 0) {
                obj_contact_adapter = new ContactListAdapter(getActivity(), R.layout.contact_list, obj_act);
                setListAdapter(obj_contact_adapter);

            } else {
                if (responseMsg != "") {
                    OA.attentionBox(responseMsg, "Alert", getActivity());
                } else {
                    setListAdapter(obj_contact_adapter);

                    //System.out.println("Entered On Post else");

                    ImageView emptyView = (ImageView) getListView().getEmptyView();
                    emptyView.setImageResource(R.drawable.nocontactfound);
                }
            }
            OA.loaderHide();
        }

    }

}
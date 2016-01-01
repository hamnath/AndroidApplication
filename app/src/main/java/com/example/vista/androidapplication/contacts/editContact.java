package com.example.vista.androidapplication.contacts;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.common.downloadImageFromURL;
import com.example.vista.androidapplication.properties.contact_list_properties;

public class editContact extends Fragment {
    /** Called when the activity is first created. */
    public OystorApp OA = new OystorApp();

    Button back,update;
    TextView nameView,emailView,secEmailView,companyNameView,designationView,skypenameView,contactNumberView;
    ImageView imgView;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_edit, container, false);
        back = (Button)rootView.findViewById(R.id.cancel);
        update = (Button) rootView.findViewById(R.id.update);
        update.setText("Update");
        nameView = (TextView) rootView.findViewById(R.id.name);
        emailView = (TextView) rootView.findViewById(R.id.email);
        secEmailView = (TextView) rootView.findViewById(R.id.secEmail);
        companyNameView = (TextView) rootView.findViewById(R.id.companyName);
        designationView = (TextView) rootView.findViewById(R.id.designation);
        skypenameView = (TextView) rootView.findViewById(R.id.skypename);
        contactNumberView = (TextView) rootView.findViewById(R.id.contactNumber);
        imgView = (ImageView) rootView.findViewById(R.id.userImage);

        downloadImageFromURL objdown = new downloadImageFromURL(OystorApp.obj_edit_contacts_list.getuser_image(),imgView);
        objdown.execute(OystorApp.obj_edit_contacts_list.getuser_image());

        nameView.setText(OystorApp.obj_edit_contacts_list.getname());
        nameView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    emailView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        if(OystorApp.obj_edit_contacts_list.getis_email_visible().equals("Y"))
        {
            emailView.setText(OystorApp.obj_edit_contacts_list.getemail());
        }
        else if(OystorApp.obj_edit_contacts_list.getis_email_visible().equals("N"))
        {
            emailView.setText("Email is Hidden");
        }
        else if(OystorApp.obj_edit_contacts_list.getis_email_visible().equals("anyType{}"))
        {
            emailView.setText(OystorApp.obj_edit_contacts_list.getemail());
        }
        else
        {
            emailView.setText("");
        }  emailView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    companyNameView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        if(Integer.parseInt(OystorApp.obj_edit_contacts_list.getuser_id()) > 0)
        {
            emailView.setEnabled(false);
        }

        if(!OystorApp.obj_edit_contacts_list.getcompany().equals("anyType{}"))
        {
            companyNameView.setText(OystorApp.obj_edit_contacts_list.getcompany());
        }
        companyNameView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    designationView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        if(!OystorApp.obj_edit_contacts_list.getdesignation().equals("anyType{}"))
        {
            designationView.setText(OystorApp.obj_edit_contacts_list.getdesignation());
        }
        designationView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    secEmailView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        if(!OystorApp.obj_edit_contacts_list.getsecondary_email().equals("anyType{}"))
        {
            secEmailView.setText(OystorApp.obj_edit_contacts_list.getsecondary_email());
        }
        secEmailView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    contactNumberView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        if(!OystorApp.obj_edit_contacts_list.getcontact_number().equals("anyType{}"))
        {
            contactNumberView.setText(OystorApp.obj_edit_contacts_list.getcontact_number());
        }
        contactNumberView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    skypenameView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        if(!OystorApp.obj_edit_contacts_list.getskype_name().equals("anyType{}"))
        {
            skypenameView.setText(OystorApp.obj_edit_contacts_list.getskype_name());
        }
        skypenameView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //  validateContact(skypenameView);
                    return true;
                }
                return false;
            }
        });


        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Validate(skypenameView);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                fragment = new contactlist();
                FragmentManager fragmentManager =getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.frame_container, fragment);
                ft.addToBackStack("fragBack");
                ft.commit();
            }
        });

        return rootView;
    }


    //validation for contact //
    public void Validate(View v) {
        // TODO Auto-generated method stub

        if(nameView.getText().toString().equals("") || nameView.getText().toString().equals(" "))
        {
            OA.alert("Enter the Contact Name", "Error", getActivity());
        }
        else if(emailView.getText().toString().equals("") || emailView.getText().toString().equals(" "))
        {
            OA.alert("Enter the Email Address", "Error", getActivity());
        }
        else if(!OA.isValidEmail(emailView.getText().toString()))
        {
            OA.alert("Email is Not Valid", "Error", getActivity());
        }
        else {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(nameView.getWindowToken(), 0);
            //update.setText("Update");

            if (OA.loginAuthentication()) {
                String accesskey = OystorApp.accessKey;
                contact_list_properties obj_contact_property = new contact_list_properties();

                obj_contact_property.setcontact_id(OystorApp.obj_edit_contacts_list.getcontact_id());
                obj_contact_property.setuser_id(OystorApp.obj_edit_contacts_list.getuser_id());
                obj_contact_property.setname(nameView.getText().toString());
                obj_contact_property.setemail(emailView.getText().toString());
                obj_contact_property.setsecondary_email(secEmailView.getText().toString());
                obj_contact_property.setcompany(companyNameView.getText().toString());
                obj_contact_property.setdesignation(designationView.getText().toString());
                obj_contact_property.setskype_name(skypenameView.getText().toString());
                obj_contact_property.setcontact_number(contactNumberView.getText().toString());

                async_task_contact obj_asyncAddContact = new async_task_contact(getActivity(), obj_contact_property,"1");
                obj_asyncAddContact.execute(obj_contact_property);
            }
        }
    }
}
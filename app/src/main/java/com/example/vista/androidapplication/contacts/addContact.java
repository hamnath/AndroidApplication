package com.example.vista.androidapplication.contacts;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.contact_list_properties;

public class addContact extends Fragment {

    TextView nameView,emailView,secEmailView,companyNameView,designationView,skypenameView,contactNumberView;
    ImageView imgView;
    Button back,update;
    public OystorApp OA = new OystorApp();
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contact_edit, container, false);
        nameView = (TextView) rootView.findViewById(R.id.name);
        emailView = (TextView) rootView.findViewById(R.id.email);
        secEmailView = (TextView) rootView.findViewById(R.id.secEmail);
        companyNameView = (TextView) rootView.findViewById(R.id.companyName);
        designationView = (TextView) rootView.findViewById(R.id.designation);
        skypenameView = (TextView) rootView.findViewById(R.id.skypename);
        contactNumberView = (TextView) rootView.findViewById(R.id.contactNumber);
        imgView = (ImageView) rootView.findViewById(R.id.userImage);
        back = (Button)rootView.findViewById(R.id.cancel);
        update = (Button) rootView.findViewById(R.id.update);
        try
        {
            if(OA.loginAuthentication())
            {
                //System.out.println("Entered Contact List onCreateView");
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

                emailView.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            companyNameView.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });

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

                skypenameView.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            System.out.println(skypenameView+"skypenameView");
                            return true;
                        }
                        return false;
                    }
                });
            }
            else
            {
//  	        	OA.logout(this);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Validate(skypenameView);
            }
        });
        back.setOnClickListener(new OnClickListener() {

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

            if (OA.loginAuthentication()) {
                String accesskey = OystorApp.accessKey;

                contact_list_properties obj_contact_property = new contact_list_properties();

                obj_contact_property.setname(nameView.getText().toString());
                obj_contact_property.setemail(emailView.getText().toString());
                obj_contact_property.setsecondary_email(secEmailView.getText().toString());
                obj_contact_property.setcompany(companyNameView.getText().toString());
                obj_contact_property.setdesignation(designationView.getText().toString());
                obj_contact_property.setskype_name(skypenameView.getText().toString());
                obj_contact_property.setcontact_number(contactNumberView.getText().toString());

                async_task_contact obj_asyncAddContact = new async_task_contact(getActivity(), obj_contact_property,"0");
                obj_asyncAddContact.execute(obj_contact_property);
            }
        }
    }
}
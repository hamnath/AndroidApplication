package com.example.vista.androidapplication.files_folders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;

import java.util.ArrayList;
import java.util.Calendar;


public class share_file extends Fragment  {

    OystorApp OA = new OystorApp();
    String docId="";
    EditText expiry;
    private int mYear;
    private int mMonth;
    private int mDay;
    Context mContext;

    static final int DATE_DIALOG_ID = 0;
    static final int USERS_DIALOG_ID = 1;
    static String[] emails = new String[100];

    public String selectedUserIds = "";
    public String selectedContactIds = "";
    public String shareClicked = "0";

    protected String[] availableUserIds = {""};
    protected String[] availableContactIds = {""};

    protected CharSequence[] _options = { "" };
    protected boolean[] _selections =  new boolean[ _options.length ];

    TextView rightsView;
    EditText subject;
    EditText message;

    CheckBox copy;
    CheckBox download;
    CheckBox forward;
    CheckBox passwordCheck;

    EditText password;
    ArrayList<String[]> arr_contacts;
    files_folders_properties obj_files_folders_properties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        arr_contacts = new ArrayList<String[]>();
        View rootView = inflater.inflate(R.layout.sharefile, container, false);

        Bundle bundle = this.getArguments();
        docId = bundle.getString("docId");

        try {
            System.out.println("Entered Share file");

            //Getting Contact details of the Users
            get_contacts obj_contacts = new get_contacts(getActivity(), "");
            arr_contacts = obj_contacts.execute().get();

            emails =  arr_contacts.get(0);
            availableContactIds = arr_contacts.get(1);
            availableUserIds = arr_contacts.get(2);

            System.out.println(emails + " Emails ");

            //emails = getContacts("");
            _options = new String[emails.length];
            _options = emails;
            System.out.println(_options.length + " Emails ");

            _selections = new boolean[_options.length];

            Button sharewith = (Button) rootView.findViewById(R.id.sharewith);
            sharewith.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    //getActivity().showDialog(USERS_DIALOG_ID);
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle( "Contacts" )
                            .setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
                            .setPositiveButton( "OK", new DialogButtonClickHandler() )
                            .create();

                    dialog.show();
                }
            });
        }
        catch (Exception ex)
        {
            System.out.println("Exception in insert Share");
        }


        Button share = (Button)rootView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateShare(v);
            }
        });


        subject = (EditText)rootView.findViewById(R.id.sharesubject);
        message = (EditText)rootView.findViewById(R.id.message);

        subject.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    message.requestFocus();
                    return true;
                }
                return false;
            }
        });

        password = (EditText)rootView.findViewById(R.id.password);
        password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //validateShare(message);
                    return false;
                }
                return false;
            }
        });

        passwordCheck = (CheckBox)rootView.findViewById(R.id.passwordCheck);
        passwordCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked)
                {
                    password.setVisibility(View.VISIBLE);
                }
                else
                {
                    password.setVisibility(View.GONE);
                }
            }
        });

        expiry = (EditText)rootView.findViewById(R.id.expiry);
        expiry.setKeyListener(null);

        expiry.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("Entered in Expiry view");

                //Displaying the datePicker in expiry date

                // get the current date, month and Year
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                //getActivity().showDialog(DATE_DIALOG_ID);
                DatePickerDialog obj_DatePickerDialog = new DatePickerDialog(getActivity(),mDateSetListener,mYear, mMonth, mDay);
                obj_DatePickerDialog.show();

                return false;
            }
        });

        rightsView= (TextView)rootView.findViewById(R.id.rights);
        password= (EditText)rootView.findViewById(R.id.password);

        copy = (CheckBox)rootView.findViewById(R.id.copy);
        download = (CheckBox)rootView.findViewById(R.id.download);
        forward = (CheckBox)rootView.findViewById(R.id.allowforward);

        return rootView;
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//
//
//        switch (id) {
//            case DATE_DIALOG_ID:
//                return new DatePickerDialog(getActivity(),
//                        mDateSetListener,
//                        mYear, mMonth, mDay);
//            case USERS_DIALOG_ID:
//                AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                        .setTitle( "Contacts" )
//                        .setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
//                        .setPositiveButton( "OK", new DialogButtonClickHandler() )
//                        .create();
//                return dialog;
//        }
//        return null;
//    }

    public class DialogButtonClickHandler implements DialogInterface.OnClickListener
    {
        public void onClick( DialogInterface dialog, int clicked )
        {
            switch( clicked )
            {
                case DialogInterface.BUTTON_POSITIVE:
                    System.out.println("Entered expiry date" + shareClicked);

                    selectedUserIds="";
                    selectedContactIds="";

                    putSelectedUsers();
                    break;
            }
        }
    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
    {
        public void onClick( DialogInterface dialog, int clicked, boolean selected )
        {
//			Log.i( "ME", _options[ clicked ] + " selected: " + selected );
        }
    }

    public DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                updateDisplay();
            }
        };

    protected void putSelectedUsers(){

        for( int i = 0; i < _options.length; i++ ){
            if(_selections[i])
            {
                if(!selectedContactIds.equals(""))
                {
                    selectedUserIds += "," + availableUserIds[i];
                    selectedContactIds += "," + availableContactIds[i];
                }
                else
                {
                    selectedUserIds += availableUserIds[i];
                    selectedContactIds += availableContactIds[i];
                }
            }
        }

        System.out.println("selectedUserIds" + selectedUserIds +" selectedContactIds "+selectedContactIds);
        if(shareClicked.equals("1"))
        {
            shareClicked = "0";
            //TextView view= (TextView)findViewById(R.id.rights);
             validateShare(rightsView);
        }
    }

    // the callback received when the user "sets" the date in the dialog
    private void updateDisplay() {
        expiry.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(""));
    }

    public void validateShare(View v)
    {
        System.out.println("Entered Validate Share" + selectedContactIds);

        if(selectedContactIds.equals(""))
        {
            //showDialog(USERS_DIALOG_ID);
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle( "Contacts" )
                    .setMultiChoiceItems( _options, _selections, new DialogSelectionClickHandler() )
                    .setPositiveButton( "OK", new DialogButtonClickHandler() )
                    .create();

            dialog.show();
            shareClicked = "1";
            System.out.println("Validate shareClicked " + shareClicked);
        }
        else if(subject.getText().toString().equals("") || subject.getText().toString().equals(" "))
        {
            OA.alert("Enter the Subject", "Error", getActivity());
        }
        else
        {
            System.out.println("Validate files_folders_properties " + selectedUserIds);
            obj_files_folders_properties = new files_folders_properties();

            System.out.println("entered subject " + subject.getText().toString());
            System.out.println("entered message " + message.getText().toString());

            obj_files_folders_properties.setsubject(subject.getText().toString());
            obj_files_folders_properties.setmessage(message.getText().toString());
            obj_files_folders_properties.setexpiryDates(expiry.getText().toString());

            obj_files_folders_properties.setdocId(docId);
            obj_files_folders_properties.setselectedUserIds(selectedUserIds);
            obj_files_folders_properties.setselectedContactIds(selectedContactIds);

            String rights = "1";

            if(copy.isChecked())
            {
                rights += ",3";
            }

            if(download.isChecked())
            {
                rights += ",5";
            }

            obj_files_folders_properties.setrights(rights);

            if(passwordCheck.isChecked())
            {
                obj_files_folders_properties.setis_password("Y");
                obj_files_folders_properties.setpassword(password.getText().toString());
            }
            else
            {
                obj_files_folders_properties.setis_password("N");
                obj_files_folders_properties.setpassword("");
            }

            obj_files_folders_properties.setisForward(forward.isChecked());

            add_share obj_addShare = new add_share(obj_files_folders_properties,getActivity());
            obj_addShare.execute();

        }
    }

}

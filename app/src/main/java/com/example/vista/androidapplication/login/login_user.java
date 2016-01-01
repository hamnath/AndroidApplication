package com.example.vista.androidapplication.login;

import java.net.ConnectException;
import java.net.SocketException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;

import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.MainActivity;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.adapter.DbAdapter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class login_user extends Activity {
    /**
     * Called when the activity is first created.
     */
    private OystorApp OA = new OystorApp();
    public final DbAdapter db = new DbAdapter(this);
    public Cursor cursor;
    public String accessKey;
    private ProgressDialog dialog;
    String responseMsg = "";
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        System.out.println("logedin");
        mContext = getApplicationContext();
        final EditText username = (EditText) findViewById(R.id.txt_username);
        final EditText password = (EditText) findViewById(R.id.txt_password);
        final Button login = (Button) findViewById(R.id.btn_login);
        dialog= new ProgressDialog(this);
        try {
            System.out.println("logedout");

            username.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN)
                            && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        password.requestFocus();
                        System.out.println("logedout" + password);
                        return true;
                    }
                    return false;
                }
            });

            password.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN)
                            && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        loginLoader();
                        return false;
                    }
                    return false;
                }

            });

           // activityValidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                System.out.println(password);
                System.out.println(username);
                loginLoader();
            }

            // Intent intent = new Intent(login.this,MainActivity.class);
            // startActivity(intent);

        });
    }

    public void activityValidate() {
        // TODO Auto-generated method stub
        if (OA.accessKey == "" || OA.accessKey == null || OA.accessKey == "null" || OA.accessKey.equals("")) {
            db.open();
            cursor = db.fetchAllRow();
            startManagingCursor(cursor);
            Cursor c = cursor;
            if (c.getCount() > 0) {
                c.moveToFirst();
                String accessKey = c.getString(1);
                if (accessKey.equals("") || accessKey == null) {

                } else {
                    OA.accessKey = accessKey;
                    OA.freshEnter = "1";

                    this.finish();
                }
            } else {
                if (OA.errorCode == "" || OA.errorCode == null) {

                } else {
                    if (OA.errorMessage != "anyType{}")
                        OA.attentionBox(OA.errorMessage, "Attention", this);
                    OA.errorCode = "";
                    OA.errorMessage = "";
                }
            }
            close();
        } else {
            this.finish();
        }
    }

    private void close() {
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
    }

    private void loginLoader() {
        // TODO Auto-generated method stub
        Button login = (Button) findViewById(R.id.btn_login);
        EditText txtUsername = (EditText) findViewById(R.id.txt_username);
        EditText txtPassword = (EditText) findViewById(R.id.txt_password);

        String username = txtUsername.getText().toString().replace(" ","");
        String password = txtPassword.getText().toString();

        if (username.equals(""))
        {
            OA.alert("Please Enter Username", "Alert", this);
           txtUsername.requestFocus();
        }
        else if (password.equals(""))
        {
            OA.alert("Please Enter Password", "Alert", this);
            txtPassword.requestFocus();
        }
        else
        {
            AsyncTaskRunner runner = new AsyncTaskRunner(username, password);
            runner.execute(username, password);
        }

        login.setClickable(true);
        login.setEnabled(true);
    }

    /* Calling Webservice for getting username and password */
    public class AsyncTaskRunner extends AsyncTask<String, String, String> {

        public AsyncTaskRunner(String username, String password) {
            // TODO Auto-generated constructor stub
        }

        public void onPreExecute(){
            OA.loaderShow(login_user.this);
        }

        @Override
        public String doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("params");
            String username = params[0];
            String password = params[1];
            String accessKey ="";
            String errorKey = "";

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "authenticationAndroid");
            request.addProperty("apiKey", OystorApp.apiKey);
            request.addProperty("username", username);
            request.addProperty("password", password);
            request.addProperty("deviceName", Build.MODEL);
            request.addProperty("deviceType", "3");
            request.addProperty("osVersion", Build.VERSION.RELEASE);
            request.addProperty("deviceUniqueId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);

            try {
                System.out.println("Entered");
                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                System.out.println("Entered 1");
                SoapObject responseInner = (SoapObject) envelope.bodyIn;
                System.out.println("Entered 2");
                SoapObject response = (SoapObject) responseInner.getProperty("response");
                System.out.println("Entered 3");
                String returnCode = response.getProperty("returnCode").toString();

                if (response.getPropertyCount() > 0 && returnCode.equals("1"))
                {
                    System.out.println("Entered if");
                    SoapObject values = (SoapObject) response.getProperty("returnValues");
                    accessKey = values.getProperty("access_key").toString();
                }
                else
                {
                    System.out.println("Entered else");
                    responseMsg = response.getProperty("returnMessage").toString();
                    //OA.attentionBox(response.getProperty("returnMessage").toString(), "Alert", this);
                }
            } catch (ConnectException e)
            {
                if (OA.isInternetAvailable(mContext))
                {
                    //OA.attentionBox(Constants.API_SERVER_DOWN, "Attention",this);
                    responseMsg = Constants.API_SERVER_DOWN;
                }else
                {
                    // OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE,"Attention",this);
                    responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                }
            }
            catch (SocketException e)
            {
                if (OA.isInternetAvailable(mContext))
                {
                    //OA.attentionBox(Constants.API_SERVER_DOWN, "Attention",this);
                    responseMsg = Constants.API_SERVER_DOWN;

                } else
                {
                    responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                    //OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE,"Attention", this);
                }
            }
            catch (Exception e)
            {
                responseMsg = e.getMessage();
                //Toast.makeText(login_user.this, " " + e.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println( "responseMsg " + e);
                System.out.println(e.getMessage() + "Exception ");
                e.printStackTrace();
            }

            System.out.println(accessKey + "assume");
            return accessKey;
        }

        /*passing access key to on postexecute*/
        public void onPostExecute(String accessKey) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service

            OA.loaderHide();
            if(accessKey != "") {
                System.out.println("login" + accessKey);
                db.open();
                db.createLogin(accessKey);
                close();
                OA.accessKey = accessKey;
                OA.freshEnter = "1";
                Intent HomeFragment = new Intent().setClass(login_user.this, MainActivity.class);
                startActivity(HomeFragment);
            }
            else {
                OA.attentionBox(responseMsg, "Alert", login_user.this);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_sort:
                openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearch() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
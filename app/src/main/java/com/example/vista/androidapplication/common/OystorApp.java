package com.example.vista.androidapplication.common;

import com.example.vista.androidapplication.adapter.ActivityListAdapter;
import com.example.vista.androidapplication.login.login_user.AsyncTaskRunner;
import com.example.vista.androidapplication.properties.activity_list_properties;
import com.example.vista.androidapplication.properties.contact_list_properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Toast;

import static com.example.vista.androidapplication.common.DigestUtils.*;

public class OystorApp extends Application {
    public Cursor cursor;
	public ProgressDialog loader;
	
	public static final String apiKey="abcdefhijklmnop";
//uat
	public static final String API_LOC = "http://54.251.110.207/oystor_api_v2_8/";
	public static final String JAVA_API_LOC = "http://admin.oystor.com/oystoruat/";
//local
    //  public static final String API_LOC = "http://10.0.0.108/oystor_api_v2_8/";
	// public static final String JAVA_API_LOC = "http://10.0.0.108:8070/portal/";
	//com
    //public static final String API_LOC = "http://api.oystor.com/oystor_api";
    //public static final String JAVA_API_LOC = "https://www.oystor.com/oystor/";
	public static final String SOAP_ACTION = API_LOC + "webservices.php"; 
	public static final String NAMESPACE = API_LOC + "webservices.php";
	public static final String URL = API_LOC + "webservices.php?wsdl";
	
	public static final String JAVA_SOAP_ACTION = "http://DefaultNamespace/";
	public static final String JAVA_NAMESPACE = "http://DefaultNamespace/";
	public static final String JAVA_URL = JAVA_API_LOC + "DocumentService.jws";
	public static final String JAVA_REGISTER_LOC = JAVA_API_LOC + "register_page";
	
	public static String accessKey = "";	//"0Ip1MSGI8i";   //"DZowE8uoxZ";        //"W9PEd5F3g6";
	public static String errorCode;
	public static String errorMessage;
	public static String refresh = "0";
	public static String freshEnter = "0";
	public static String downloadFolder = "/Oystor_Downloads";
	public static String albumFolder = "/My_Albums";
	public static String sharedFolder = "/My_Shared_Files";
	public static String sharedrealFolder = "/Shared_Folder";
	public static String specialCharExp = "[^a-zA-Z0-9._]+";
	public static String specialCharRep = "__";


    //vinoth make changes for adding folderId
    public static String folderId = "0";
    public static String is_shared = "0";
    public static String download_file_path;
    public static ActivityListAdapter obj_activity_adapter;
    public static activity_list_properties obj_activity_list = null;
    public static contact_list_properties obj_edit_contacts_list = null;
    public static String docId = "0";

    public static ArrayList<activity_list_properties> obj_activity_array_List;

    @Override
    public void onCreate() {
    	
    }
    
    public void attentionBox(String msg, String title, Context context)
    {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
				.setTitle(title)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				         ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
				         // the rest of your stuff
				    }
				});
		AlertDialog alert = builder.create();
		alert.show();
    }
    
    public void alertToast(String msg, Context context)
    {
    	Toast.makeText(context, msg,1000).show();
    }
    
    public boolean loginAuthentication()
    {
    	if(!this.accessKey.equals("") && !this.accessKey.equals(" "))
    	{	System.out.println(accessKey);
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public void loaderShow(Context context)
    {
    	 loader = ProgressDialog.show(context, "", "Loading", true, true);
    	 loader.show();
    }
    
    public void loaderRefShow(Context context)
    {
    	 loader = ProgressDialog.show(context, "", "Refreshing", true, true);
    	 loader.show();
    }
    
    public void loaderUplShow(Context context)
    {
    	 loader = ProgressDialog.show(context, "", "Uploading", true, true);
    	 loader.show();
    }
    
    public void loaderCustomShow(Context context,String text)
    {
    	loader = ProgressDialog.show(context, "", text, true, true);
    	loader.show();
    }
    
    public void loaderHide()
    {
    	 loader.dismiss();
    }
    
    public void errorResponse(String returnCode, String returnMessage)
    {
    	this.errorCode = returnCode;
    	this.errorMessage = returnMessage;
    }

	public String sizeRoundInputMB(double val)throws Exception
	{
        String result;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        if(val < 1024)
        {
        	result = df.format(val).toString()+" MB";
        }
        else
        {
        	val = val/1024;
        	result = df.format(val).toString()+" GB";
        }
        
        return result;
	}

    public String sizeRound(double val)throws Exception
    {
        String result;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        if(val < 1024)
        {
            result = df.format(val).toString()+" KB";
        }
        else if((val/1024) < 1024)
        {
            val = val/1024;
            result = df.format(val).toString()+" MB";
        }
        else
        {
            val = val/(1024*1024);
            result = df.format(val).toString()+" GB";
        }

        return result;
    }

    public void logout(Context context)
    {
        try{
            Intent intent = new Intent().setClass(context, OystorApp.class);
            context.startActivity(intent);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	public String lengthReduce(String name,int num)
	{
		if(name.length() > num)
		{
			return name.substring(0,num) + "..";
		}
		else
		{
			return name;
		}
	}
		  
	  public String parseDate(String val,String type) throws Exception {
		  SimpleDateFormat givenFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  Date date = givenFormat.parse(val);
		  SimpleDateFormat resultFormat;
		  if(type == "1")
		  {
			  resultFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
		  }
		  else if(type == "2")
		  {
			  resultFormat = new SimpleDateFormat("dd MMM, yyyy");
		  }
		  else if(type == "3")
		  {
			  resultFormat = new SimpleDateFormat("dd - MMM - yyyy");
		  }
		  else
		  {
			  resultFormat = new SimpleDateFormat("yyyy-MM-dd");
		  }
		  
		  String result = resultFormat.format(date);
		  return result;
		}
	  
	  public static String getFileCheckSum(InputStream is){
		  String output="";
		  try {
		   
		   MessageDigest digest = MessageDigest.getInstance("MD5");
		         byte[] buffer = new byte[is.available()];
		         int read = 0;
		          while( (read = is.read(buffer)) > 0){
		              digest.update(buffer, 0, read);
		          }

		         output = md5DigestAsHex(buffer);
		   
		   }catch (Exception e){
		             e.printStackTrace();
		            }
		  return(output); 
		 }
	    
	    public static void sendEmail(Context context, String[] recipientList, String title, String subject, String body) {
	        Intent emailIntent = new Intent(Intent.ACTION_SEND);

	        emailIntent.setType("text/html");

	        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientList);

	        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

	        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

	        context.startActivity(Intent.createChooser(emailIntent, title));

	    }

        public boolean isInternetAvailable1(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
            }
        }
	    
	  //encording
		public static byte[] getBytesFromFile(File file) throws IOException {
		    InputStream is = new FileInputStream(file);
		
		    // Get the size of the file
		    long length = file.length();
		
		    // You cannot create an array using a long type.
		    // It needs to be an int type.
		    // Before converting to an int type, check
		    // to ensure that file is not larger than Integer.MAX_VALUE.
		    if (length > Integer.MAX_VALUE) {
		        // File is too large
		    }
		
		    // Create the byte array to hold the data
		    byte[] bytes = new byte[(int)length];
		
		    // Read in the bytes
		    int offset = 0;
		    int numRead = 0;
		    while (offset < bytes.length
		           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		        offset += numRead;
		    }
		
		    // Ensure all the bytes have been read in
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file "+file.getName());
		    }
		
		    // Close the input stream and return bytes
		    is.close();
		    return bytes;
		}
	  
	  //final static private String charset = "[a-zA-Z0-9_]"; // separate this out for future fixes
	  //final static private String regex = charset + "+@" + charset + "+\." + charset + "+";
	  final static private String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)";

	  public boolean isValidEmail(String email) {
	      return email.matches(regex);
	  }
	  
	  public void internetProblem(Context context)
	  {
	    if(isInternetAvailable(context))
      	{
			errorResponse("9001", Constants.API_SERVER_DOWN);
			System.exit(1);
      	}
      	else
      	{
			errorResponse("9002", Constants.INTERNET_NOT_AVAILABLE);
			System.exit(1);
      	}
	  }

    public void alert(String msg, String title, Context onClickListener)
    {
    	AlertDialog alertDialog = new AlertDialog.Builder(onClickListener).create();
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
             //here you can add functions
          } });
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.show();
    }

	public boolean isInternetAvailable(Context context) {
		// TODO Auto-generated method stub
		try {
	        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

	        return cm.getActiveNetworkInfo().isConnectedOrConnecting();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}

	public void attentionBox(String apiServerDown, String title,
			AsyncTaskRunner asyncTaskRunner) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
		builder.setMessage("added")
				.setTitle(title)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				         ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
				         // the rest of your stuff
				    }
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	  
	  /*
	   * Unused Functions
	   * 	
		  public static void RoundTwoDecimalPlaces() {
		  float num = 2.954165f;
		  float round = Round(num,2);
		  System.out.println("Rounded data: " + round);
		  }

		  public static float Round(float Rval, int Rpl) {
		  float p = (float)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  float tmp = Math.round(Rval);
		  return (float)tmp/p;
		  }
	   */



}
package com.example.vista.androidapplication.files_folders;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;
import android.app.Dialog;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.activities.clear_activity_list;
import com.example.vista.androidapplication.adapter.FilesFoldersAdapter;
import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.contacts.addContact;
import com.example.vista.androidapplication.contacts.editContact;
import com.example.vista.androidapplication.properties.activity_list_properties;
import com.example.vista.androidapplication.properties.files_folders_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;

public class files_folder_list extends ListFragment {
    private OystorApp OA = new OystorApp();
    public String currentFolderId = "0",previousFolderId = "0",rootFolderId="0";
    FilesFoldersAdapter obj_files_folders_adapter;
    ArrayList<files_folders_properties> arr_obj_files_folders_list = null;
    String responseMsg = "",tags="";
    Fragment fragment = null;
    activity_list_properties obj_activity_property;
    public Dialog tagdialog;
    public String downloadStarted = "0";

    InputMethodManager inputMethodManager;
    Button button1;
    Button button2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Entered files and folders List onCreateView");
        arr_obj_files_folders_list = new ArrayList<files_folders_properties>();

        View rootView = inflater.inflate(R.layout.main_folder_list, container, false);
        if (OA.loginAuthentication()) {
            String accesskey = OystorApp.accessKey;

            System.out.println("Login Authentication" + accesskey);

            try {
                FilesFoldersAsyncTaskRunner runner = new FilesFoldersAsyncTaskRunner(accesskey);
                arr_obj_files_folders_list = runner.execute(accesskey).get();

                if (arr_obj_files_folders_list != null && arr_obj_files_folders_list.size() != 0) {
                    System.out.println("Entered On Post IF    " + arr_obj_files_folders_list.size());
                    obj_files_folders_adapter = new FilesFoldersAdapter(getActivity(), R.layout.folder_list, arr_obj_files_folders_list);
                    setListAdapter(obj_files_folders_adapter);

                }
            }
            catch (Exception ex)
            {
                System.out.println("Exception in Files and Folders : " + ex);
            }
             OA.loaderHide();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedState) {

        super.onActivityCreated(savedState);
        registerForContextMenu(getListView());

        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //Log.v("TAG", "CLICKED row number: " + arg2);
                System.out.println("e  " + arg2);
            }

        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Files Folders");
        MenuInflater inflaters = getActivity().getMenuInflater();
        inflaters.inflate(R.menu.files_folders_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        super.onContextItemSelected(item);
        System.out.println("Files Folders Item Context Menu");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ImageView emptyView;
        System.out.println("Files Folders Item " + item.getTitle().toString());
        switch (item.getItemId()) {
            case R.id.activity_item_1:
                System.out.println("activity Item Get ID" + OystorApp.obj_activity_list.getId());
                int listPosition = info.position;

                System.out.println("Size position List " + OystorApp.obj_activity_array_List.size());

                if (OystorApp.obj_activity_array_List.size() != 0) {

                    System.out.println("Size Array List " + OystorApp.obj_activity_array_List.size());
                    obj_activity_property = OystorApp.obj_activity_array_List.get(listPosition);
                }

                if (OystorApp.obj_activity_adapter.getCount() > 0) {
                    System.out.println("obj_activity_adapter entered 111" + OystorApp.obj_activity_adapter.getCount());
                    clear_activity_list obj_clear_activity_list = new clear_activity_list(getActivity(), OystorApp.obj_activity_list.getId());
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

                return true;

            case R.id.activity_item_2:

                clear_activity_list obj_clear_activity_list = new clear_activity_list(getActivity(), "0");
                obj_clear_activity_list.execute("0");
//                obj_activity_adapter.clear();
//
//                setListAdapter(obj_activity_adapter);
                emptyView = (ImageView) getListView().getEmptyView();
                emptyView.setImageResource(R.drawable.noactivityfound);

                return true;

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
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.add(R.id.frame_container, fragment);
                    ft.addToBackStack("fragBack");
                    ft.commit();
                }
                return true;

            case R.id.createFile:
                return true;

            case R.id.createFolder:

                tagdialog = new Dialog(getActivity());
                tagdialog.setContentView(R.layout.createfolder);
                tagdialog.setTitle("Create Folder");
                tagdialog.show();
                EditText folderbox = (EditText) tagdialog.findViewById(R.id.folderbox);

                inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                folderbox.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press

                            EditText folderbox = (EditText) tagdialog.findViewById(R.id.folderbox);
                            final String folderboxText = folderbox.getText().toString();
                            create_folder obj_create_folder = new create_folder(folderboxText, getActivity());
                            obj_create_folder.execute();
                            tagdialog.dismiss();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                            fragment = new files_folder_list();
                            if (fragment != null) {
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.add(R.id.frame_container, fragment);
                                ft.addToBackStack("fragBack");
                                ft.commit();
                            }

                            return true;
                        }
                        return false;
                    }
                });

                button1 = (Button) tagdialog.findViewById(R.id.ok);
                button1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        EditText folderbox = (EditText) tagdialog.findViewById(R.id.folderbox);
                        final String folderboxText = folderbox.getText().toString();
                        create_folder obj_create_folder = new create_folder(folderboxText, getActivity());
                        obj_create_folder.execute();
                        tagdialog.dismiss();

                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        fragment = new files_folder_list();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction ft = fragmentManager.beginTransaction();
                            ft.add(R.id.frame_container, fragment);
                            ft.addToBackStack("fragBack");
                            ft.commit();
                        }
                    }
                });
                button2 = (Button) tagdialog.findViewById(R.id.cancel);
                button2.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tagdialog.dismiss();
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });


                return true;

            case R.id.addTag:
                tagdialog = new Dialog(getActivity());
                tagdialog.setContentView(R.layout.addtags);
                tagdialog.setTitle("Add Tags");
                tagdialog.show();

                EditText tagsbox = (EditText)tagdialog.findViewById(R.id.tagstextbox);
                tagsbox.setText(tags.replace(",", ";"));

                inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                tagsbox.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press

                            EditText tagsBox = (EditText)tagdialog.findViewById(R.id.tagstextbox);
                            final String tagText = tagsBox.getText().toString();
                            add_tags obj_add_tags = new add_tags(OystorApp.docId, tagText, getActivity());
                            obj_add_tags.execute();

                            tagdialog.dismiss();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            return true;
                        }
                        return false;
                    }
                });

                button1= (Button) tagdialog.findViewById(R.id.ok);
                button1.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        EditText tagsBox = (EditText)tagdialog.findViewById(R.id.tagstextbox);
                        final String tagText = tagsBox.getText().toString();
                        add_tags obj_add_tags = new add_tags(OystorApp.docId, tagText, getActivity());
                        obj_add_tags.execute();

                        tagdialog.dismiss();
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });
                button2 = (Button) tagdialog.findViewById(R.id.cancel);
                button2.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tagdialog.dismiss();
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });

                return true;

            case R.id.share:

                fragment = new share_file();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();

                    //Passing docId parameters
                    Bundle args = new Bundle();
                    args.putString("docId", OystorApp.docId);
                    fragment.setArguments(args);

                    ft.add(R.id.frame_container, fragment);
                    ft.addToBackStack("fragBack");
                    ft.commit();
                }
                return true;

            case R.id.download:
                viewDownloadFile(OystorApp.docId);
                return true;
        }
        return true;
    }

    // Method for downloading a file and viewing a file.
    public void viewDownloadFile(String docid) {
        if (!downloadStarted.equals("0")) {
            OA.alert("Already a download for this file started. Please wait until it finishes.", "Attention", getActivity());
        } else {
            String rootPath = "";
            if (Environment.getExternalStorageState().equals("mounted")) {
                files_folders_properties obj_files_folders_properties = new files_folders_properties();

                try {
                    download_Details obj_download_details = new download_Details(getActivity(), docid);
                    obj_files_folders_properties = obj_download_details.execute(docid).get();
                } catch (Exception e) {
                    System.out.println("Err" + e);
                }

                rootPath = Environment.getExternalStorageDirectory().getPath();
                System.out.println("object rootPath" + rootPath);

                String path = obj_files_folders_properties.getPath();

                if (path.equals("anyType{}")) {
                    path = "";
                } else if (!path.equals("")) {
                    path = path.toString().replaceAll(OA.specialCharExp, OA.specialCharRep) + "/";
                }

                String docLocalPath = rootPath + OA.downloadFolder + "/" + path + obj_files_folders_properties.getfilename().toString().replaceAll(OA.specialCharExp, OA.specialCharRep).toLowerCase();

                StatFs stat = new StatFs(rootPath);
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                long freeSpaceInBytes = availableBlocks * blockSize;

                if (Double.parseDouble(obj_files_folders_properties.getSize()) <= freeSpaceInBytes) {
                    downloadStarted = "2";

                    //Calling only download async task
                    final download_file task = new download_file(getActivity(), false);

                    task.s3accesskey = obj_files_folders_properties.gets3AccessKey();
                    task.s3secretkey = obj_files_folders_properties.gets3SecretKey();
                    task.bucketname = obj_files_folders_properties.getbucketName();
                    task.fileName = obj_files_folders_properties.getfilename().toString();
                    task.fileLoc = obj_files_folders_properties.getfileLoc();
                    task.path = obj_files_folders_properties.getPath().toString();
                    task.size = obj_files_folders_properties.getSize();
                    task.rootpath = rootPath;
                    task.downloadFolder = OA.downloadFolder;
                    try {
                        task.execute(getActivity());
                        downloadStarted = "0";
                    } catch (Exception ex) {
                        System.out.println("Error in calling download file" + ex);
                    }
                } else {
                    OA.alert("Free Space is not available to download", "Error", getActivity());
                }
            }
        }
    }

    public class FilesFoldersAsyncTaskRunner extends AsyncTask<String, String, ArrayList<files_folders_properties>> {
        public FilesFoldersAsyncTaskRunner(String acceskey) {

            System.out.println(acceskey);
        }

        protected void onPreExecute() {
            OA.loaderShow(getActivity());
        }

        @Override
        protected ArrayList<files_folders_properties> doInBackground(String... params) {
            Object bitmap;
            String methodName = "";

            if(OystorApp.folderId != "0" )
                 methodName="getFilesFolderDetails";
            else
                 methodName = "getAllFilesFolderInfo";

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, methodName);
            request.addProperty("accessKey", OystorApp.accessKey);
            request.addProperty("folderId",OystorApp.folderId);

            if(OystorApp.folderId != "0")
                request.addProperty("isShared     ",OystorApp.is_shared);

            System.out.println(OystorApp.folderId  + "msg3");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            // envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);

            try {
                androidHttpTransport.call(OystorApp.SOAP_ACTION,envelope);
                SoapObject responseInner = (SoapObject) envelope.bodyIn;

                SoapObject response = (SoapObject) responseInner.getProperty("response");
                String returnCode = response.getProperty("returnCode").toString();
                String exitMode = response.getProperty("exitMode").toString();

                System.out.println("response Count" + response.getPropertyCount() + "returnCode" + returnCode);

                if(response.getPropertyCount() > 0 && returnCode.equals("1"))
                {
                    SoapObject values = (SoapObject) response.getProperty("returnValues");
                    double size;
                    System.out.println("values.getPropertyCount()" + values.getPropertyCount());
                    String type,folderid,foldername,modified_time,no_of_files,no_of_folders,is_shared, created_by;
                    String fileid,filename,modified,extension,is_public,convertsize,getsize;
                    String expiry_date,Path,Hash,doctype;

                    int totalCount = values.getPropertyCount();
                    System.out.println(totalCount + "totalcount");
                    if(totalCount != 0)
                    {
                        for (int detailCount = 0; detailCount < totalCount; detailCount++) {
                            SoapObject docVals = (SoapObject) values.getProperty(detailCount);
                            type = docVals.getProperty("type").toString();
                            folderid = docVals.getProperty("folderid").toString();
                            foldername = docVals.getProperty("foldername").toString();
                            getsize = docVals.getProperty("size").toString();
                            modified_time = docVals.getProperty("modified_time").toString();
                            is_shared = docVals.getProperty("is_shared").toString();
                            created_by = docVals.getProperty("created_by_name").toString();
                            no_of_files = docVals.getProperty("no_of_files").toString();
                            no_of_folders = docVals.getProperty("no_of_folders").toString();
                            fileid = docVals.getProperty("fileid").toString();
                            filename = docVals.getProperty("filename").toString();
                            modified = docVals.getProperty("modified").toString();
                            extension = docVals.getProperty("extension").toString();
                            is_public = docVals.getProperty("is_public").toString();
                            expiry_date = docVals.getProperty("expiry").toString();
                            Path = docVals.getProperty("filepath").toString();
                            Hash = docVals.getProperty("hash").toString();
                            doctype = docVals.getProperty("doctype").toString();

                            files_folders_properties obj_files_folders_properties = new files_folders_properties();

                            if (type.equals("D")) {
                                obj_files_folders_properties.setId(folderid);
                                obj_files_folders_properties.setName(OA.lengthReduce(foldername, 36));
                                obj_files_folders_properties.setFullname(foldername);
                                obj_files_folders_properties.setisShared(is_shared);
                                obj_files_folders_properties.setCreatedby(created_by);
                                obj_files_folders_properties.setNoOfFiles(no_of_files);
                                obj_files_folders_properties.setNoOfFolders(no_of_folders);
                            } else {
                                obj_files_folders_properties.setId(fileid);
                                String docName = filename + "." + extension;
                                String docName2 = filename;
                                obj_files_folders_properties.setName(OA.lengthReduce(docName2, 36));
                                obj_files_folders_properties.setFullname(docName);
                                obj_files_folders_properties.setDate(OA.parseDate(modified, "1"));
                                obj_files_folders_properties.setExt(extension);
                                obj_files_folders_properties.setExpiry(OA.parseDate(expiry_date, "1"));
                                obj_files_folders_properties.setPath(Path);
                                obj_files_folders_properties.setType(doctype);
                                size = Double.parseDouble(getsize);
                                convertsize = OA.sizeRound(size);
                                obj_files_folders_properties.setSize(convertsize);
                                obj_files_folders_properties.setHash(Hash);

                                if (is_public.equals("Y"))
                                    obj_files_folders_properties.setTypePrivate("Public");
                                else
                                    obj_files_folders_properties.setTypePrivate("Private");
                            }
                            obj_files_folders_properties.setRowType(type);

                            arr_obj_files_folders_list.add(obj_files_folders_properties);
                        }
                    }
                    else
                    {
                        System.out.println("No Files and Folders");
                    }
                }
               else if(exitMode.equals("2"))
                {
                    OA.alert(response.getProperty("returnMessage").toString(), "Error", getActivity());
                    //responseMsg=response.getProperty("returnMessage").toString();
                }
                else if(exitMode.equals("1"))
                {
                    OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                    OA.logout(getActivity());
                }
                else
                {
                    System.out.println("Text No Files and Folders");
                    OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                    OA.logout(getActivity());
                }
            } catch (ConnectException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());
                    //responseMsg = Constants.API_SERVER_DOWN;
                } else {
                    //responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                    OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                }
            } catch (SocketException e) {
                if (OA.isInternetAvailable(getActivity())) {
                    OA.attentionBox(Constants.API_SERVER_DOWN, "Attention", getActivity());
                    //responseMsg = Constants.API_SERVER_DOWN;

                } else {
                    OA.attentionBox(Constants.INTERNET_NOT_AVAILABLE, "Attention", getActivity());
                    //responseMsg = Constants.INTERNET_NOT_AVAILABLE;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage() + "Exception ");
                e.printStackTrace();
            }
            return arr_obj_files_folders_list;
        }

        /*passing access key to on postexecute*/
        public void onPostExecute(ArrayList<files_folders_properties> obj_act) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service
//            if(responseMsg != "") {
//                OA.attentionBox(responseMsg, "Alert", getActivity());
//            }

            if(responseMsg != "") {
                OA.attentionBox(responseMsg, "Alert", getActivity());
            }
            else {
                System.out.println("Entered On Post else");

                setListAdapter(obj_files_folders_adapter);
                ImageView emptyView = (ImageView) getListView().getEmptyView();
                emptyView.setImageResource(R.drawable.nofilefound);
            }
        }
     }
}

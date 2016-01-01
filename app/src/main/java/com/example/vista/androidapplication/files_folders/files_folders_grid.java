package com.example.vista.androidapplication.files_folders;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.vista.androidapplication.DetailsActivity;
import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.adapter.FilesFoldersAdapter;
import com.example.vista.androidapplication.adapter.GridViewAdapter;
import com.example.vista.androidapplication.adapter.ImageItem;
import com.example.vista.androidapplication.common.Constants;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;

public class files_folders_grid extends Fragment {
    private OystorApp OA = new OystorApp();
    public String currentFolderId = "0", previousFolderId = "0", rootFolderId = "0";
    GridViewAdapter obj_files_folders_adapter;
    ArrayList<files_folders_properties> arr_obj_files_folders_list = null;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_folder_grid, container, false);
        dialog = new ProgressDialog(getActivity());
        arr_obj_files_folders_list = new ArrayList<files_folders_properties>();

        if (OA.loginAuthentication()) {
            String accesskey = OystorApp.accessKey;

            System.out.println("Login Authentication" + accesskey);

            FilesFoldersGridAsyncTaskRunner runner = new FilesFoldersGridAsyncTaskRunner(accesskey);
            runner.execute();
        }

        System.out.println("arr_obj_files_folders_list" + arr_obj_files_folders_list.size());


        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                System.out.println("Welcome to item GridView");

                Fragment fragment = null;
                fragment = new files_folders_details();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    android.app.FragmentTransaction ft = fragmentManager.beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("title", item.getTitle());
                    args.putParcelable("image", item.getImage());

                    fragment.setArguments(args);
                    ft.replace(R.id.frame_container, fragment);
                    ft.commit();
                }
            }
        });

        return rootView;
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {

        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

    public class FilesFoldersGridAsyncTaskRunner extends AsyncTask<String, String, ArrayList<files_folders_properties>> {
        public FilesFoldersGridAsyncTaskRunner(String acceskey) {

            System.out.println(acceskey);
        }

        @Override
        protected ArrayList<files_folders_properties> doInBackground(String... params) {
            String methodName = "";

            if(OystorApp.folderId != "0" )
                methodName="getFilesFolderDetails";
            else
                methodName = "getAllFilesFolderInfo";

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, methodName);
            request.addProperty("accessKey", OystorApp.accessKey);
            request.addProperty("folderId",OystorApp.folderId);

            if(OystorApp.folderId != "0")
                request.addProperty("isShared",OystorApp.is_shared);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            // envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);

            try {
                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                SoapObject responseInner = (SoapObject) envelope.bodyIn;

                SoapObject response = (SoapObject) responseInner.getProperty("response");
                String returnCode = response.getProperty("returnCode").toString();
                String exitMode = response.getProperty("exitMode").toString();

                if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                    SoapObject values = (SoapObject) response.getProperty("returnValues");
                    double size;
                    System.out.println("values Result " + values.getPropertyCount());
                    String type, folderid, foldername, modified_time, no_of_files, no_of_folders,is_shared, created_by;
                    String fileid, filename, modified, extension, is_public, convertsize, getsize;
                    String expiry_date, Path, Hash, doctype;
                    int totalCount = values.getPropertyCount();
                    System.out.println(totalCount + "totalcount");
                    if (totalCount != 0) {
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

                    } else {
                        System.out.println("No Files and Folders");
                    }
                }
                else if(exitMode.equals("2"))
                {
                    OA.alert(response.getProperty("returnMessage").toString(), "Error", getActivity());
                }
                else if(exitMode.equals("1"))
                {
                    OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                    OA.logout(getActivity());
                }
                else {
                    OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                    OA.logout(getActivity());
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
                System.out.println(e + "msg3");

                e.printStackTrace();
            }
            System.out.println(arr_obj_files_folders_list.size() + " msg3 size");

            return arr_obj_files_folders_list;
        }


        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        /*passing access key to on postexecute*/
        public void onPostExecute(ArrayList<files_folders_properties> obj_act) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service

            System.out.println("Entered");
            System.out.println("On post result" + obj_act.size());

            arr_obj_files_folders_list = obj_act;
            if (obj_act.size() != 0) {
                gridAdapter = new GridViewAdapter(getActivity(), R.layout.folder_grid, arr_obj_files_folders_list);
                gridView.setAdapter(gridAdapter);
            } else {
                System.out.println("Entered On Post else");

            }
            dialog.dismiss();
        }
    }
}

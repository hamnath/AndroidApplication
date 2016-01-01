package com.example.vista.androidapplication.shared;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.common.downloadImage;
import com.example.vista.androidapplication.common.downloadImageFromURL;
import com.example.vista.androidapplication.files_folders.download_Details;
import com.example.vista.androidapplication.files_folders.download_file;
import com.example.vista.androidapplication.properties.files_folders_properties;
import com.example.vista.androidapplication.properties.shared_list_properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.net.ConnectException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class shared_file_details extends Fragment {

    public OystorApp OA = new OystorApp();
    protected static final int GET_TAGS = 0;
    public String subject, expiry_date, shared_on, emails, name, ext, sizes, id, rights, hash, message;
    public String tags = "", shareId = "";
    public String downloadStarted = "0";
    TextView subjects;
    TextView share_on;
    TextView emaild;
    TextView expired;
    TextView messages;
    ImageView userImageView;
    String userImage;
    ArrayList<shared_list_properties> arr_obj_shared_list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.shared_file_details, container, false);

        subjects = (TextView) rootView.findViewById(R.id.subject);
        share_on = (TextView) rootView.findViewById(R.id.sharedOn);
        emaild = (TextView) rootView.findViewById(R.id.emails);
        expired = (TextView) rootView.findViewById(R.id.expiry);
        messages = (TextView) rootView.findViewById(R.id.message);
        userImageView = (ImageView) rootView.findViewById(R.id.user_image);
        arr_obj_shared_list = new ArrayList<shared_list_properties>();

        if (OA.loginAuthentication()) {
            String accesskey = OystorApp.accessKey;
            System.out.println("Login  Authentication" + accesskey);

            Bundle bundle = this.getArguments();
            shareId = bundle.getString("share_id");
            userImage = bundle.getString("userImage");

            System.out.println("shared_file_details userimage  dddd       " + userImage);
            //downloadImageFromURL objdown = new downloadImageFromURL(userImage, userImageView);
            //objdown.execute(userImage);

            try {
                if(userImage.equals("") ||userImage.equals("anyType{}"))
                    userImageView.setImageResource(R.drawable.no_user_image);
                else {
                    downloadImage objdown = new downloadImage(userImage);
                    userImageView.setImageDrawable(objdown.execute(userImage).get());
                }
            }
            catch (Exception ex)
            {
                System.out.println("Error in Share file Details");
            }

			/*calling Share details Async task */
            try {
                AsyncTaskRunner3 runner2 = new AsyncTaskRunner3(accesskey);
                arr_obj_shared_list = runner2.execute(accesskey).get();
            }
            catch (Exception ex)
            {
              System.out.println("Error" + ex);
            }

            try {
                for (int i = 0; i < arr_obj_shared_list.size(); i++) {

                    System.out.println(" DDDDDDDDD " + arr_obj_shared_list.get(i).getDocs_name().toString());

                    final List<String> rightTest = Arrays.asList(rights.split(","));
                    share_on.setText(arr_obj_shared_list.get(i).getSharedOn().toString());
                    subjects.setText(arr_obj_shared_list.get(i).getDocs_name().toString());
                    emaild.setText(arr_obj_shared_list.get(i).getEmails().toString());
                    expired.setText("Not Available");
                    messages.setText(arr_obj_shared_list.get(i).getMessage().toString());

                    final float scale = getResources().getDisplayMetrics().density;

                    LinearLayout allfiles = (LinearLayout)rootView.findViewById(R.id.allfiles);
                    LinearLayout totalLayout = new LinearLayout(getActivity().getApplicationContext());
                    LinearLayout.LayoutParams fullparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    totalLayout.setLayoutParams(fullparams);
                    totalLayout.setPadding(0, 0, 0, 10);

                    ImageView extimage = new ImageView(getActivity().getApplicationContext());
                    String fileExtensionImage = "drawable/file_extension_" + arr_obj_shared_list.get(i).getDocs_ext().toString();
                    int imageResource = getResources().getIdentifier(fileExtensionImage, null, getActivity().getPackageName());

                    if (imageResource > 0) {
                        extimage.setImageResource(imageResource);
                    } else {
                        extimage.setImageResource(R.drawable.file_extension_default);
                    }

                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.gravity = Gravity.CENTER_VERTICAL;
                    extimage.setLayoutParams(params1);
                    totalLayout.addView(extimage);

                    LinearLayout linear1 = new LinearLayout(getActivity().getApplicationContext());
                    linear1.setOrientation(LinearLayout.VERTICAL);
                    linear1.setLayoutParams(new LinearLayout.LayoutParams(
                            (int) (200 * scale), LinearLayout.LayoutParams.WRAP_CONTENT));
                    TextView filename = new TextView(getActivity().getApplicationContext());
                    filename.setText(arr_obj_shared_list.get(i).getDocs_name().toString());

                    //System.out.println(filename + " filename");
                    //System.out.println(fileExtensionImage + " fileEXT");

                    filename.setTextAppearance(getActivity().getApplicationContext(),
                            R.style.shareViewFileName);
                    filename.setWidth(160);
                    linear1.addView(filename);

                    LinearLayout sizeLayout = new LinearLayout(
                            getActivity().getApplicationContext());
                    TextView sizeVal = new TextView(getActivity().getApplicationContext());
                    sizeVal.setText(OA.sizeRound(Double.parseDouble(arr_obj_shared_list.get(i).getDocs_sizes().toString())));
                    //sizeVal.setText((details.get(i).getDocs_sizes().toString()));
                    sizeVal.setTextAppearance(getActivity().getApplicationContext(),
                            R.style.shareViewData);
                    sizeLayout.addView(sizeVal);
                    linear1.addView(sizeLayout);
                    totalLayout.addView(linear1);

                    LinearLayout linear2 = new LinearLayout(getActivity().getApplicationContext());
                    linear2.setLayoutParams(params1);
                    totalLayout.addView(linear2);

                    if (rightTest.contains("3")) {
                        TextView fileOptionDownload = new TextView(
                                getActivity().getApplicationContext());
                        fileOptionDownload.setText("Download");
                        fileOptionDownload.setTextAppearance(getActivity().getApplicationContext(), R.style.shareViewDownload);

                        fileOptionDownload.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                System.out.println("On click Download");
                                viewDownloadFile(id);
                            }
                        });
                        linear2.addView(fileOptionDownload);
                    }

                    allfiles.addView(totalLayout);
                }
            } catch (ConnectException e) {
                System.out.println("Exception " + e);
                OA.internetProblem(getActivity());
            } catch (SocketException e) {
                OA.internetProblem(getActivity());
                System.out.println("Exception " + e);
            } catch (Exception e) {
                System.out.println("Exception " + e);
                e.printStackTrace();
            }

        }

        return rootView;
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
                    task.downloadFolder = OA.downloadFolder + OA.sharedFolder;
                    //task.fileType = doctype;
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

    public class AsyncTaskRunner3 extends
            AsyncTask<String, String, ArrayList<shared_list_properties>> {

        public AsyncTaskRunner3(String accesskey) {
            System.out.println(accesskey + "acceskey");
        }

        @Override
        public ArrayList<shared_list_properties> doInBackground(String... params)
        {
            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getSharedFilesByMe");
            request.addProperty("accessKey", OystorApp.accessKey);
            request.addProperty("shareId", shareId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);

            try {
                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                SoapObject responses = (SoapObject) envelope.bodyIn;
                SoapObject response = (SoapObject) responses .getProperty("response");
                String returnCode = response.getProperty("returnCode").toString();
                String exitMode = response.getProperty("exitMode").toString();
                if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                    SoapObject shareValues = (SoapObject) response.getProperty("returnValues");
                    int totalCount = shareValues.getPropertyCount();
                    for (int detailCount = 0; detailCount < totalCount; detailCount++) {
                        final SoapObject values = (SoapObject) shareValues.getProperty(detailCount);

                        subject = values.getProperty("subject").toString();
                        expiry_date = values.getProperty("expiry_date").toString();
                        shared_on = values.getProperty("shared_on").toString();
                        emails = values.getProperty("emails").toString();
                        name = values.getProperty("docs_name").toString();
                        ext = values.getProperty("docs_ext").toString();
                        sizes = values.getProperty("docs_sizes").toString();
                        id = values.getProperty("doc_id").toString();
                        rights = values.getProperty("right_id").toString();
                        hash = values.getProperty("hash").toString();
                        message =values.getProperty("message").toString();

                        shared_list_properties obj_shared_list_properties = new shared_list_properties();
                        obj_shared_list_properties.setSubject(subject);
                        obj_shared_list_properties.setExpiry_date((expiry_date));
                        obj_shared_list_properties.setSharedOn(shared_on);
                        obj_shared_list_properties.setEmails(emails);
                        obj_shared_list_properties.setDocs_name(name);
                        obj_shared_list_properties.setDocs_ext(ext);
                        obj_shared_list_properties.setDocs_sizes(sizes);
                        obj_shared_list_properties.setDoc_id(id);
                        obj_shared_list_properties.setMessage(message);

                        arr_obj_shared_list.add(obj_shared_list_properties);
                    }
                } else if (exitMode.equals("2")) {
                       OA.alert(response.getProperty("returnMessage").toString(), "Error", getActivity());
                } else if (exitMode.equals("1")) {
                      OA.errorResponse(response.getProperty("returnCode").toString(),response.getProperty("returnMessage").toString());
                      OA.logout(getActivity());
                } else {
                     OA.errorResponse(response.getProperty("returnCode").toString(), response.getProperty("returnMessage").toString());
                     OA.logout(getActivity());
                }
            } catch (ConnectException e) {
                 OA.internetProblem(getActivity());
            } catch (SocketException e) {
                 OA.internetProblem(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arr_obj_shared_list;
        }

        public void onPostExecute(ArrayList<shared_list_properties> details) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service
            System.out.println("entering on post" + details);
            System.out.println("Size" + details.size());

        }


    }
}


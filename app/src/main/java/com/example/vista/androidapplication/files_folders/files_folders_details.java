package com.example.vista.androidapplication.files_folders;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.Common;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class files_folders_details extends Fragment {

    public String tags = "", docid = "", Hash = "";
    public static String doctype = "";
    public Dialog tagdialog;
    public String downloadStarted = "0", tagResult;
    public Fragment fragment = null;
    OystorApp OA = new OystorApp();
    Common obj_common = new Common();
    TextView setTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.files_folder_details, container, false);

        try {
            // parameters receiving from fragments
            Bundle bundle = this.getArguments();

            TextView heading;
            ImageView viewImage;
            TextView viewText;

            docid = bundle.getString("docId");
            String name = bundle.getString("name");
            String size = bundle.getString("size");
            String type = bundle.getString("type");
            String path = bundle.getString("path");
            doctype = bundle.getString("doctype");

            if (doctype.equals("4")) {
                viewImage = (ImageView) rootView.findViewById(R.id.viewImage);
                viewImage.setImageResource(R.drawable.play_icon);
                viewText = (TextView) rootView.findViewById(R.id.viewText);
                viewText.setText("  Play");
            }
            if (doctype.equals("2")) {
                viewImage = (ImageView) rootView.findViewById(R.id.viewImage);
                viewImage.setImageResource(R.drawable.view_icon);
                viewText = (TextView) rootView.findViewById(R.id.viewText);
                viewText.setText("  View");
            } else if (doctype.equals("3")) {
                viewImage = (ImageView) rootView.findViewById(R.id.viewImage);
                viewImage.setImageResource(R.drawable.play_icon);
                viewText = (TextView) rootView.findViewById(R.id.viewText);
                viewText.setText("  Play");
            } else {
                viewImage = (ImageView) rootView.findViewById(R.id.viewImage);
                viewImage.setImageResource(R.drawable.view_icon);
                viewText = (TextView) rootView.findViewById(R.id.viewText);
                viewText.setText("  Open");
            }
            if (path.equals("anyType{}")) {
                path = " - ";
            }
            String uploadedDate = bundle.getString("uploadedDate");

            TextView view = (TextView) rootView.findViewById(R.id.name);
            view.setText(name);
            view = (TextView) rootView.findViewById(R.id.size);
            view.setText(size);
            view = (TextView) rootView.findViewById(R.id.type);
            view.setText(type);
            view = (TextView) rootView.findViewById(R.id.path);
            view.setText(path);
            view = (TextView) rootView.findViewById(R.id.uploaded);
            view.setText(uploadedDate);

            setTags = (TextView) rootView.findViewById(R.id.tags);
            setTags.setText(getTags());

            String x[] = name.split("\\.(?=[^\\.]+$)");
            String filefirstname = x[0], extension = x[1];

            String fileExtensionImage = "drawable/file_extension_" + extension.toLowerCase();
            int imageResource = getResources().getIdentifier(fileExtensionImage, null, getActivity().getPackageName());
            ImageView extimage = (ImageView) rootView.findViewById(R.id.extension);
            extimage.setImageResource(imageResource);


            //Share Image Icon Click Event
            ImageView imgShareView = (ImageView) rootView.findViewById(R.id.imgShare);
            imgShareView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new share_file();

                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();

                        //Passing docId parameters
                        Bundle args = new Bundle();
                        args.putString("docId", docid);
                        fragment.setArguments(args);

                        ft.add(R.id.frame_container, fragment);
                        ft.addToBackStack("fragBack");
                        ft.commit();
                    }
                }
            });

            //Share Icon Text Click Event
            TextView txtShareView = (TextView) rootView.findViewById(R.id.txtShare);
            txtShareView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new share_file();

                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();

                        //Passing docId parameters
                        Bundle args = new Bundle();
                        args.putString("docId", docid);
                        fragment.setArguments(args);

                        ft.add(R.id.frame_container, fragment);
                        ft.addToBackStack("fragBack");
                        ft.commit();

                    }
                }
            });


            //Tag Image Click Event
            ImageView imgTagView = (ImageView) rootView.findViewById(R.id.imgTag);
            imgTagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagdialog = new Dialog(getActivity());
                    tagdialog.setContentView(R.layout.addtags);
                    tagdialog.setTitle("Add Tags");
                    tagdialog.show();

                    EditText tagsbox = (EditText)tagdialog.findViewById(R.id.tagstextbox);
                    tagsbox.setText(tagResult.replace(",", ";"));
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    tagsbox.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // If the event is a key-down event on the "enter" button
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                // Perform action on key press
                                insertTags();
                                tagdialog.dismiss();
                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                return true;
                            }
                            return false;
                        }
                    });

                    Button button1 = (Button) tagdialog.findViewById(R.id.ok);
                    button1.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            insertTags();
                            tagdialog.dismiss();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    });
                    Button button2 = (Button) tagdialog.findViewById(R.id.cancel);
                    button2.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            tagdialog.dismiss();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    });

                }
            });


            //Tag Text Click Event
            TextView txtTagView = (TextView) rootView.findViewById(R.id.txtTag);
            txtTagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Tag Click");

                    tagdialog = new Dialog(getActivity());
                    tagdialog.setContentView(R.layout.addtags);
                    tagdialog.setTitle("Add Tags");
                    tagdialog.show();

                    EditText tagsbox = (EditText) tagdialog.findViewById(R.id.tagstextbox);
                    tagsbox.setText(tags.replace(",", ";"));

                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    tagsbox.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // If the event is a key-down event on the "enter" button
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                // Perform action on key press

                                insertTags();
                                tagdialog.dismiss();
                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                return true;
                            }
                            return false;
                        }
                    });

                    Button button1 = (Button) tagdialog.findViewById(R.id.ok);
                    button1.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            insertTags();
                            tagdialog.dismiss();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    });
                    Button button2 = (Button) tagdialog.findViewById(R.id.cancel);
                    button2.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            tagdialog.dismiss();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    });

                }
            });

            //View Image Click Event
            ImageView imgViewImage = (ImageView) rootView.findViewById(R.id.viewImage);
            imgViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("View Image Click");
                    // Method for Viewing the file
                    viewDownloadFile(true);
                }
            });

            //View Image Text Click Event
            TextView txtViewImage = (TextView) rootView.findViewById(R.id.viewText);
            txtViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("View Text  Click");
                    // Method for Viewing the file
                    viewDownloadFile(true);
                }
            });

            //Download Image Click Event
            ImageView imgDownload = (ImageView) rootView.findViewById(R.id.imgDownload);
            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Download Image Click");
                    // Method for Downloading the file
                    viewDownloadFile(false);

                }
            });

            //Download Image Text Click Event
            TextView txtDownload = (TextView) rootView.findViewById(R.id.txtDownload);
            txtDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Download Text Click");
                    // Method for Downloading the file
                    viewDownloadFile(false);
                }
            });
        } catch (Exception ex) {
            System.out.println("Exception Files and Folders Details" + ex);
        }

        return rootView;
    }

    // Method for downloading a file and viewing a file.
    public void viewDownloadFile(boolean isView) {
        if (!downloadStarted.equals("0")) {
            OA.alert("Already a download for this file started. Please wait until it finishes.", "Attention", getActivity());
        } else {
            String rootPath = "";
            boolean downloadFile, canView = false;
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
                File docfile = new File(docLocalPath);

                if (docfile.exists()) {
                    String localHash = "";
                    try {
                        InputStream is = new FileInputStream(docfile);
                        localHash = OA.getFileCheckSum(is);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (localHash.equals(Hash) && isView) {
                        downloadFile = false;
                        //viewDocument(docLocalPath);
                        obj_common.viewDocument(getActivity(), docLocalPath);
                    } else {
                        downloadFile = true;
                    }
                } else {
                    downloadFile = true;
                }

                if (downloadFile) {
                    StatFs stat = new StatFs(rootPath);
                    long blockSize = stat.getBlockSize();
                    long availableBlocks = stat.getAvailableBlocks();
                    long freeSpaceInBytes = availableBlocks * blockSize;

                    if (Double.parseDouble(obj_files_folders_properties.getSize()) <= freeSpaceInBytes) {
                        downloadStarted = "2";
                        final download_file task = new download_file(getActivity(), isView);
                        task.s3accesskey = obj_files_folders_properties.gets3AccessKey();
                        task.s3secretkey = obj_files_folders_properties.gets3SecretKey();
                        task.bucketname = obj_files_folders_properties.getbucketName();
                        task.fileName = obj_files_folders_properties.getfilename().toString();
                        task.fileLoc = obj_files_folders_properties.getfileLoc();
                        task.path = obj_files_folders_properties.getPath().toString();
                        task.size = obj_files_folders_properties.getSize();
                        task.rootpath = rootPath;
                        task.downloadFolder = OA.downloadFolder;
                        task.fileType = doctype;

                        try {
                             /* if getting result from async task directly dialog is not displaying */
                            //String result = (task.execute(getActivity())).get();
                            task.execute(getActivity());

                            //viewDocument(OystorApp.download_file_path);
                            downloadStarted = "0";
                        } catch (Exception ex) {
                            System.out.println("Error in calling download file" + ex);
                        }
                    } else {
                        OA.alert("Free Space is not available to download", "Error", getActivity());
                    }
                }
            } else {
                OA.alert("External Storage is not available to download", "Error", getActivity());
            }
        }
    }

    //Method for adding the Tag Values of the files.
    public void insertTags() {
        EditText tagsBox = (EditText) tagdialog.findViewById(R.id.tagstextbox);
        final String tagText = tagsBox.getText().toString();

        add_tags obj_add_tags = new add_tags(docid, tagText, getActivity());
        obj_add_tags.execute();

        setTags.setText(getTags());
    }


    //Method for Returing the Tag Values of the files.
    public String getTags() {
        try {
            get_tags obj_get_tags = new get_tags(docid, getActivity());
            tagResult = obj_get_tags.execute().get();

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return tagResult;
    }

}



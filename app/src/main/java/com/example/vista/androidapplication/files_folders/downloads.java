package com.example.vista.androidapplication.files_folders;

import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.vista.androidapplication.R;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.properties.files_folders_properties;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class downloads extends ListFragment {

    private OystorApp OA = new OystorApp();

    private static final String ITEM_KEY = "key";

    private static final String ITEM_IMAGE = "image";

    private static String ROOT = "/sdcard";

    public static final String START_PATH = "START_PATH";

    public static final String FORMAT_FILTER = "FORMAT_FILTER";

    public static final String RESULT_PATH = "RESULT_PATH";

    public static final String SELECTION_MODE = "SELECTION_MODE";
    private View recentView = null;

    public static final String CAN_SELECT_DIR = "CAN_SELECT_DIR";

    private static final String TAG = null;

    public static String CURRENT_LOC = "";

    private List<String> path = null;
    private TextView myPath;
    private EditText mFileName;
    private ArrayList<HashMap<String, Object>> mList;
    private Button selectButton;
    private Button newButton;

    private LinearLayout layoutSelect;
    private LinearLayout layoutCreate;
    private InputMethodManager inputManager;
    private String parentPath;
    private String currentPath = ROOT;

    private int selectionMode = SelectionMode.MODE_CREATE;

    private String[] formatFilter = null;

    private boolean canSelectDir = false;

    private File selectedFile;
    private HashMap<String, Integer> lastPositions = new HashMap<String, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("Entered  Downloads onCreateView");
        ROOT = Environment.getExternalStorageDirectory().getPath() + OA.downloadFolder;

        currentPath = ROOT;
        System.out.println("currentPath" + currentPath);
        View rootView = inflater.inflate(R.layout.downloads_main, container, false);

        if(Environment.getExternalStorageState().equals("mounted"))
        {
            File checkpath = new File(ROOT);
            if(!checkpath.exists())
            {
                checkpath.mkdir();
            }

            myPath = (TextView)rootView.findViewById(R.id.path);
            mFileName = (EditText)rootView.findViewById(R.id.fdEditTextFile);

            inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            selectButton = (Button)rootView.findViewById(R.id.fdButtonSelect);
            selectButton.setEnabled(false);
            selectButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (selectedFile != null) {
                       viewDocument(selectedFile.getPath());
                    }
                }
            });

            newButton = (Button)rootView.findViewById(R.id.fdButtonNew);
            newButton.setEnabled(false);
            newButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (selectedFile != null) {
                        //deleteDocument(selectedFile.getPath());
                        //refresh();
                    }
                }
            });

            selectionMode = getActivity().getIntent().getIntExtra(SELECTION_MODE,
                    SelectionMode.MODE_CREATE);

            formatFilter = getActivity().getIntent().getStringArrayExtra(FORMAT_FILTER);

            canSelectDir = getActivity().getIntent().getBooleanExtra(CAN_SELECT_DIR, false);

            if (selectionMode == SelectionMode.MODE_OPEN) {
                newButton.setEnabled(false);
            }

            layoutSelect = (LinearLayout)rootView.findViewById(R.id.fdLinearLayoutSelect);
            layoutCreate = (LinearLayout)rootView.findViewById(R.id.fdLinearLayoutCreate);
            layoutCreate.setVisibility(View.GONE);

            String startPath = getActivity().getIntent().getStringExtra(START_PATH);
            startPath = startPath != null ? startPath : ROOT;
            if (canSelectDir) {
                File file = new File(startPath);
                selectedFile = file;
                selectButton.setEnabled(true);
                newButton.setEnabled(true);
            }
            CURRENT_LOC = startPath;
            getDir(startPath);

            try {
                String openPath = getActivity().getIntent().getStringExtra("openPath");
                if(!openPath.equals("") && !openPath.equals(null))
                {
                    openPath = ROOT + "/" + openPath;
                    getDir(openPath);
                }
            } catch (Exception e)
            {
//                	e.printStackTrace();
            }
        }
        else
        {
            //setContentView(R.layout.empty_list);
            OA.alert("External Storage not available", "Error", getActivity());
        }

        return rootView;
    }

    private void getDir(String dirPath) {

        boolean useAutoSelection = dirPath.length() < currentPath.length();

        Integer position = lastPositions.get(parentPath);

        getDirImpl(dirPath);

        if (position != null && useAutoSelection) {
            getListView().setSelection(position);
        }
    }


    private void getDirImpl(final String dirPath) {

        currentPath = dirPath;

        final List<String> item = new ArrayList<String>();
        path = new ArrayList<String>();
        mList = new ArrayList<HashMap<String, Object>>();

        File f = new File(currentPath);
        File[] files = f.listFiles();
        if (files == null) {
            currentPath = ROOT;
            f = new File(currentPath);
            files = f.listFiles();
        }

        String test = Environment.getExternalStorageDirectory().getPath() + OA.downloadFolder;
        int start = test.length();
        int end = currentPath.length();
        String localPath = currentPath.substring(start,end);
        myPath.setText(getText(R.string.app_name) + ": " + localPath);

        if (!currentPath.equals(ROOT)) {

            //item.add(ROOT);
            //addItem(ROOT, R.drawable.folder);
            //path.add(ROOT);

            item.add("");
            addItem("", R.drawable.folder_back);
            path.add(f.getParent());
            parentPath = f.getParent();

        }

        TreeMap<String, String> dirsMap = new TreeMap<String, String>();
        TreeMap<String, String> dirsPathMap = new TreeMap<String, String>();
        TreeMap<String, String> filesMap = new TreeMap<String, String>();
        TreeMap<String, String> filesPathMap = new TreeMap<String, String>();
        for (File file : files) {
            if (file.isDirectory()) {
                String dirName = file.getName();
                dirsMap.put(dirName, dirName);
                dirsPathMap.put(dirName, file.getPath());
            } else {
                final String fileName = file.getName();
                final String fileNameLwr = fileName.toLowerCase();
                // se ha um filtro de formatos, utiliza-o
                if (formatFilter != null) {
                    boolean contains = false;
                    for (int i = 0; i < formatFilter.length - 1; i++) {
                        final String formatLwr = formatFilter[i].toLowerCase();
                        if (fileNameLwr.endsWith(formatLwr)) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        filesMap.put(fileName, fileName);
                        filesPathMap.put(fileName, file.getPath());
                    }
                    // senao, adiciona todos os arquivos
                } else {
                    filesMap.put(fileName, fileName);
                    filesPathMap.put(fileName, file.getPath());
                }
            }
        }
        item.addAll(dirsMap.tailMap("").values());
        item.addAll(filesMap.tailMap("").values());
        path.addAll(dirsPathMap.tailMap("").values());
        path.addAll(filesPathMap.tailMap("").values());

        SimpleAdapter fileList = new SimpleAdapter(getActivity(), mList,R.layout.downloads_row,
                new String[] { ITEM_KEY, ITEM_IMAGE }, new int[] {
                R.id.fdrowtext, R.id.fdrowimage });

        for (String dir : dirsMap.tailMap("").values()) {
            addItem(dir, R.drawable.folder);
        }

        for (String file : filesMap.tailMap("").values()) {
            addItem(file, R.drawable.file);
        }

        fileList.notifyDataSetChanged();

        setListAdapter(fileList);

    }

    private void addItem(String fileName, int imageId) {
        HashMap<String, Object> item = new HashMap<String, Object>();
        item.put(ITEM_KEY, fileName);
        item.put(ITEM_IMAGE, imageId);
        mList.add(item);
    }

    public void viewDocument(String paths)
    {
        try {
            File file = new File(paths);

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(paths));

                if(!mimeType.equals(""))
                {
                    intent.setDataAndType(path, mimeType);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    OA.alertToast("Unsupported Format to Open", getActivity());
                }
            }
        }
        catch (ActivityNotFoundException e) {
            OA.alertToast("No Application Available to View this File", getActivity());
        }
        catch(Exception e)
        {
            OA.alertToast("No Application Available to View this File", getActivity());
//            	Log.e(TAG, e.getMessage(), e);
        }
    }
}


class SelectionMode {
    public static final int MODE_CREATE = 0;

    public static final int MODE_OPEN = 1;
}


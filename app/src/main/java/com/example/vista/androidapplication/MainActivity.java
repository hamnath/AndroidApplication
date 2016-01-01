package com.example.vista.androidapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.vista.androidapplication.activities.activity_list;
import com.example.vista.androidapplication.adapter.NavDrawerListAdapter;
import com.example.vista.androidapplication.common.OystorApp;
import com.example.vista.androidapplication.contacts.contactlist;
import com.example.vista.androidapplication.files_folders.files_folder_list;
import com.example.vista.androidapplication.files_folders.files_folders_grid;
import com.example.vista.androidapplication.login.login_user;
import com.example.vista.androidapplication.model.NavDrawerItem;
import com.example.vista.androidapplication.shared.shared_file_list;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private OystorApp OA = new OystorApp();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public String ActivityCount ="";

    private Menu mMenu;
    private ProgressDialog dialog;
    private String action_Item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog= new ProgressDialog(this);

        mTitle = mDrawerTitle = getTitle();

        //Change Action Bar Colors.
        ActionBar ab = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#01A9DB"));
        ab.setBackgroundDrawable(colorDrawable);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

//        if(OA.loginAuthentication()) {
//            String accesskey = OystorApp.accessKey;
//            System.out.println("Login Authentication" + accesskey);
//            AsyncTaskRunner runner = new AsyncTaskRunner(accesskey);
//            runner.execute(accesskey);
//        }

        System.out.println("ActivityCount :::" + ActivityCount);
        // adding nav drawer items to array

        // files and folders
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Activites
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, ActivityCount));
        // Shared files
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Contacts
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // My account
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Logout
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);

                //getActionBar().setBackgroundDrawable(new ColorDrawable(0xFF160203));
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            System.out.println("displayView");
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(navMenuTitles[0] != getActionBar().getTitle()) {
            menu.findItem(R.id.action_view_as_grid).setVisible(false);
       }
        else {
            menu.findItem(R.id.action_view_as_grid).setVisible(true);
        }

        try {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        catch (Exception ex)
        {
            System.out.println("Welcome 3" + ex);
        }

        return true;
    }


    //Action bar Items Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar actions click
        switch (item.getItemId()) {

            case R.id.action_search:
                // search action
                return true;

            case R.id.action_sort:

               //Sort by options Generated
               action_Item= "Sort by";
               View menuItemView = findViewById(R.id.menu_overflow); // SAME ID AS MENU ID
               registerForContextMenu(menuItemView);
               openContextMenu(menuItemView);

               return true;

            case R.id.action_filter:
                //Action Filter options Generated
                action_Item= "Filter by";
                View menuItemView1 = findViewById(R.id.menu_overflow); // SAME ID AS MENU ID
                registerForContextMenu(menuItemView1);
                openContextMenu(menuItemView1);

                return true;

            case R.id.action_view_as_grid:

                //Action bar Icon changes for Grid View to List View
                Fragment fragment = null;
                if(item.getItemId() == R.id.action_view_as_grid && item.getTitle().equals("Grid View"))
                {
                    //Change Icon to List View and Title
                    item.setIcon(R.drawable.ic_action_view_as_list);
                    item.setTitle(R.string.action_view_as_list);
                    fragment = new files_folders_grid();
                }
                else {
                    //Change Icon to Grid View and Title
                    item.setIcon(R.drawable.ic_action_view_as_grid);
                    item.setTitle(R.string.action_view_as_grid);
                    fragment = new files_folder_list();
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack("fragBack").commit();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Context Menu creation and Operations are Done here   */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        System.out.println("onCreateContextMenu" + action_Item);

        if(action_Item == "Sort by" ) {
            menu.clear();
            menu.setHeaderTitle("Sort by");
            MenuInflater inflaters = this.getMenuInflater();
            inflaters.inflate(R.menu.sort_by_menu, menu);
        }
        else
        {
            menu.clear();
            menu.setHeaderTitle("Filter by");
            MenuInflater inflaters = this.getMenuInflater();
            inflaters.inflate(R.menu.filter_by_menu, menu);
        }
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        System.out.println("Main Activity onContextItemSelected");
//        //activity_list obj_activity_list;
//
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int listPosition = info.position;
//
//        switch (item.getItemId()) {
//            case R.id.sort_by_item_1:
//                System.out.println("sort_by_item_1" + item.getItemId() + " Title " + item.getTitle());
//                return true;
//            case R.id.sort_by_item_2:
//                System.out.println("sort_by_item_2" + item.getItemId() + " Title " + item.getTitle());
//                return true;
//            case R.id.sort_by_item_3:
//                System.out.println("sort_by_item_3" + item.getItemId() + " Title " + item.getTitle());
//                return true;
//
//             case R.id.activity_item_1:
//
//                System.out.println("clearActivity" + item.getItemId() + " Title " + item.getTitle());
//                activity_list obj_activity_list = new activity_list();
//                obj_activity_list.clearActivity(listPosition);
//                return true;
//
//            case R.id.activity_item_2:
//                System.out.println("clear All Activity" + item.getItemId() + " Title " + item.getTitle());
//                obj_activity_list = new activity_list();
//                obj_activity_list.clearAllActivity();
//
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    /*  Called when invalidateOptionsMenu() is triggered */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);

        System.out.println(" *** getTitle *** "+getActionBar().getTitle());

        menu.findItem(R.id.action_filter).setVisible(!drawerOpen);
        menu.findItem(R.id.action_sort).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /* Diplaying fragment view for selected nav drawer list item */

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                OystorApp.folderId = "0";
                fragment = new files_folder_list();
                break;
            case 1:
                fragment = new activity_list();
                break;
            case 2:
                fragment = new shared_file_list();
                break;
            case 3:
                fragment = new contactlist();
                break;
            case 4:
                fragment = new my_account();
                break;
            case 5:
                //fragment = new Logout();
                logout();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack("fragBack").commit();
            System.out.println("fragmentManager  getBackStackEntryCount " + fragmentManager.getBackStackEntryCount());
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /* When using the ActionBarDrawerToggle, you must call it during  onPostCreate() and onConfigurationChanged()... */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private static long back_pressed;
    @Override
    public void onBackPressed() {
        System.out.println("onBackPressed " + getFragmentManager().getBackStackEntryCount());

        if (getFragmentManager().findFragmentByTag("fragBack") != null) {

        } else {
            if (getFragmentManager().getBackStackEntryCount()-1 > 0) {
                System.out.println("Fragment BackStack ELSE");
                super.onBackPressed();
            } else {
                //System.out.println("Count 221" + getFragmentManager().getBackStackEntryCount());

                //getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                //System.out.println("Count 2" + getFragmentManager().getBackStackEntryCount());
                //super.onBackPressed();
                //this.finish();

                logout();

//                Intent nextScreen = new Intent(MainActivity.this, login_user.class);
//                nextScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                nextScreen.putExtra("EXIT", true);
//                startActivity(nextScreen);

//                if (back_pressed + 2000 > System.currentTimeMillis()) {
//                    System.out.println("Fragment BackStack IF");
//                    super.onBackPressed();
//                } else {
//                    Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
//                    back_pressed = System.currentTimeMillis();
//                }

//            if (getFragmentManager().getBackStackEntryCount() != 0) {
//                System.out.println("Fragment IF ");
//                Fragment frag = getFragmentManager().findFragmentByTag("fragBack");
//                System.out.println("Fragment BackStack" + frag);
//                FragmentTransaction transac = getFragmentManager().beginTransaction().remove(frag);
//                transac.commit();
//            }

            }
       }
    }

    public void logout()
    {
        mDrawerLayout.closeDrawer(mDrawerList);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Oystor");
        alert.setMessage("Are you sure want to logout? ");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                Intent nextScreen = new Intent(MainActivity.this, login_user.class);
                nextScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                nextScreen.putExtra("EXIT", true);
                startActivity(nextScreen);
                System.out.println("ASYNC" + "" + "TASK");
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alert.show();
    }


//    public void onBackPressed() {
//        System.out.println("SAMfad");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        System.out.println("assign++d");
//                        Intent intent=new Intent(MainActivity.this,login_user.class);
//                        startActivity(intent);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }

    /*public class AsyncTaskRunner extends AsyncTask<String, String, String> {

        public AsyncTaskRunner(String accesskey) {
            // TODO Auto-generated constructor stub
        }

        public void onPreExecute(){
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        public String doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("params" + params[0]);

            String accessKey = params[0];
            Object bitmap;

            SoapObject request = new SoapObject(OystorApp.NAMESPACE, "getActivitiesList");
            System.out.println("getActivitiesList params" + accessKey);
            request.addProperty("accessKey", accessKey);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //	envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(OystorApp.URL);
            String total = "0";
            int totalCount = 0;
            try {
                System.out.println(OystorApp.URL + "url");

                androidHttpTransport.call(OystorApp.SOAP_ACTION, envelope);
                SoapObject responseInner = (SoapObject) envelope.bodyIn;

                SoapObject response = (SoapObject) responseInner.getProperty("response");
                String returnCode = response.getProperty("returnCode").toString();

                if (response.getPropertyCount() > 0 && returnCode.equals("1")) {
                    SoapObject values = (SoapObject) response.getProperty("returnValues");
                    totalCount = values.getPropertyCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("get Message" + e);
            }

            return String.valueOf(totalCount);
        }

        public void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            // In this example it is the return value from the web service
            System.out.println("Entered");
            System.out.println("On post result" + result);
            ActivityCount = result;

            dialog.dismiss();
        }
    }   */
}

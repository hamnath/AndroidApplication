package com.example.vista.androidapplication;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vista.androidapplication.login.login_user;

public class Logout extends Fragment {
	
	public Logout(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Builder alert = new Builder(getActivity());
        alert.setTitle("Oystor");
        alert.setMessage("Are you sure want to logout? ");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent nextScreen = new Intent(getActivity(), login_user.class);
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


        View rootView = inflater.inflate(R.layout.logout, container, false);
        return rootView;
    }
}

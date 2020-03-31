package com.example.ecodrive5;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class DashboardFragment extends Fragment {

    private Button StartARide;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        StartARide = view.findViewById(R.id.startrideF);
        StartARide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServiceOk()) {
                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    startActivity(i);
                    // Toast.makeText(getActivity(), "Welcome...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    public Boolean isServiceOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(getActivity(),"You can't make map request",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
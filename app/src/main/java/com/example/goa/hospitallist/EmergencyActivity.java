package com.example.goa.hospitallist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EnumMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmergencyActivity extends AppCompatActivity implements LocationResult{

    ImageButton btnGPSShowLocation;
    private MyLocation myLocation = null;
    private EditText message;
    private Button submit;
    TextView tvAddress;
    DatabaseReference rootRef, demoRef;


    private static final String[] INITIAL_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int INITIAL_REQUEST = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        myLocation = new MyLocation();

        btnGPSShowLocation = (ImageButton) findViewById(R.id.btnGPSShowLocation);
        message =(EditText)findViewById(R.id.message);
        submit =(Button)findViewById(R.id.sub) ;
        rootRef = FirebaseDatabase.getInstance().getReference();

        demoRef = rootRef.child("Emergency");





        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View arg0) {

              /*  if (!canAccessLocation() || !canAccessCoreLocation()) {
                    requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);

                } else*/ {
                    boolean networkPresent = myLocation.getLocation(EmergencyActivity.this, EmergencyActivity.this);
                    if (!networkPresent) {
                        showSettingsAlert();
                    }
                }

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg =message.getText().toString();

                String location = tvAddress.getText().toString();

                demoRef.push().setValue(msg);
                demoRef.push().setValue(location);


                Toast.makeText(getApplicationContext(), "Your Request  successfully added",
                        Toast.LENGTH_LONG).show();


            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case INITIAL_REQUEST:
                if (canAccessLocation() && canAccessCoreLocation()) {
                    boolean networkPresent = myLocation.getLocation(EmergencyActivity.this, this);
                    if (!networkPresent) {
                        showSettingsAlert();
                    }
                }

                break;


        }
    }

    private boolean canAccessLocation() {
        return (hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCoreLocation() {
        return (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean hasPermission(String perm) {

        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(EmergencyActivity.this, perm));
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                EmergencyActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        EmergencyActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void gotLocation(Location location) {

        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        final String result = "Latitude: " + location.getLatitude() +
                " Longitude: " + location.getLongitude();


        EmergencyActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                tvAddress.setText(result);
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getApplicationContext(), new GeocoderHandler());
            }
        });








    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            tvAddress.setText(locationAddress);
        }
    }





}

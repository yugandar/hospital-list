package com.example.goa.hospitallist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyActivity extends AppCompatActivity {



    DatabaseReference rootRef, demoRef;

    private TextView message;
    private Button submit;
    private ImageButton location;
    private String lat,lon;
    private EditText Emergeny;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);



        message = (TextView)findViewById(R.id.txt);
        submit =(Button)findViewById(R.id.submit);
        location=(ImageButton)findViewById(R.id.btn);
        Emergeny =(EditText)findViewById(R.id.edit) ;


        location.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("MissingPermission")
            public void onClick(View arg0) {
                // Acquire a reference to the system Location Manager
                LocationManager locationManager =
                        (LocationManager) EmergencyActivity.this.getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        lat = Double.toString(location.getLatitude());
                        lon = Double.toString(location.getLongitude());
                        message.setText("Your Location is:" + lat + "--" + lon);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    public void onProviderEnabled(String provider) {}
                    public void onProviderDisabled(String provider) {}
                };
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        });



        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        demoRef = rootRef.child("Emergency");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = Emergeny.getText().toString();
                String Location = message.getText().toString();

                //push creates a unique id in database
                demoRef.push().setValue(data);
                demoRef.push().setValue(location);
                Toast.makeText(getApplicationContext(), "Your Review successfully added",
                        Toast.LENGTH_LONG).show();


            }
        });
    }
}

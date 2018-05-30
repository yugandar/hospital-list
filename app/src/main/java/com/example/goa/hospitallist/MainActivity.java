package com.example.goa.hospitallist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.goa.hospitallist.adapter.CustomHospitalListViewAdapter;
import com.example.goa.hospitallist.beans.HospitalItem;
import com.example.goa.hospitallist.constants.DoctorAppPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

        private Context mContext;
        private ListView mHospitalItemListView;
        private RequestQueue requestQueue;

        private ArrayList<HospitalItem> mHospitalItems;

        private CustomHospitalListViewAdapter mAdapter;
        private final static String LOG_TAG = MainActivity.class.getSimpleName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

             mHospitalItems = new ArrayList<HospitalItem>();
             mContext = this;
             mHospitalItemListView = (ListView) findViewById(R.id.hospitalListView);
//            mAdapter = new CustomHospitalListViewAdapter(this,mHospitalItems);
//            mHospitalItemListView.setAdapter(mAdapter);
            requestQueue = Volley.newRequestQueue(getApplicationContext());

            putLogTagForLocaitonInfo();

            fetchHospitals();

        }






    private void fetchHospitals() {
        String url = "https://doctorapp-f9b65.firebaseio.com/.json";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("records");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject ob = array.getJSONObject(i);
                                HospitalItem hosp = new HospitalItem();
                                hosp.setHospitalName(ob.getString("hospitalname"));
                                hosp.setHospitalAddress(ob.get("address_first_line").toString() + ", " + ob.get("district").toString());
                                hosp.setHospitalEmail(ob.get("hospitalprimaryemailid").toString());
                                hosp.setHospitalCategory(ob.getString("hospital_category"));
                                hosp.setHospitalWebsite(ob.getString("website"));

                                String tel = ob.getString("telephone");
                                String telephone = "";
                                for (int j = 0; j < tel.length(); j++) {
                                    if (tel.charAt(j) == ',')
                                        break;
                                    telephone += tel.charAt(j);
                                }
                                hosp.setHospitalTelephone(telephone);
                                hosp.setPincode(ob.getString("pincode"));
                                hosp.setSystemsOfMedicine(ob.getString("systems_of_medicine"));
                                hosp.setLatitude(ob.getString("googlemapcorridinate_lati"));
                                hosp.setLongitude(ob.getString("googlemapcorridinate_longi"));
                                mHospitalItems.add(hosp);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        mAdapter = new CustomHospitalListViewAdapter(getApplicationContext(), mHospitalItems);
                        mHospitalItemListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                 }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
        }
    });

    // Adding request to request queue
        requestQueue.add(req);
    }


    private void putLogTagForLocaitonInfo() {

        Log.v(LOG_TAG, "TestToast: " + PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getFloat(DoctorAppPreferences.LATITUDE, 0.0F) + "," + PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getFloat(DoctorAppPreferences.LONGITUDE, 0.0F) + " " + PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString(DoctorAppPreferences.DISTRICT, DoctorAppPreferences.DEFAULT_TEXT_WHEN_NO_DISTRICT_IS_AVAILABLE));
    }
}

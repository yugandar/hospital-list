package com.example.goa.hospitallist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);


        Button btn1,bnt2,btn3;

        btn1 = (Button) findViewById(R.id.pick_location_id);

        bnt2 = (Button) findViewById(R.id.enable_gps_id);

        btn3 =(Button) findViewById(R.id.emergency);


        btn1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(GridActivity.this,MainActivity.class);

                        startActivity(intent);


                    }
                }
        );


        bnt2.setOnClickListener(


                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(GridActivity.this,ReviewActivity.class);
                        startActivity(intent);
                    }
                }
        );

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(GridActivity.this,EmergencyActivity.class);

                startActivity(intent);


            }
        });


    }
}

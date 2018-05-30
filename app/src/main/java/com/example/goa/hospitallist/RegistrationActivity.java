package com.example.goa.hospitallist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends   AppCompatActivity {

    Button register;
    TextView t2;
    EditText username, password;
    ProgressBar progressBa;
    Spinner spinner;
    private FirebaseAuth mAuth;
    ArrayAdapter<CharSequence> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Date currentTime = Calendar.getInstance().getTime();
        Log.i("TIME", "onCreate: " + currentTime);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").push();

        mAuth = FirebaseAuth.getInstance();
        progressBa = (ProgressBar) findViewById(R.id.progressBar);
        username = (EditText) findViewById(R.id.editText4);
        password = (EditText) findViewById(R.id.editText5);
        register = (Button) findViewById(R.id.button2);
        t2 = (TextView) findViewById(R.id.textView2);

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                adddata();

            }
        });

    }

    private void adddata() {
        String user = username.getText().toString().trim();

        if (!TextUtils.isEmpty(user)) {
            String id = databaseReference.push().getKey();

            pojo pojo = new pojo(id, user);

            databaseReference.setValue(pojo);
            //databaseReference.setValue(user);


        } else {
            Toast.makeText(this, "Enter valid name", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    private void registerUser() {

        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            username.setError("EMail is required");
            username.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            username.setError("Please enter a valid EMail");
            username.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            password.setError("Minimum length of password must be 6");
            password.requestFocus();
            return;
        }
        progressBa.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBa.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(RegistrationActivity.this, GridActivity.class);
                    Toast.makeText(RegistrationActivity.this, "User Registration is Successfull", Toast.LENGTH_SHORT).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegistrationActivity.this, "You are already Registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}



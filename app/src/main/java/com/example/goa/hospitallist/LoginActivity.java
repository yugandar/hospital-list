package com.example.goa.hospitallist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;

public class LoginActivity  extends  AppCompatActivity {
    private final static String TAG = LoginActivity.class.getSimpleName();


    private static final int RC_SIGN_IN = 2;
    Button b1, b4;
    EditText e1, e2;
    FirebaseAuth mAuth;
    TextView t1;
    ProgressBar progressBa1, progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBa1 = (ProgressBar) findViewById(R.id.progressBar1);
        b1 = (Button) findViewById(R.id.button);
        e1 = (EditText) findViewById(R.id.emailID);
        e2 = (EditText) findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();
        t1 = (TextView) findViewById(R.id.textView);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();





        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });


    }

    private void userlogin() {
        String user1 = e1.getText().toString().trim();
        String pass1 = e2.getText().toString().trim();
        if (user1.isEmpty()) {
            e1.setError("EMail is required");
            e1.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user1).matches()) {
            e1.setError("Please enter a valid EMail");
            e1.requestFocus();
            return;
        }
        if (pass1.isEmpty()) {


            e2.setError("Password is required");
            e2.requestFocus();
            return;
        }
        if (pass1.length() < 6) {
            e2.setError("Minimum length of password must be 6");
            e2.requestFocus();
            return;
        }
        progressBa1.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(user1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBa1.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    Intent intent = new Intent(LoginActivity.this, GridActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "User Logged in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        }


    }

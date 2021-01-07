package com.example.slot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slot.loginandsignin.SignIn_Activity;
import com.example.slot.loginandsignin.SignUpActivity;

public class MainActivity extends AppCompatActivity {
    private Button SignUp;
    private Button SignIn;
    InternetBroadcastReceiver ibr=new InternetBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignIn=findViewById(R.id.button_login);
        SignUp= findViewById(R.id.button_register);
        SignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, SignIn_Activity.class));
                finish();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                finish();
            }
            });
        }
        @Override
        protected void onStart() {
            super.onStart();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(ibr, filter);
        }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(ibr);
    }

}
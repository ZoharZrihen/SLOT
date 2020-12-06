package com.example.slot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LecturerMainActivity extends AppCompatActivity {
    private Button set, show, logOut;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_main);
        // Set button
        set = findViewById(R.id.set_appointment_professor);
        show = findViewById(R.id.show_appointment_professor);
        logOut = findViewById(R.id.sign_out_professor_main);
        auth = FirebaseAuth.getInstance();

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LecturerMainActivity.this, setAppointmentProfessorActivity.class));
                finish();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LecturerMainActivity.this, showAppointmentProfessorActivity.class));
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(LecturerMainActivity.this, MainActivity.class));
                finish();
            }
        });










    }
}
package com.example.slot.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.slot.R;
import com.example.slot.utilclasses.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class setAppointmentStudentActivity extends AppCompatActivity {
    private Spinner spinner_courses;
    private Spinner spinner_dates;
    private Button backToStudentMain;

    Map<String, Appointment> available_courses = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_student);

        spinner_courses = findViewById(R.id.spinner_courses);

        backToStudentMain = findViewById(R.id.go_back);
        backToStudentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setAppointmentStudentActivity.this, StudentMainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Appointments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                available_courses.clear();
                for (DataSnapshot data_snapshot : snapshot.getChildren()){
                    available_courses.put(data_snapshot.getKey(), data_snapshot.getValue(Appointment.class));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                available_courses.keySet().toArray());
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner_courses.setAdapter(ad);
        spinner_courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] temp = (String[]) available_courses.keySet().toArray();

                Toast.makeText(getApplicationContext(),
                        temp[position],
                        Toast.LENGTH_LONG)
                        .show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
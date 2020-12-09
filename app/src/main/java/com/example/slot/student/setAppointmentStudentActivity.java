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
import android.widget.TextView;
import android.widget.Toast;

import com.example.slot.R;
import com.example.slot.utilclasses.Appointment;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class setAppointmentStudentActivity extends AppCompatActivity {
    private Spinner spinner_courses;
    private Spinner spinner_dates;
    private Button  backToStudenMain;

    DatabaseReference rootRef, appointmentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Map<String, String> meetings_info = new HashMap<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_student);

        spinner_courses = findViewById(R.id.spinner_courses);

        backToStudenMain = findViewById(R.id.go_back);
        backToStudenMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setAppointmentStudentActivity.this, StudentMainActivity.class);
                startActivity(intent);
            }
        });
        //Database reference pointing to the root of the database.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("appointments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data_snapshot : snapshot.getChildren()){
                    String name_of_meeting = data_snapshot.getKey();
                    String all_info = data_snapshot.getValue().toString();
                    meetings_info.put(name_of_meeting, all_info);
                    System.out.println("\n\n\n\n"+ meetings_info.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        System.out.println("\n\n\n\n"+ meetings_info.toString());
        int numberCourses = meetings_info.keySet().size();
        String[] courses_names = new String[numberCourses];
        Iterator<String> iter = meetings_info.keySet().iterator();
        for(int i = 0 ; i < numberCourses; i++){
            courses_names[i] = iter.next();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,courses_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_courses.setAdapter(adapter);

        spinner_courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String course_name = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "בחרת: " + course_name,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


    }

}
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class setAppointmentStudentActivity extends AppCompatActivity  {
    private Spinner spinner_courses_names;
    private Spinner spinner_dates;
    private Button  backToStudenMain;
    Map<String, String> meetings_info = new HashMap<>();
    Map<String, Map<String, Object>> Meetings = new HashMap<>();

    DatabaseReference rootRef, appointmentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //<Name of the course, all the info about it>
        // < Introduction to Java, {time=12:00, slot = ......}>

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_student);
        spinner_dates = findViewById(R.id.spinner_dates);
        spinner_courses_names = findViewById(R.id.spinner_courses);
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
        //Database reference to 'appointments'
        DatabaseReference ref = database.getReference("appointments");
        // Retrieving the information from Firebase and loading it into
        // the hash map 'meetings_info'
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data_snapshot : snapshot.getChildren()){
//                    String name_of_meeting = data_snapshot.getKey();
//                    String all_info = data_snapshot.getValue().toString();
//                    meetings_info.put(name_of_meeting, all_info);
//                    spinner_courses_init(meetings_info);

                    String name_course = data_snapshot.getKey();
                    Meetings.put( name_course,  (Map<String, Object>)data_snapshot.getValue());
                    spinner_courses_init(Meetings);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    private void spinner_courses_init(Map <String,Map<String,Object>>meetings_info2){
        ArrayList<String> courses_names = new ArrayList<>(meetings_info2.keySet());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,courses_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_courses_names.setAdapter(adapter);
        spinner_courses_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_by_user = spinner_courses_names.getSelectedItem().toString();
//                String selected_by_user2 = parent.getItemAtPosition(position).toString();
                setSpinner_dates_init(meetings_info2, selected_by_user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinner_dates_init(Map <String,Map<String,Object>>meetings_info2 , String chosen_course){
        Map<String, Object> course_info = meetings_info2.get(chosen_course);
        String _date = (String)course_info.get("date");
        HashMap<String, Boolean> _slots = (HashMap<String, Boolean>) course_info.get("slots");
        ArrayList<String> available_slots = new ArrayList<>();
        //Checking for available slots
        for( String slot : _slots.keySet()){
            if (_slots.get(slot) == true){
                String statement = _date + " - " + slot;
                available_slots.add(statement);
            }
        }

        // If there are no available slots
        if( available_slots.size() == 0){
            //Too full, display a message to user.
            available_slots.add("הכל תפוס");
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,available_slots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dates.setAdapter(adapter);
        spinner_dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String slot_selected_by_user = spinner_dates.getSelectedItem().toString();
//                String selected_by_user2 = parent.getItemAtPosition(position).toString();
                // TODO Set the slot as busy on DB
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }



}
package com.example.slot.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Map;

public class setAppointmentStudentActivity extends AppCompatActivity  {
    private Spinner spinner_courses_names;
    private Spinner spinner_dates;
    private Button  backToStudenMain;
    private Button setTheAppointment;

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
        setTheAppointment = findViewById(R.id.set_specific_appointment_student);

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
        ArrayList<String> courses_names = new ArrayList<>();
        courses_names.add("בחר קורס");
        courses_names.addAll(meetings_info2.keySet());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,courses_names){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_courses_names.setAdapter(adapter);
        spinner_courses_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0 ){
                    String selected_by_user = spinner_courses_names.getSelectedItem().toString();

                    setSpinner_dates_init(meetings_info2, selected_by_user);
                }
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
        available_slots.add("בחר SLOT");
        for( String slot : _slots.keySet()){
            try {
                if(_slots.get(slot) == true){
                    String statement = _date + " - " + slot;
                    available_slots.add(statement);
                }
            }catch(Exception e){

            }
        }

        // If there are no available slots
        if( available_slots.size() == 0){
            //Too full, display a message to user.
            Toast.makeText(this, "אין מקום !", Toast.LENGTH_LONG);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,available_slots){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dates.setAdapter(adapter);
        spinner_dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String slot_selected_by_user = spinner_dates.getSelectedItem().toString();
//
                if (position > 0){

                    String chosen_time = slot_selected_by_user.substring(slot_selected_by_user.indexOf("- ") +2);
                    HashMap <String, Boolean> time_table = (HashMap<String, Boolean>) meetings_info2.get(chosen_course).get("slots");
                    time_table.put(chosen_course, false);

                    setTheAppointment.setVisibility(View.VISIBLE);
                    setTheAppointment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uid=FirebaseAuth.getInstance().getUid();
                            UpdateUser(chosen_course,chosen_time,uid);

                        }
                    });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }
    //This func adds the name of the user and updating appointments list of the user.(zohar)
    private void UpdateUser(String chosen_course,String chosen_time,String uid){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("StudentUser").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user = snapshot.getValue(User.class);
               String StudentID= user.getUserID();
               String studentName= user.getName();
                FirebaseDatabase.getInstance().getReference().child("appointments").child(chosen_course).child("slots").child(chosen_time)
                        .child("name").setValue(studentName);
                FirebaseDatabase.getInstance().getReference().child("StudentUser").child(StudentID)
                        .child("Appointments").child(chosen_course).setValue(chosen_time);


//

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}
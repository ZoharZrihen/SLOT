package com.example.slot.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.slot.InternetBroadcastReceiver;
import com.example.slot.MainActivity;
import com.example.slot.R;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class showAppointmentProfessorActivity extends AppCompatActivity {
    private TextView current_user;
    private Button back;
    private Map<String,Map<String,Object>> myAppointments;
    private Spinner spinnerAppointments;
    private ListView slots;
    private FirebaseAuth auth;
    InternetBroadcastReceiver ibr=new InternetBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment_professor);
        slots=(ListView)findViewById(R.id.appointment_time_professor);
        auth=FirebaseAuth.getInstance();
        spinnerAppointments=findViewById(R.id.spinner_courses_professor);
        back=findViewById(R.id.backFromProfessorShow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(showAppointmentProfessorActivity.this,ProfessorMainActivity.class));
                finish();
            }
        });

        current_user = findViewById(R.id.hello_professor_show);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LecturerUser").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User Professor = snapshot.getValue(User.class);
                current_user.setText( Professor.getName()+", אלו הפגישות שהגדרת:" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Database reference to 'appointments'
        DatabaseReference ref2 = database.getReference("appointments");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override  //Sorting into myAppointments only the relevant meetings for the lecturer(working good)
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myAppointments=new HashMap<>();
                String id=FirebaseAuth.getInstance().getUid();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String course=ds.getKey();
                    Map<String,Object>tempMap=(Map<String,Object>)ds.getValue();
                    if(tempMap.get("LecturerID").toString().equals(id)){
                        myAppointments.put(course,tempMap);
                    }
                }
                OrderAppointmentsToShow(myAppointments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    private void OrderAppointmentsToShow(Map<String,Map<String,Object>> appointments){
        ArrayList<String> courses=new ArrayList<>();
        courses.add("בחר פגישה:");
        courses.addAll(appointments.keySet());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,courses){
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
        spinnerAppointments.setAdapter(adapter);
        spinnerAppointments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0 ){
                    String selected_by_user = spinnerAppointments.getSelectedItem().toString();
                        setListView((Map<String, Object>) myAppointments.get(selected_by_user).get("slots"));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListView(Map<String,Object> appointmentsTimes){

        ArrayList<String> times=new ArrayList<>();
        for(String time : appointmentsTimes.keySet()){
                String name=appointmentsTimes.get(time).toString();
                if(name.contains("true")){
                    times.add(time+" פנוי ");
                }else{
                    String studentName=name.substring(name.indexOf("=")+1,name.length()-1);
                    times.add(time+" "+studentName);
                }


        }
        Collections.sort(times);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,times);
        slots.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_logout){
            auth.signOut();
            startActivity(new Intent(showAppointmentProfessorActivity.this, MainActivity.class));
            finish();
        }else if(item.getItemId()==R.id.menu_backToMain){
            startActivity(new Intent(showAppointmentProfessorActivity.this, ProfessorMainActivity.class));
            finish();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    }

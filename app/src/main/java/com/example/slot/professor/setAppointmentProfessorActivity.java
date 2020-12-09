package com.example.slot.professor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.slot.utilclasses.Appointment;
import com.example.slot.R;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class setAppointmentProfessorActivity extends AppCompatActivity {
    private Button setDate;
    private Button startTime, endTime;
    private Button back;
    private Button set;
    private Calendar calendar;
    private DatePickerDialog dpd;
    private TimePickerDialog Stpd, Etpd;
    private int startHour, startMinute, endHour, endMinute,Interval;
    private int day,month,year;
    private String course;
    private EditText courseName;
    private EditText interval;
    private FirebaseAuth auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_professor);
        courseName=findViewById(R.id.Coursename);
        back=findViewById(R.id.backfromsetproffesor);
        setDate=findViewById(R.id.pickdate);
        startTime=findViewById(R.id.pickstarttime);
        endTime=findViewById(R.id.pickendtime);
        interval=findViewById(R.id.interval);
        set=findViewById(R.id.setmeetingProffesor);


//        String course= courseName.getText().toString();
        //int Interval=Integer.parseInt(interval.getText().toString());
        auth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("LecturerUser").child(auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue(User.class));
                 user = dataSnapshot.getValue(User.class);
                System.out.println(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            /*
            * Save to DB
            *
            * */
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Appointment appointment= new Appointment( startHour, startMinute, endHour, endMinute, Interval, day, month, year);
                System.out.println(appointment.toMap());
                String course = courseName.getText().toString();
                String key = course + "-" + user.getName() + "-" +appointment.getDate();

                Map<String, Object> dataMap = appointment.toMap();
                FirebaseDatabase.getInstance().getReference().child("appointments").child(key).setValue(dataMap); //putting appointments in the DB
//
//                DatabaseReference mDbRef = database.getReference();
//                Map<String, Object> updates = new HashMap<>();
//                updates.put("/appointments/" ,dataMap);
//                mDbRef.updateChildren(updates);
////
//
//
//
//                DatabaseReference ref = database.getReference("appointments");
//                ref.child(appointmentKey).updateChildren(dataMap);

                startActivity(new Intent(setAppointmentProfessorActivity.this, ProfessorMainActivity.class));
                finish();
//                ref.updateChildren(dataMap);

 /*                Interval=Integer.parseInt(interval.getText().toString());
                 course= courseName.getText().toString();
                 Appointment appointment= new Appointment( startHour, startMinute, endHour, endMinute,Interval, day,month,year);
                 String appointmentID=course + "- " +user.getName() + "- " +day+"-"+month+"-"+year;
                 FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointmentID).setValue(appointment); //putting appointments in the DB
                 FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointmentID).child("slotsMap").setValue("appointment.getSlot()"); //putting appointments in the DB
                 Toast.makeText(setAppointmentProfessorActivity.this,"שעת קבלה הוגדרה בהצלחה",Toast.LENGTH_LONG).show();
                 startActivity(new Intent(setAppointmentProfessorActivity.this, ProfessorMainActivity.class));
                 finish();*/
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(setAppointmentProfessorActivity.this, ProfessorMainActivity.class));
                finish();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                startHour=calendar.get(Calendar.HOUR_OF_DAY);
                startMinute=calendar.get(Calendar.MINUTE);
                Stpd= new TimePickerDialog(setAppointmentProfessorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(setAppointmentProfessorActivity.this, "שעת התחלה: "+hourOfDay+":"+minute,Toast.LENGTH_LONG).show();
                    }
                },startHour,startMinute,true);
                Stpd.show();
            }

        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                endHour=calendar.get(Calendar.HOUR_OF_DAY);
                endMinute=calendar.get(Calendar.MINUTE);
                Stpd= new TimePickerDialog(setAppointmentProfessorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(setAppointmentProfessorActivity.this, "שעת סיום: "+hourOfDay+":"+minute,Toast.LENGTH_LONG).show();
                    }
                },endHour,endMinute,true);
                Stpd.show();
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();

                day=calendar.get(Calendar.DAY_OF_MONTH);
                month=calendar.get(Calendar.MONTH);
                year=calendar.get(Calendar.YEAR);
                dpd=new DatePickerDialog(setAppointmentProfessorActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        Toast.makeText(setAppointmentProfessorActivity.this, "תאריך שהוגדר: "+dayOfMonth+"/"+month+"/"+year,Toast.LENGTH_LONG).show();
                    }
                },year,month,day);
                System.out.println();
                dpd.show();
            }
        });

//        Appointment appointment= new Appointment( startHour, startMinute, endHour, endMinute,Interval, day,month,year);
//        String appointmentID=course + "- " +user.getName() + "- " +day+"/"+month+"/"+year;
//        FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointmentID).setValue(appointment); //putting appointments in the DB


    }


}
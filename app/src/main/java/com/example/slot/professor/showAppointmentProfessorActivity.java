package com.example.slot.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.slot.R;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class showAppointmentProfessorActivity extends AppCompatActivity {

    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();



    User user;
    Map<String, String> appointments = new HashMap<>();


    //    Display name
    private TextView current_user;
    private Spinner spinner_appointment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment_professor);


        current_user = findViewById(R.id.professorNameShowAppointment);
        spinner_appointment = findViewById(R.id.spinner_appointment);

        DatabaseReference ref = database.getReference().child("LecturerUser").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                current_user.setText( " ברוך הבא "+ user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query query = ref.child("appointments").orderByChild("LectureID").equalTo();
        // Dean: getting the professor name from DB and presenting it on the screen.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data_snapshot : snapshot.getChildren()) {
                    String name_course = data_snapshot.getKey();
                    appointments.put(name_course, data_snapshot.getValue());
                    System.out.println(appointments);
//                    spinner_courses_init(Meetings);
                }
            }


                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }


            });
        }

}
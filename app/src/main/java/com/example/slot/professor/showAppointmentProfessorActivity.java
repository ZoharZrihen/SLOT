package com.example.slot.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.slot.R;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class showAppointmentProfessorActivity extends AppCompatActivity {
    private TextView current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment_professor);

// Dean: getting the professor name from DB and presenting it on the screen.
        current_user = findViewById(R.id.professorNameShowAppointment);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LecturerUser").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User Professor = snapshot.getValue(User.class);
                current_user.setText( " שלום "+ Professor.getName() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
package com.example.slot.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.slot.R;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class showAppointmentStudentActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_appointment_student);
    DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("LecturerUser")
                                .child(FirebaseAuth.getInstance().getUid());
    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        User Student = snapshot.getValue(User.class);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}

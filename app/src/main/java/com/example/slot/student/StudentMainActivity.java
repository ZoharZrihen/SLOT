package com.example.slot.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

import com.example.slot.MainActivity;
import com.example.slot.R;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentMainActivity extends AppCompatActivity {
    private Button set, show, logout;
    private TextView hello;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        hello=findViewById(R.id.welcome_student);
        set = findViewById(R.id.set_appointment_student);
        show = findViewById(R.id.show_appointment_student);
        logout = findViewById(R.id.sign_out_student_main);
        auth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("StudentUser").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User Student = snapshot.getValue(User.class);
                hello.setText( " ברוך הבא "+ Student.getName() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentMainActivity.this, setAppointmentStudentActivity.class ));
                finish();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentMainActivity.this, showAppointmentStudentActivity.class ));
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(StudentMainActivity.this, MainActivity.class));
                finish();
            }
        });

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
            startActivity(new Intent(StudentMainActivity.this, MainActivity.class));
            finish();
        }else if(item.getItemId()==R.id.menu_backToMain){
            Toast.makeText(this,"הנך במסך הראשי",Toast.LENGTH_LONG).show();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

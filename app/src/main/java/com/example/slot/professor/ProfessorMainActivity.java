package com.example.slot.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slot.InternetBroadcastReceiver;
import com.example.slot.MainActivity;
import com.example.slot.R;
import com.example.slot.student.StudentMainActivity;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfessorMainActivity extends AppCompatActivity {
    private Button set, show, logOut;
    private FirebaseAuth auth;
    private TextView hello;
    InternetBroadcastReceiver ibr=new InternetBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_main);
        // Set button
        set = findViewById(R.id.set_appointment_professor);
        hello = findViewById(R.id.hello_professor_main);
        show = findViewById(R.id.show_appointment_professor);
        logOut = findViewById(R.id.sign_out_professor_main);
        auth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LecturerUser").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User Professor = snapshot.getValue(User.class);
                hello.setText( " ברוך הבא "+ Professor.getName() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfessorMainActivity.this, setAppointmentProfessorActivity.class));
                finish();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfessorMainActivity.this, showAppointmentProfessorActivity.class));
//                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(ProfessorMainActivity.this, MainActivity.class));
                finish();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_logout){
            auth.signOut();
            startActivity(new Intent(ProfessorMainActivity.this, MainActivity.class));
            finish();
        }else if(item.getItemId()==R.id.menu_backToMain){
            Toast.makeText(this,"הנך במסך הראשי",Toast.LENGTH_LONG).show();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}


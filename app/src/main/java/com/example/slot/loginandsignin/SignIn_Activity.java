package com.example.slot.loginandsignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slot.professor.ProfessorMainActivity;
import com.example.slot.MainActivity;
import com.example.slot.R;
import com.example.slot.student.StudentMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn_Activity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button back;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);

        email= findViewById(R.id.email_signIn);
        password=findViewById(R.id.password_signIn);
        login= findViewById(R.id.signin);
        back=findViewById(R.id.returnfromsignin);
        auth= FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                loginUser(txt_email,txt_password);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn_Activity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task ) {
                if(task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    String UserID = user.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentUser").child(UserID);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                startActivity(new Intent(SignIn_Activity.this, StudentMainActivity.class));
                            } else {
                                startActivity(new Intent(SignIn_Activity.this, ProfessorMainActivity.class));
                            }
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(SignIn_Activity.this, "התחבר/ת בהצלחה!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignIn_Activity.this, "פרטי התחברות שגויים!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
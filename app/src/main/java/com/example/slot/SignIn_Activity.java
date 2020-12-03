package com.example.slot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn_Activity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);

        email= findViewById(R.id.email_signIn);
        password=findViewById(R.id.password_signIn);
        login= findViewById(R.id.signin);

        auth= FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=email.getText().toString();
                loginUser(txt_email,txt_password);
            }
        });
    }

    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignIn_Activity.this, "התחבר/ת בהצלחה!",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(SignIn_Activity.this, ));  ***NEED TO CONTINUE FROM THE LOGIN SCREEN.
                //finish();
            }
        });
    }
}
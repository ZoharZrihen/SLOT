package com.example.slot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static java.net.URLEncoder.encode;

public class SignUpActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText permission;
    private EditText name;
    private Button register;
    private Button back;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.signup);
        permission=findViewById(R.id.permission);
        name=findViewById(R.id.name);
        back=findViewById(R.id.returnfromsignup);

        auth=FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                String txt_permission=permission.getText().toString();
                String txt_name=name.getText().toString();

                if(TextUtils.isEmpty(txt_email )|| TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_permission) || TextUtils.isEmpty(txt_name)){
                    Toast.makeText(SignUpActivity.this,"נא להזין פרטים!",Toast.LENGTH_SHORT).show();
                } else if(txt_password.length()<6){
                    Toast.makeText(SignUpActivity.this,"סיסמה חייבת להיות בת לפחות 6 תווים",Toast.LENGTH_SHORT).show();
                }else if(txt_permission.equals( "מרצה") || txt_permission.equals("סטודנט")){

                    registerUser(txt_email,txt_password,txt_permission,txt_name);
                }
                else{
                    Toast.makeText(SignUpActivity.this,"נא להזין מרצה/סטודנט!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String email, String password,String permission, String name) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User NewUser= new User(email, auth.getUid(),name, permission);
                    Toast.makeText(SignUpActivity.this,"הרשמה בוצעה בהצלחה!",Toast.LENGTH_SHORT).show();
                    if(permission.equals("מרצה")) {
                        //FirebaseDatabase.getInstance().getReference().child("LecturerUser").child(auth.getUid()).setValue(email);
                        FirebaseDatabase.getInstance().getReference().child("LecturerUser").child(auth.getUid()).setValue(NewUser);
                    }else{
                      //  FirebaseDatabase.getInstance().getReference().child("StudentUser").child(auth.getUid()).setValue(email);
                        FirebaseDatabase.getInstance().getReference().child("StudentUser").child(auth.getUid()).setValue(NewUser);
                    }
                    startActivity(new Intent(SignUpActivity.this, SignIn_Activity.class));
                    finish();
                } else{
                    Toast.makeText(SignUpActivity.this,"ההרשמה נכשלה!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
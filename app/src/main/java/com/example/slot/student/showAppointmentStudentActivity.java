package com.example.slot.student;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slot.InternetBroadcastReceiver;
import com.example.slot.MainActivity;
import com.example.slot.R;
import com.example.slot.professor.ProfessorMainActivity;
import com.example.slot.utilclasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
public class showAppointmentStudentActivity extends AppCompatActivity {
  HashMap<String, String> appointments = new HashMap<>();
  private Button back_to_student_activity;
  private ListView appointments_list_view;
  private FirebaseAuth auth;
  InternetBroadcastReceiver ibr=new InternetBroadcastReceiver();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_appointment_student);
    appointments_list_view = findViewById(R.id.listview_student_appointments);
    back_to_student_activity = findViewById(R.id.button_back_to_main_student);
    auth= FirebaseAuth.getInstance();
    back_to_student_activity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(showAppointmentStudentActivity.this, StudentMainActivity.class));
      }
    });

    DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("StudentUser")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("Appointments");
    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for ( DataSnapshot snap : snapshot.getChildren() ){
          String course_name = snap.getKey();
          String booked_slots = (String) snap.getValue();
          appointments.put(course_name, booked_slots);
        }
//        System.out.println(appointments);
        init_list_view(appointments);
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


  private void init_list_view(HashMap<String, String> student_appointments){
    ArrayList<String> view_of_appointments = new ArrayList<>();
    for( String key : student_appointments.keySet()){
      String course = key;
      String[] course_arr = course.split("-");
      String pretty_course = "";
      for( String part: course_arr){
        pretty_course += part + " ";
      }
      String slot = student_appointments.get(key);
      view_of_appointments.add("     " + pretty_course + "   -    " + slot);
    }

    ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,view_of_appointments);
    appointments_list_view.setAdapter(adapter);


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
      startActivity(new Intent(showAppointmentStudentActivity.this, MainActivity.class));
      finish();
    }else if(item.getItemId()==R.id.menu_backToMain){
      startActivity(new Intent(showAppointmentStudentActivity.this, StudentMainActivity.class));
      finish();
    }else{
      return super.onOptionsItemSelected(item);
    }
    return true;
  }


}

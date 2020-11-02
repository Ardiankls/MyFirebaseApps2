package com.uc.myfirebaseapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.adapter.LecturerAdapter;
import com.uc.myfirebaseapss.adapter.StudentAdapter;
import com.uc.myfirebaseapss.model.Lecturer;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar bar;
    DatabaseReference dbStudent;
    ArrayList<Student> listStudent = new ArrayList<>();

    RecyclerView rv_student_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);
        bar = findViewById(R.id.tb_student_data);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        rv_student_data = findViewById(R.id.rv_student_data);

        fetchStudentData();
    }
    public void fetchStudentData(){
        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listStudent.clear();
                rv_student_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Student student = childSnapshot.getValue(Student.class);
                    listStudent.add(student);
                }
                showStudentData(listStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void showStudentData(final ArrayList<Student> list){
        rv_student_data.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setListStudent(list);
        rv_student_data.setAdapter(studentAdapter);

//        ItemClickSupport.addTo(rv_student_data).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                v.startAnimation(klik);
//                Intent intent = new Intent(StudentData.this, LecturerDetail.class);
//                Student student = new Student(list.get(position).getUid(), list.get(position).getName(), list.get(position).getPassword(), list.get(position).getEmail(), list.get(position).getNim(),list.get(position).getEmail(),list.get(position).getGender());
//                intent.putExtra("data_student", student);
//                intent.putExtra("position", position);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
//                startActivity(intent, options.toBundle());
//                finish();
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent intent;
            intent = new Intent(StudentData.this, RegisterStudent.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentData.this, RegisterStudent.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
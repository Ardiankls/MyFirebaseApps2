package com.uc.myfirebaseapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.adapter.CourseAdapter;
import com.uc.myfirebaseapss.model.Course;

import java.util.ArrayList;

public class CourseData extends AppCompatActivity {

    Toolbar bar;
    DatabaseReference dbCourse;
    ArrayList<Course> listCourse = new ArrayList<>();
    RecyclerView rv_course_data;
    Course course;
    Intent intent;
    Button button_edit, button_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_data);
        bar = findViewById(R.id.tb_coursedata);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        rv_course_data = findViewById(R.id.rv_coursedata);
        fetchCourseData();

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CourseData.this, AddCourse.class);
                intent.putExtra("action", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
    public void fetchCourseData(){
        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                    rv_course_data.setAdapter(null);
                }
                showCourseData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void showCourseData(final ArrayList<Course> list){
        rv_course_data.setLayoutManager(new LinearLayoutManager(CourseData.this));
        CourseAdapter CourseAdapter = new CourseAdapter(CourseData.this);
        CourseAdapter.setListCourse(list);
        rv_course_data.setAdapter(CourseAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            intent = new Intent(CourseData.this, AddCourse.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }else{
        intent = new Intent(CourseData.this, AddCourse.class);
        intent.putExtra("action", "edit_data_course");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(CourseData.this, AddCourse.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
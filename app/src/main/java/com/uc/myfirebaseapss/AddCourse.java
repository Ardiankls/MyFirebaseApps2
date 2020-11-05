package com.uc.myfirebaseapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourse extends AppCompatActivity implements TextWatcher {

    Toolbar bar;
    Dialog dialog;
    Spinner spinner_day, spinner_start, spinner_end, spinner_lecturer;
    TextInputLayout input_subject;
    String subject = "", day = "", timeS ="", timeE="", start = "", end = "", idlecturer = "", action="";
    Button btnAddCourse;
    Course course;
    DatabaseReference mDatabase;
    List<String> lecturer_array;
    ArrayAdapter<CharSequence> adapterend;

    DatabaseReference dbStudent;
    DatabaseReference dbCourse;
    DatabaseReference getDbCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        dbStudent = FirebaseDatabase.getInstance().getReference("student");

        dialog = Glovar.loadingDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bar = findViewById(R.id.tb_coursedata);

        input_subject = findViewById(R.id.input_subject_course);
        spinner_day = findViewById(R.id.spinner_day_course);
        spinner_start = findViewById(R.id.spinner_start_course);
        spinner_end = findViewById(R.id.spinner_end_course);
        spinner_lecturer = findViewById(R.id.spinner_lecturer_course);
        btnAddCourse =findViewById(R.id.btn_add_course);


        setSupportActionBar(bar);

        bar.setNavigationOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourse.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        

        spinner_day = findViewById(R.id.spinner_day_course);
        ArrayAdapter<CharSequence> adapterday = ArrayAdapter.createFromResource(AddCourse.this, R.array.day_array, android.R.layout.simple_spinner_item);
        adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapterday);

        spinner_lecturer = findViewById(R.id.spinner_lecturer_course);
        ArrayAdapter<CharSequence> adapterlecturer = ArrayAdapter.createFromResource(AddCourse.this, R.array.lecturer, android.R.layout.simple_spinner_item);
        adapterlecturer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lecturer.setAdapter(adapterlecturer);


        spinner_start = findViewById(R.id.spinner_start_course);
        ArrayAdapter<CharSequence> adapterstart = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_start_array, android.R.layout.simple_spinner_item);
        adapterstart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adapterstart);

        spinner_end = findViewById(R.id.spinner_end_course);

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterend = null;
                setSpinner_end(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lecturer_array = new ArrayList<>();
        mDatabase.child("lecturer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()){
                    String firebase_lecturer = childSnapshot.child("name").getValue(String.class);
                    lecturer_array.add(firebase_lecturer);
                }
                ArrayAdapter<String> adapterlecturers = new ArrayAdapter<>(AddCourse.this, android.R.layout.simple_spinner_item,lecturer_array);
                adapterlecturers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_lecturer.setAdapter(adapterlecturers);
                if (action.equalsIgnoreCase("edit_data_course")){
                    int lectIndex = adapterlecturers.getPosition(course.getLecturer());
                    spinner_lecturer.setSelection(lectIndex);
                    Log.d("lecturer", String.valueOf(lectIndex)+course.getLecturer());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle(R.string.addcourse);
            btnAddCourse.setText("Add");
            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subject = input_subject.getEditText().getText().toString().trim();
                    day = spinner_day.getSelectedItem().toString();
                    timeS = spinner_start.getSelectedItem().toString();
                    timeE = spinner_end.getSelectedItem().toString();
                    idlecturer = spinner_lecturer.getSelectedItem().toString();
                    addCourse(subject,day, timeS, timeE,idlecturer);
                }
            });
        }else {
            getSupportActionBar().setTitle(R.string.editcourse);
//            toolbar.setTitle("Edit Course");
            btnAddCourse.setText("Edit");
            course = intent.getParcelableExtra("edit_data_course");

            input_subject.getEditText().setText(course.getSubject());

            int dayIndex = adapterday.getPosition(course.getDay());
            spinner_day.setSelection(dayIndex);

            int startIndex = adapterstart.getPosition(course.getStart());
            spinner_start.setSelection(startIndex);

            setSpinner_end(startIndex);
            int endIndex = adapterend.getPosition(course.getEnd());
            spinner_end.setSelection(endIndex);


            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    subject = input_subject.getEditText().getText().toString().trim();
                    day = spinner_day.getSelectedItem().toString();
                    timeS = spinner_start.getSelectedItem().toString();
                    timeE = spinner_end.getSelectedItem().toString();
                    idlecturer = spinner_lecturer.getSelectedItem().toString();

                    Map<String, Object> params = new HashMap<>();
                    params.put("subject", subject);
                    params.put("day", day);
                    params.put("start", timeS);
                    params.put("end", timeE);
                    params.put("lecturer", idlecturer);

                    dbCourse.child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        //                    mDatabase.child("student").child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.cancel();
                            checkCourse(course.getId());
                            Intent intent;
                            Toast.makeText(AddCourse.this, "Course Data Updated Successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(AddCourse.this, CourseData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    public void addCourse(String msubject, String mday, String mstimeS, String mtimeE, String mlecturer){
        String mid = mDatabase.child("course").push().getKey();
        Course course = new Course(mid, msubject, mday, mstimeS, mtimeE, mlecturer);
        mDatabase.child("course").child(mid).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                Toast.makeText(AddCourse.this, "Add Course Successfully", Toast.LENGTH_SHORT).show();
                input_subject.getEditText().setText("");
                spinner_day.setSelection(0);
                spinner_start.setSelection(0);
                spinner_end.setSelection(0);
                spinner_lecturer.setSelection(0);

                Log.d("success", "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("nowpe", "");

                Toast.makeText(AddCourse.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        subject = input_subject.getEditText().getText().toString().trim();
        day = spinner_day.getSelectedItem().toString();
        timeS = spinner_start.getSelectedItem().toString();
        timeE = spinner_end.getSelectedItem().toString();
        idlecturer = spinner_lecturer.getSelectedItem().toString();

        if (!subject.isEmpty()) {
            btnAddCourse.setEnabled(true);
        } else {
            btnAddCourse.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.courselist_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        } else if (id == R.id.listCourse) {
            Intent intent;
            intent = new Intent(AddCourse.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddCourse.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    public void setSpinner_end(int position){
        if(position==0){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
        }else if(position==1){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
        }else if(position==2){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
        }else if(position==3){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
        }else if(position==4){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
        }else if(position==5){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
        }else if(position==6){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
        }else if(position==7){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
        }else if(position==8){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
        }else if(position==9){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
        }else if(position==10){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
        }else if(position==11){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
        }else if(position==12){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
        }else if(position==13){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
        }else if(position==14){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
        }else if(position==15){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
        }else if(position==16){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
        }else if(position==17){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
        }else if(position==18){
            adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
        }

        adapterend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_end.setAdapter(adapterend);
    }
    public void checkCourse(final String check){
        dbStudent.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot stud : snapshot.getChildren()){
                    dbCourse = dbStudent.child(stud.getValue(Student.class).getUid()).child("courses");
                    dbCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot cr : snapshot.getChildren()){
                                cr.getValue(Course.class).getId();
                                if (check.equals(cr.getValue(Course.class).getId())){
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("subject", subject);
                                    params.put("day", day);
                                    params.put("start", timeS);
                                    params.put("end", timeE);
                                    params.put("lecturer", idlecturer);
                                    dbCourse.child(cr.getValue(Course.class).getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}


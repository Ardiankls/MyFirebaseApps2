package com.uc.myfirebaseapss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

public class AddCourse extends AppCompatActivity {

    Toolbar bar;
    Dialog dialog;
    Spinner spinner_day, spinner_start, spinner_end;
    TextInputLayout input_subject;
    String subject = "", day = "", start = "", end = "", idlecturer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        bar = findViewById(R.id.tb_course);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        input_subject = findViewById(R.id.input_subject_course);
        input_subject.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner_day = findViewById(R.id.spinner_day_course);
        ArrayAdapter<CharSequence> adapterday = ArrayAdapter.createFromResource(AddCourse.this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapterday);
        spinner_end = findViewById(R.id.spinner_end_course);
        spinner_start = findViewById(R.id.spinner_start_course);
        ArrayAdapter<CharSequence> adapterstart = ArrayAdapter.createFromResource(AddCourse.this,
                R.array.jam_start_array, android.R.layout.simple_spinner_item);
        adapterstart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adapterstart);

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapterend = null;
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(AddCourse.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
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
}
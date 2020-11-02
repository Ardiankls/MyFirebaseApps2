//package com.uc.myfirebaseapss;
//
//import android.app.ActivityOptions;
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.uc.myfirebaseapss.model.Student;
//
//public class AddStudent extends AppCompatActivity implements TextWatcher {
//
//    Toolbar bar;
//    Dialog dialog;
//    TextInputLayout input_email, input_password, input_fullname, input_nim, input_age, input_address;
//    RadioGroup rg_gender;
//    RadioButton radioButton;
//    Button btn_add;
//    String email="", password="", fullname="", nim="", gender="male", age="", address="",  action="";
//    Student student;
//    private DatabaseReference mDatabase;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register_student);
//        dialog = Glovar.loadingDialog(AddStudent.this);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        bar = findViewById(R.id.tb_reg_student);
//        setSupportActionBar(bar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        input_email = findViewById(R.id.input_email_reg_student);
//        input_password = findViewById(R.id.input_password_reg_student);
//        input_fullname = findViewById(R.id.input_name_reg_student);
//        input_nim = findViewById(R.id.input_nim_reg_student);
//        input_age = findViewById(R.id.input_age_reg_student);
//        input_address = findViewById(R.id.input_age_reg_student);
//
//        input_email.getEditText().addTextChangedListener(this);
//        input_password.getEditText().addTextChangedListener(this);
//        input_fullname.getEditText().addTextChangedListener(this);
//        input_nim.getEditText().addTextChangedListener(this);
//        input_age.getEditText().addTextChangedListener(this);
//        input_address.getEditText().addTextChangedListener(this);
//        btn_add = findViewById(R.id.btn_reg_student);
//        rg_gender=findViewById(R.id.radg_gender_reg_student);
//        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int i) {
//                radioButton = findViewById(i);
//                gender = radioButton.getText().toString();
//            }
//        });
//
//        Intent intent = getIntent();
//        action = intent.getStringExtra("action");
//        if(action.equals("add")) {
//            btn_add.setText("Register Student");
//            btn_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    email = input_email.getEditText().getText().toString().trim();
//                    password = input_password.getEditText().getText().toString().trim();
//                    fullname = input_fullname.getEditText().getText().toString().trim();
//                    nim = input_nim.getEditText().getText().toString().trim();
//                    age = input_age.getEditText().getText().toString().trim();
//                    address = input_address.getEditText().getText().toString().trim();
//                    addStudent(email, password, fullname, nim, gender, age, address);
//
//                }
//            });
//        }else{
//
//        }
//
//
//
//    }
//
//    public void addStudent(String memail, String mpassword,  String mfullname, String mnim, String mgender, String mage, String maddress){
//        dialog.show();
//        String mid = mDatabase.child("student").push().getKey();
//        Student student = new Student(mid, memail, mpassword, mfullname, mnim, mgender, mage, maddress);
//        mDatabase.child("student").child(mid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                dialog.cancel();
//                Toast.makeText(AddStudent.this, "Student Registered ", Toast.LENGTH_LONG).show();
//                input_email.getEditText().setText("");
//                input_password.getEditText().setText("");
//                input_fullname.getEditText().setText("");
//                input_nim.getEditText().setText("");
//                rg_gender.check(R.id.rad_male_reg_student);
//                input_age.getEditText().setText("");
//                input_address.getEditText().setText("");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                dialog.cancel();
//                Toast.makeText(AddStudent.this, "Register failed, please try again", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        email = input_email.getEditText().getText().toString().trim();
//        password = input_password.getEditText().getText().toString().trim();
//        fullname = input_fullname.getEditText().getText().toString().trim();
//        nim = input_nim.getEditText().getText().toString().trim();
//        age = input_age.getEditText().getText().toString().trim();
//        address = input_address.getEditText().getText().toString().trim();
//        if (!email.isEmpty()&& !password.isEmpty() && !fullname.isEmpty() && !nim.isEmpty()
//        && !age.isEmpty()
//        && !address.isEmpty()){
//            btn_add.setEnabled(true);
//        }else{
//            btn_add.setEnabled(false);
//        }
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }
//
//    @Override
//    public void onBackPressed(){
//        Intent intent;
//        intent = new Intent(AddStudent.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddStudent.this);
//        startActivity(intent, options.toBundle());
//        finish();
//    }
//
//}

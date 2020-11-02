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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.uc.myfirebaseapss.model.Student;

public class LoginStudent extends AppCompatActivity implements TextWatcher {

    Toolbar bar;
    Dialog dialog;
    FirebaseAuth firebaseAuth;
    Button btn_login;
    TextInputLayout input_email, input_password;
    Intent intent;
    String email, password;
    Student student;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);
        bar = findViewById(R.id.tb_login);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = Glovar.loadingDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        input_email = findViewById(R.id.input_email_login);
        input_password = findViewById(R.id.input_password_login);

        input_email.getEditText().addTextChangedListener(this);
        input_password.getEditText().addTextChangedListener(this);
        btn_login = findViewById(R.id.btn_loginStud);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStudent.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
//        bar = findViewById(R.id.tb_login);
//        setSupportActionBar(bar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                email = input_email.getEditText().getText().toString().trim();
                password =input_password.getEditText().getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.cancel();
                        if(task.isSuccessful()){
                            Toast.makeText(LoginStudent.this, "Login Success", Toast.LENGTH_SHORT).show();
                            intent = new Intent(LoginStudent.this, UserFragment.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginStudent.this, "Failed, Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LoginStudent.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginStudent.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LoginStudent.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginStudent.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        email = input_email.getEditText().getText().toString().trim();
        password = input_password.getEditText().getText().toString().trim();

        if(!email.isEmpty() && !password.isEmpty()){
            btn_login.setEnabled(true);
        }else{
            btn_login.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
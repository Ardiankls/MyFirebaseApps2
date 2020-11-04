package com.uc.myfirebaseapss.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.myfirebaseapss.Glovar;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.RegisterStudent;
import com.uc.myfirebaseapss.StudentData;
import com.uc.myfirebaseapss.model.Lecturer;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder>{
    DatabaseReference dbStud;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private Context context;
    Dialog dialog;
    private ArrayList<Student> listStudent;
    private ArrayList<Student> getListStudent() {
        return listStudent;
    }
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }
    public StudentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.CardViewViewHolder holder, int position) {
        final Student student = getListStudent().get(position);
        Log.d("test", "nama"+student.getName()+student.getEmail()+student.getNim()+student.getAddress());
        holder.lbl_name_student.setText(student.getName());
        holder.lbl_email_student.setText(student.getEmail());
        holder.lbl_nim_student.setText(student.getNim());
        holder.lbl_address_student.setText(student.getAddress());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        holder.btn_editStud.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent in = new Intent(context, RegisterStudent.class);
                in.putExtra("action", "edit_stud");
                in.putExtra("edit_data_student", student);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(in);
//                finish();
            }
        });
        holder.btn_delStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.ic_android_goldtrans_24dp)
                        .setMessage("Delete "+student.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();

                                        String uid = student.getUid();
                                        Log.d("cobadong", uid);

                                        firebaseAuth.signInWithEmailAndPassword(student.getEmail(),student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                firebaseUser = firebaseAuth.getCurrentUser();
                                                firebaseUser.delete();

                                                dbStud.child(student.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Intent in = new Intent(context, StudentData.class);
                                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                                                        context.startActivity(in);
                                                        ((Activity)context).finish();
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                            }
                                        });
//
                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView   lbl_name_student, lbl_email_student, lbl_nim_student, lbl_address_student;
        ImageView btn_editStud, btn_delStud;
        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_name_student = itemView.findViewById(R.id.lbl_name_student_adp);
            lbl_nim_student = itemView.findViewById(R.id.lbl_nim_student_adp);
            lbl_email_student = itemView.findViewById(R.id.lbl_email_student_adp);
            lbl_address_student = itemView.findViewById(R.id.lbl_address_student_adp);
            btn_delStud = itemView.findViewById(R.id.stud_delete);
            btn_editStud = itemView.findViewById(R.id.stud_edit);
            dbStud = FirebaseDatabase.getInstance().getReference("student");
            dialog = Glovar.loadingDialog(context);

        }
    }
}

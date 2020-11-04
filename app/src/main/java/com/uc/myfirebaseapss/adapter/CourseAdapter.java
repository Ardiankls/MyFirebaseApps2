package com.uc.myfirebaseapss.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.AddCourse;
import com.uc.myfirebaseapss.CourseData;
import com.uc.myfirebaseapss.Glovar;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewViewHolder> {

    Dialog dialog;
    private Context context;
    DatabaseReference dbCourse;
    DatabaseReference dbStudent;
    DatabaseReference dbCourses;
    int pos =0;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private ArrayList<Course> listCourse;
    private ArrayList<Course>getListCourse(){return listCourse;}
    public void setListCourse(ArrayList<Course>listCourse){this.listCourse = listCourse;}
    public CourseAdapter (Context context){this.context = context;}




    @NonNull
    @Override
    public CourseAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_course_adapter, parent, false);
        return new CourseAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        holder.cvSubject.setText(course.getSubject());
        holder.cvDay.setText(course.getDay());
        holder.cvStart.setText(course.getStart());
        holder.cvEnd.setText(course.getEnd());
        holder.cvLecturer.setText(course.getLecturer());

        holder.btn_edit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, AddCourse.class);
                in.putExtra("action", "edit_data_course");
                in.putExtra("edit_data_course", course);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(in);

            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
//                        .setIcon(R.drawable.logo2)
                        .setMessage("Are you sure you want to delete  "+course.getSubject()+" ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        checkCourse(course.getId());
                                        dbCourse.child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Intent in = new Intent(context, CourseData.class);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
//                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context);
//                                                context.startActivity(in, options.toBundle());
                                                context.startActivity(in);
                                                ((Activity)context).finish();
                                                dialogInterface.cancel();
                                            }
                                        });

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
        return getListCourse().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView cvSubject, cvDay, cvStart, cvEnd, cvLecturer;
        ImageView btn_edit, btn_delete;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSubject = itemView.findViewById(R.id.courseSubj_sched_adap);
            cvDay = itemView.findViewById(R.id.courseDay_sched_adap);
            cvStart = itemView.findViewById(R.id.courseStart_sched_adap);
            cvEnd = itemView.findViewById(R.id.courseEnd_sched_adap);
            cvLecturer = itemView.findViewById(R.id.courseLect_sched_adap);

            dbCourse = FirebaseDatabase.getInstance().getReference("course");
            dbStudent = FirebaseDatabase.getInstance().getReference("student");

            dialog = Glovar.loadingDialog(context);

            btn_edit = itemView.findViewById(R.id.edit_course_adap);
            btn_delete = itemView.findViewById(R.id.del_sched_adap);
        }
    }
    public void checkCourse(final String check){
        dbStudent.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot stud : snapshot.getChildren()){
                    dbCourses = dbStudent.child(stud.getValue(Student.class).getUid()).child("courses");
                    dbCourses.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot cr : snapshot.getChildren()){
                                cr.getValue(Course.class).getId();
                                if (check.equals(cr.getValue(Course.class).getId())){
                                    dbCourses.child(cr.getValue(Course.class).getId()).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Log.d("yipii", check);
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
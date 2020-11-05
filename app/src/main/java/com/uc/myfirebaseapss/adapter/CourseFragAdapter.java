package com.uc.myfirebaseapss.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.Glovar;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.model.Course;

import java.util.ArrayList;

public class CourseFragAdapter extends RecyclerView.Adapter<CourseFragAdapter.CardViewViewHolder>{

    private Context context;
    Dialog dialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dbCourse;
    Course course;
    int pos = 0;
    private ArrayList<Course> listCourse;

    private ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    public CourseFragAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_course_frag_adapter, parent, false);
        return new CourseFragAdapter.CardViewViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CourseFragAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        holder.cvSubject.setText(course.getSubject());
        holder.cvDay.setText(course.getDay());
        holder.cvStart.setText(course.getStart());
        holder.cvEnd.setText(course.getEnd());
        holder.cvLecturer.setText(course.getLecturer());

        holder.btn_enroll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Take this " + course.getSubject() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        CheckTime(course);

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
    MutableLiveData<Course> courseAdd = new MutableLiveData<>();
    public MutableLiveData<Course> getCourseAdd() {
        return courseAdd;
    }
    boolean conflict = false;
    public void CheckTime(final Course choose) {


        final String courseDay = choose.getDay();
        final int courseStart = Integer.parseInt(choose.getStart().replace(":", ""));
        Log.d("testCourseStart 1", String.valueOf(courseStart));
        final int courseEnd = Integer.parseInt(choose.getEnd().replace(":", ""));

        FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conflict = false;
                Log.d("testCourseStart 2", String.valueOf(courseStart));
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course courses = childSnapshot.getValue(Course.class);


                    String cvDay = courses.getDay();
                    int cvStart = Integer.parseInt(courses.getStart().replace(":", ""));
                    int cvEnd = Integer.parseInt(courses.getEnd().replace(":", ""));

                    Log.d("testCourseStart 3", String.valueOf(cvStart));

                    if (courseDay.equalsIgnoreCase(cvDay)) {
                        Log.d("testCourseStart 4", (cvDay)+courseDay);
                        if (courseStart >= cvStart && courseStart < cvEnd) {
                            conflict = true;
                            Log.d("testCourseStart 5", String.valueOf(cvStart));
                        }
                        if (courseEnd > cvStart && courseEnd <= cvEnd) {
                            conflict = true;
                        }
                    }
                }
                if (conflict == true){
                    Log.d("testConflict", "YASsss");
                }else {
                    Log.d("testConflict", "NOOOOO");
                }

                if (conflict) {

                    new AlertDialog.Builder(context)
                            .setTitle("Warning")
                            .setMessage("You cannot take this course, check again your schedule!")
                            .setCancelable(false)
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {
                                    dialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.cancel();

                                        }
                                    }, 1000);
                                }
                            })
                            .create()
                            .show();
                } else {
                    courseAdd.setValue(choose);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView cvSubject, cvDay, cvStart, cvEnd, cvLecturer;
        ImageView btn_enroll;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSubject = itemView.findViewById(R.id.courseSubj_sched_adap);
            cvDay = itemView.findViewById(R.id.courseDay_sched_adap);
            cvStart = itemView.findViewById(R.id.courseStart_sched_adap);
            cvEnd = itemView.findViewById(R.id.courseEnd_sched_adap);
            cvLecturer = itemView.findViewById(R.id.courseLect_sched_adap);

            dbCourse = FirebaseDatabase.getInstance().getReference("course");
            dbCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        course = childSnapshot.getValue(Course.class);
                        listCourse.add(course);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            dialog = Glovar.loadingDialog(context);

            btn_enroll = itemView.findViewById(R.id.btn_enroll);
        }
    }
}
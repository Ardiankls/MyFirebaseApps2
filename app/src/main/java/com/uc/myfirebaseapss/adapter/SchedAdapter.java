package com.uc.myfirebaseapss.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.myfirebaseapss.Glovar;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.model.Course;

import java.util.ArrayList;

public class SchedAdapter extends RecyclerView.Adapter<SchedAdapter.CardViewViewHolder> {
    private Context context;
    Dialog dialog;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbStudent;
    int pos = 0;
    private ArrayList<Course> listCourse;
    private ArrayList<Course> getListCourse() {
        return listCourse;
    }
    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }
    public SchedAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sched_adapter, parent, false);
        return new SchedAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchedAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        holder.cvSubject.setText(course.getSubject());
        holder.cvDay.setText(course.getDay());
        holder.cvStart.setText(course.getStart());
        holder.cvEnd.setText(course.getEnd());
        holder.cvLecturer.setText(course.getLecturer());

        holder.btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
//                        .setIcon(R.drawable.logo2)
                        .setMessage("Are you sure to delete "+course.getSubject()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dbStudent.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(context, "Course has been removed", Toast.LENGTH_SHORT).show();
                                                dialogInterface.cancel();
                                                Log.d("id course", course.getId());

                                            }
                                        });
                                        dialog.cancel();
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
        ImageView btn_delete;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSubject = itemView.findViewById(R.id.courseSubj_sched_adap);
            cvDay = itemView.findViewById(R.id.courseDay_sched_adap);
            cvStart = itemView.findViewById(R.id.courseStart_sched_adap);
            cvEnd = itemView.findViewById(R.id.courseEnd_sched_adap);
            cvLecturer = itemView.findViewById(R.id.courseLect_sched_adap);

            dbStudent = FirebaseDatabase.getInstance().getReference("student");

            dialog = Glovar.loadingDialog(context);

            btn_delete = itemView.findViewById(R.id.del_sched_adap);
        }
    }
}
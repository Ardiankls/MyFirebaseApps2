package com.uc.myfirebaseapss.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.adapter.SchedAdapter;
import com.uc.myfirebaseapss.model.Course;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {
    TextView nosched;
    RecyclerView recyclerView;
    Adapter ScheduleAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Course course;
    DatabaseReference dbCourse;
    ImageView imageView;
    ArrayList<Course> listCourse = new ArrayList<>();

    public ScheduleFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbCourse = FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses");
        nosched = getView().findViewById(R.id.lbl_nosched);
        recyclerView = getView().findViewById(R.id.rv_sched_frag);

        nosched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Input Your Schedule First!", Toast.LENGTH_SHORT).show();
            }
        });
        fetchCourseData();
    }
    public void fetchCourseData(){
        dbCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                    recyclerView.setAdapter(null);
                }
                showCourseData(listCourse);
                if (listCourse.isEmpty()){
                    nosched.setVisibility(View.VISIBLE);
                }else{
                    nosched.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void showCourseData(final ArrayList<Course> list){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SchedAdapter ScheduleAdapter = new SchedAdapter(getActivity());
        ScheduleAdapter.setListCourse(list);
        recyclerView.setAdapter(ScheduleAdapter);

    }
}
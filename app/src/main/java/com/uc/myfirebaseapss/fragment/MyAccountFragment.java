package com.uc.myfirebaseapss.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.Glovar;
import com.uc.myfirebaseapss.MainActivity;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.model.Student;


public class MyAccountFragment extends Fragment {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Button logout;
    Dialog dialog;
    Student student;
    TextView txtname, txtnim, txtemail, txtage, txtgender, txtaddress;
    DatabaseReference dbStudent;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        dbStudent = FirebaseDatabase.getInstance().getReference("student").child(firebaseAuth.getCurrentUser().getUid());
        dialog = Glovar.loadingDialog(getActivity());

        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student = snapshot.getValue(Student.class);
                userData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void userData(){
        txtname.setText(student.getName());
        txtnim.setText(student.getNim());
        txtemail.setText(student.getEmail());
        txtage.setText(student.getAge());
        txtgender.setText(student.getGender());
        txtaddress.setText(student.getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtname = (TextView) getView().findViewById(R.id.userfrag_name);
        txtnim = (TextView) getView().findViewById(R.id.userfrag_nim);
        txtemail = (TextView) getView().findViewById(R.id.userfrag_email);
        txtage = (TextView) getView().findViewById(R.id.userfrag_age);
        txtgender = (TextView) getView().findViewById(R.id.userfrag_gender);
        txtaddress= (TextView) getView().findViewById(R.id.userfrag_address);

        logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(getActivity())
                        .setMessage("Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);

                                    }
                                },2000);
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
}
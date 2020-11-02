package com.uc.myfirebaseapss;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uc.myfirebaseapss.fragment.CourseFragment;
import com.uc.myfirebaseapss.fragment.MyAccountFragment;
import com.uc.myfirebaseapss.fragment.ScheduleFragment;

public class UserFragment extends AppCompatActivity {
    Toolbar bar;
    BottomNavigationView btmNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fragment);
        bar = findViewById(R.id.tb_userfrag);

        setSupportActionBar(bar);

        btmNavBar = findViewById(R.id.bottom_nav_menu);
        btmNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.schedule_menu:
                        bar.setTitle(R.string.schedule_frag_menu);
                        setSupportActionBar(bar);
                        fragment = new ScheduleFragment();
                        loadFragment(fragment);
                        return false;
                    case R.id.myaccount_menu:
                        bar.setTitle("Account");
                        setSupportActionBar(bar);
                        fragment = new MyAccountFragment();
                        loadFragment(fragment);
                        return  false;
                    case R.id.courses_menu:
                        bar.setTitle("Course");
                        setSupportActionBar(bar);
                        fragment = new CourseFragment();
                        loadFragment(fragment);
                        return  false;

                }

                return false;
            }
        });
    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_user_frag, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        btmNavBar.setSelectedItemId(R.id.schedule_menu);
    }
}
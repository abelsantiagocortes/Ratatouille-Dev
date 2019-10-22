package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseReference dbChef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager) findViewById(R.id.viewPager);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        dbChef = dbRats.getReference("userChef");


        dbChef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tabLayout.setupWithViewPager(viewPager);
                setUpViewPager();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(setUpViewPager());


    }
    public TabViewPagerAdapter setUpViewPager(){

        TabViewPagerAdapter tabViewPagerAdapter= new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new Chefs_tab(),"Chefs");
        tabViewPagerAdapter.addFragment(new Foodplates_tab(),"Food Plates");
        tabViewPagerAdapter.addFragment(new Orders_tab(),"Orders");
        return  tabViewPagerAdapter;


    }

    @Override
    public void onBackPressed() {
    }
}

package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private FirebaseAuth signOutAuth;
    ImageView imgLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager) findViewById(R.id.viewPager);

        signOutAuth = FirebaseAuth.getInstance();
        imgLogOut = findViewById(R.id.logOut);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        dbChef = dbRats.getReference("userChef");

        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAuth.signOut();
                Intent intent = new Intent( getApplicationContext(), LogIn.class );
                startActivity(intent);
            }
        });


        dbChef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                viewPager.setAdapter(setUpViewPager());
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

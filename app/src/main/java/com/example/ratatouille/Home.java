package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseReference dbChef;
    private FirebaseAuth signOutAuth;
    ImageView imgLogOut;
    EditText direccionIngresada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager) findViewById(R.id.viewPager);

        signOutAuth = FirebaseAuth.getInstance();
        imgLogOut = findViewById(R.id.logOut);
        direccionIngresada = findViewById(R.id.DireccionIngresada);

        FirebaseUser user = signOutAuth.getCurrentUser();
        String uid= user.getUid();
        Log.i("HOME", "DIRECCION " + uid);
        Query queryClientDireccion = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(uid);
        queryClientDireccion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dir : dataSnapshot.getChildren()){
                        Log.i("SP",dir.getValue(UserClient.class).getDir());
                        direccionIngresada.setText(dir.getValue(UserClient.class).getDir());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("LOGINFAILED","CHEF" );
            }
        });
        direccionIngresada.setText("HOLIIIII");

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

    @Override
    protected void onDestroy() {
        try {
            signOutAuth.signOut();
        }catch (Exception e){

        }finally {
            super.onDestroy();
        }
    }
}

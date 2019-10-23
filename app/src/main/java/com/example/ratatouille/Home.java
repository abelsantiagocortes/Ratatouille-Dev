package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseReference dbChef;
    private FirebaseAuth signOutAuth;
    ImageView imgLogOut;
    EditText direccionIngresada;
    Geocoder mGeocoder = null;
    private LatLng latLngDireccion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout= findViewById(R.id.tabLayout);
        viewPager= findViewById(R.id.viewPager);
        mGeocoder = new Geocoder(this);
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
                        generarLatLng(dir.getValue(UserClient.class).getDir());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("LOGINFAILED","CHEF" );
            }
        });

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
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(setUpViewPager());

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(setUpViewPager());
        direccionIngresada.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    Log.i("Teclado","Finalización");
                    String nuevaDireccion = direccionIngresada.getText().toString();
                    generarLatLng(nuevaDireccion);
                }
                return false;
            }
        });

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
    private void generarLatLng(String nuevaDireccion) {
        if(!nuevaDireccion.isEmpty()){
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(nuevaDireccion , 2);
                Log.i("Posicion","Obteniendo");
                if (addresses != null && !addresses.isEmpty ()){
                    Address addressResult = addresses.get(0);
                    latLngDireccion = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                } else {
                    Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

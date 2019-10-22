package com.example.ratatouille;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Chefs_tab extends Fragment{

    private static final double RADIUS_OF_EARTH_KM = 6371;
    private ViewPager viewPagerC;
    private MyViewPagerAdapter adapter;
    private LinearLayout dotsLayout;
    public List<UserChef> listChefs;
    Button btn_back,btn_next;
    FirebaseAuth current;
    DatabaseReference dbUser;
    double lat;
    double longi;
    List<String> listDistances;

    private int[] images={R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger,R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger,R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger};
    @Override

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_chefs,container,false);

        viewPagerC= view.findViewById(R.id.viewPagerC);

        dotsLayout= view.findViewById(R.id.LayoutDots);
        btn_back= view.findViewById(R.id.btn_back);
        btn_next= view.findViewById(R.id.btn_next);

        current = FirebaseAuth.getInstance();
        FirebaseUser currentUser = current.getCurrentUser();
        String userId = currentUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbUser = ref.child("userClient").child(userId);

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserClient user;
                user = dataSnapshot.getValue(UserClient.class);
                lat=user.lat;
                longi=user.longi;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listChefs = new ArrayList<>();
        listDistances = new ArrayList<>();
        loadNearbyChefs();

        return view;

    }

    private void loadNearbyChefs() {

        Query query = FirebaseDatabase.getInstance().getReference("userChef");
        query.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            listDistances.clear();
            listChefs.clear();
            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserChef chef = snapshot.getValue(UserChef.class);
                    double distance = distance(chef.getLat(),chef.getLongi(),lat,longi);
                    if(distance<5)
                    {
                        listChefs.add(chef);
                        listDistances.add(String.valueOf(distance));
                        System.out.println(chef.getName());
                        System.out.println(chef.getDir());
                        System.out.println(distance);
                    }

                }
            }

            for(int i =0;i<listChefs.size();i++)
            {
                System.out.println(listChefs.get(i).getName());
            }
            loadViewPager(listChefs,listDistances);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }



    };

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }

    private void loadViewPager(List<UserChef>listChefs,List<String>listDistances){
         adapter= new MyViewPagerAdapter(getFragmentManager());
         for(int i=0;i<listChefs.size();i++){
            adapter.addFragment(newInstance(listChefs.get(i).getName(),listDistances.get(i),images[i]));
         }

         viewPagerC.setAdapter(adapter);
    }
    private SliderFragment newInstance(String n_chef,String loc_chef,int image){
        Bundle bundle = new Bundle();
        bundle.putString("NameChef",n_chef);
        bundle.putString("LocChef",loc_chef);
        bundle.putInt("ImageChef",image);

        SliderFragment fragment=new SliderFragment();
        fragment.setArguments(bundle);
        return fragment;

    }


}

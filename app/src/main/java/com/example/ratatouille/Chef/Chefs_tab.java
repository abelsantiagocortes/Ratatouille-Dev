package com.example.ratatouille.Chef;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.ClientChef.ClientChefDistance;
import com.example.ratatouille.Client.Home;
import com.example.ratatouille.R;
import com.example.ratatouille.adapters.MyViewPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chefs_tab extends Fragment{

    private static final double RADIUS_OF_EARTH_KM = 6371;
    private ViewPager viewPagerC;
    private MyViewPagerAdapter adapter;
    private LinearLayout dotsLayout;
    public List<UserChef> listChefs;
    public List<ClientChefDistance> listOrdered;
    FirebaseAuth current;
    DatabaseReference dbUser;
    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseReference dbChefs;
    StorageReference storageChef;
    double lat;
    double longi;
    Button btn_refresh;
    List<String> listDistances;
    FirebaseStorage dbRatsStorage;

    private int[] images={R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger,R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger,R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger,R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test,R.drawable.hamburger,R.drawable.egg,R.drawable.plate1_test};
    @Override

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_chefs,container,false);

        viewPagerC= view.findViewById(R.id.viewPagerC);

        btn_refresh= view.findViewById(R.id.button_ref);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        current = FirebaseAuth.getInstance();
        FirebaseUser currentUser = current.getCurrentUser();
        String userId = currentUser.getUid();
        dbRatsStorage = FirebaseStorage.getInstance();
        storageChef = dbRatsStorage.getReference();

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
        //listChefs = new ArrayList<>();
        //listDistances = new ArrayList<>();
        listOrdered = new ArrayList<>();
        loadNearbyChefs();

        return view;

    }

    private void loadNearbyChefs() {

        Query query = FirebaseDatabase.getInstance().getReference("userChef");
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserChef chef = snapshot.getValue(UserChef.class);
                    double distance = distance(chef.getLat(),chef.getLongi(),lat,longi);
                    if(distance<5 && chef.isStatus())
                    {
                        FirebaseUser currentUser = current.getCurrentUser();
                        String userId = currentUser.getUid();

                        ClientChefDistance obj= new ClientChefDistance(userId,chef.getUserId(),chef.getName(),cargarImagen(snapshot,dbRatsStorage),distance);
                        listOrdered.add(obj);

                    }

                }
            }
            Collections.sort(listOrdered);
            loadViewPager(listOrdered);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }



    };

    private Bitmap[] cargarImagen(DataSnapshot dir, FirebaseStorage dbRatsStorage) {
        final Bitmap[] bitmap = {null};
        StorageReference sRf = dbRatsStorage.getReferenceFromUrl(dir.getValue(UserChef.class).getPhotoDownloadURL());
        try {
            final File localFile = File.createTempFile("images", "jpg");
            sRf.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return bitmap;
    }

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

    private void loadViewPager(List<ClientChefDistance>listOrdered){
         adapter= new MyViewPagerAdapter(getFragmentManager());
         for(int i=0;i<listOrdered.size();i++){
            adapter.addFragment(newInstance(listOrdered.get(i).getChefName(), String.valueOf(listOrdered.get(i).getDistance()),listOrdered.get(i).getImgChef(),images[i],listOrdered.get(i).getIdChef()));
         }
         viewPagerC.setAdapter(adapter);
        listOrdered.clear();
    }
    private SliderFragment newInstance(String n_chef, String loc_chef, Bitmap[] image, int img, String chefId){


        Bundle bundle = new Bundle();
        bundle.putString("NameChef",n_chef);
        bundle.putString("LocChef",loc_chef);
        bundle.putString("ChefId",chefId);
        bundle.putInt("ImagePlate",img);

        //bundle.putSerializable("ImageChef",image);

        System.out.println("Bundle :");
        System.out.println(n_chef);
        System.out.println(loc_chef);

        SliderFragment fragment=new SliderFragment();
        fragment.setArguments(bundle);
        return fragment;

    }


}

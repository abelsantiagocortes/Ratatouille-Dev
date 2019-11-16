package com.example.ratatouille.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.ClientChef.ClientChefDistance;
import com.example.ratatouille.R;
import com.example.ratatouille.adapters.ChefsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;

public class ChefbyFood extends AppCompatActivity {

    private static final double RADIUS_OF_EARTH_KM = 6371;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public List<ClientChefDistance> listOrdered ;
    FirebaseAuth current;
    FirebaseStorage dbRatsStorage;
    StorageReference storageChef;
    double lati;
    double longit;
    UserClient currentUser;
    List<Recipe> recipes;
    String type;

    boolean cumpleTipo=false;

    List<String> recipe;
    List<String> recipeids;
    List<String> recipeTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefby_food);
         type = getIntent().getStringExtra("foodType");

        recyclerView = (RecyclerView) findViewById(R.id.rcy_chefs);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        dbRatsStorage = FirebaseStorage.getInstance();
        storageChef = dbRatsStorage.getReference();

        listOrdered=new ArrayList<>();
        //Memoria para arreglos
        recipe = new ArrayList<String>();
        recipeids = new ArrayList<String>();
        recipeTypes = new ArrayList<String>();

        //Cosas de firebase necesarias
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
                lati=user.getLat();
                longit=user.getLongi();
                //Se saca la lista de chefs dependiendo del foodType que le llega en el intent
                loadNearbyChefs();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // specify an adapter (see also next example)


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
                for (DataSnapshot snapshoti : dataSnapshot.getChildren()) {
                    final UserChef chef = snapshoti.getValue(UserChef.class);
                    final double distance = distance(chef.getLat(),chef.getLongi(),lati,longit);

                    boolean vale=validarChefFoodType(chef);
                    if(vale){
                        FirebaseUser currentUser = current.getCurrentUser();
                        String userId = currentUser.getUid();

                        ClientChefDistance obj= new ClientChefDistance(userId,chef.getUserId(),chef.getName(),cargarImagen(snapshoti,dbRatsStorage),distance);
                        listOrdered.add(obj);
                    }
                    //  Saca todas las recetas para mirar si alguna tiene

                }
            }
            Collections.sort(listOrdered);

            mAdapter = new ChefsAdapter(listOrdered);
            recyclerView.setAdapter(mAdapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

    private boolean validarChefFoodType(UserChef chef) {
        List<String> foodTypeChef = new ArrayList<>();
        foodTypeChef=chef.getFoodType();
        for (int i=0;i<foodTypeChef.size();i++){
            if(foodTypeChef.get(i).equals(type)){
                return true;
            }
        }
        return false;
    }



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


}

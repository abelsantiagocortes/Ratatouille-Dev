package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChefProfile extends AppCompatActivity {

    TextView name;
    TextView description;
    TextView ids;
    Button btn_solicitud;
    DatabaseReference dbNotifs;
    UserChef chef;
    FirebaseAuth current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_profile);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ChefId");

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        dbNotifs = dbRats.getReference("solicitud");

        name=findViewById(R.id.nameChef);
        description=findViewById(R.id.descriptionChef);
        btn_solicitud = findViewById(R.id.btn_solicitud);
        ids=findViewById(R.id.idchef);

        Query queryChefData = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(id);
        queryChefData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dir : dataSnapshot.getChildren()){
                        chef = dir.getValue(UserChef.class);
                        name.setText(chef.getName());
                        description.setText(chef.getDescription());
                        ids.setText(chef.getUserId());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = FirebaseAuth.getInstance();
                FirebaseUser currentUser = current.getCurrentUser();
                String userId = currentUser.getUid();
                Solicitud notif = new Solicitud(userId,chef.getUserId());

                String key=dbNotifs.push().getKey();
                dbNotifs.child(key).setValue(notif);
            }
        });



    }
}

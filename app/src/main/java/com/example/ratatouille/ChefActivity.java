package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class ChefActivity extends AppCompatActivity {
    Switch disponible;

    DatabaseReference dbChefs;

    FirebaseAuth registerAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        registerAuth = FirebaseAuth.getInstance();
        dbChefs = dbRats.getReference("userChef");

        disponible = findViewById(R.id.Disponible);
        disponible.setChecked(true);

        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();

        disponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dbChefs.child(userId).child("status").setValue(isChecked);
            }
        });
    }

    @Override
    public void onBackPressed() {
        try{
            registerAuth.signOut();
        }catch (Exception e){

        }finally {
            super.onBackPressed();
        }
    }
}

package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DescripcionChef extends AppCompatActivity {
    TextView description;
    Button register;
    FirebaseDatabase dbRats;
    DatabaseReference dbUsersChefs;
    FirebaseAuth registerAuth;
    DatabaseReference dbChefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_chef);
        description = findViewById(R.id.Description);
        register = findViewById(R.id.RegisterBTN);

        dbRats = FirebaseDatabase.getInstance();
        registerAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        dbChefs = dbRats.getReference("userChef");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = description.getText().toString();
                Log.i("DESCRIPCION",des);
                Log.i("DESCRIPCION",registerAuth.getCurrentUser().getUid());
                dbChefs.child(userId).child("description").setValue(des);

                Intent intent1 = new Intent(getApplicationContext(),ChefRecipes.class);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }
}

package com.example.ratatouille.Chef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratatouille.Chef.ChefRecipes;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DescripcionChef extends AppCompatActivity {
    TextView años;
    TextView experiencias;
    TextView certificados;
    Button register;
    FirebaseDatabase dbRats;
    DatabaseReference dbUsersChefs;
    FirebaseAuth registerAuth;
    DatabaseReference dbChefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_register2);
        años = findViewById(R.id.textView14);
        experiencias = findViewById(R.id.textView6);
        certificados = findViewById(R.id.textView15);
        register = findViewById(R.id.btn_register3);

        dbRats = FirebaseDatabase.getInstance();
        registerAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        dbChefs = dbRats.getReference("userChef");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exp = experiencias.getText().toString();
                String anos = años.getText().toString();
                String certi = certificados.getText().toString();
                Log.i("DESCRIPCION",registerAuth.getCurrentUser().getUid());
                dbChefs.child(userId).child("experiencia").setValue(exp);
                dbChefs.child(userId).child("años").setValue(anos);
                dbChefs.child(userId).child("certificados").setValue(certi);

                Intent intent1 = new Intent(getApplicationContext(), ChefRecipes.class);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }
}

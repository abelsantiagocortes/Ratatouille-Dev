package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CalificationActivity extends AppCompatActivity {

    RatingBar calificacion;
    EditText comentario;
    Button finalizar;

    boolean chefOrClient = false;

    FirebaseAuth loginAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calification);
        comentario = findViewById(R.id.Comentario);
        calificacion = findViewById(R.id.ratingBar);
        finalizar = findViewById(R.id.Finalizar);
        Intent intent = getIntent();

        loginAuth = FirebaseAuth.getInstance();
        FirebaseUser user = loginAuth.getCurrentUser();
        String uid= user.getUid();
        Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
        queryChef.addListenerForSingleValueEvent(valueEventListenerKnow);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Cantidad Estrellas: " + calificacion.getRating(), Toast.LENGTH_LONG).show();
            }
        });
    }

    ValueEventListener valueEventListenerKnow = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserChef c = snapshot.getValue(UserChef.class);
                    comentario.setText("Chef");
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

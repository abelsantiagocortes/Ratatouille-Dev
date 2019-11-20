package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ratatouille.Chef.ChefActivity;
import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Calificacion;
import com.example.ratatouille.Class.Solicitud;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CalificationActivity extends AppCompatActivity {

    RatingBar calificacion;
    EditText comentario;
    Button finalizar;

    Intent intentAnterior;

    FirebaseAuth loginAuth;
    FirebaseDatabase dbRats;
    DatabaseReference dbCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calification);
        comentario = findViewById(R.id.Comentario);
        calificacion = findViewById(R.id.ratingBar);
        finalizar = findViewById(R.id.Finalizar);
        intentAnterior = getIntent();

        loginAuth = FirebaseAuth.getInstance();
        dbRats = FirebaseDatabase.getInstance();
        dbCalificacion = dbRats.getReference("calificacion");
        FirebaseUser user = loginAuth.getCurrentUser();
        String uid= user.getUid();
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"Cantidad Estrellas: " + calificacion.getRating(), Toast.LENGTH_LONG).show();
                crearCalificacion();
            }
        });
    }

    private void crearCalificacion() {
        Intent intent;
        Calificacion cali;
        Agree solicitud = (Agree) intentAnterior.getExtras().getSerializable("solicitud");
        float estrellas = calificacion.getRating();
        String comen = comentario.getText().toString();
        String de = loginAuth.getCurrentUser().getUid();
        String para = "";
        String servicio = solicitud.getSolicitudId();
        if(de.equals(solicitud.getIdChef())){
            intent = new Intent(this, ChefActivity.class);
            para = solicitud.getIdClient();
            cali = new Calificacion(para,de,comen,estrellas,servicio);
            dbCalificacion.child("cliente").child(cali.getIdSolicitud()).setValue(cali);
        }else{
            intent = new Intent(this,MainActivity.class);
            para = solicitud.getIdChef();
            cali = new Calificacion(para,de,comen,estrellas,servicio);
            dbCalificacion.child("chef").child(cali.getIdSolicitud()).setValue(cali);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}

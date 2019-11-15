package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.R;

import java.io.Serializable;

public class Agreement extends AppCompatActivity {

    ImageView btnRecipe;
    Button btnIngreds;
    Recipe receta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        btnRecipe= findViewById(R.id.btn_add);
        btnIngreds=findViewById(R.id.buttonInge);

        receta = (Recipe) getIntent().getSerializableExtra("REP");

        btnRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent( getApplicationContext(), Agreement.class );
                intent2.putExtra("REP", (Serializable) receta);
                startActivity(intent2);
            }
        });
        btnIngreds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent( getApplicationContext(), IngredsAgreement.class );
                intent2.putExtra("REP", (Serializable) receta);
                startActivity(intent2);
            }
        });

    }
}

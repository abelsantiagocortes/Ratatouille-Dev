package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.R;

import java.io.Serializable;

public class AgreementClass extends AppCompatActivity {

    ImageView btnRecipe;
    Button btnIngreds;
    UserChef chefSolicitado;
    Agree acu;
    Button btn_toolsAgr;
    TextView txtreceta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        acu = new Agree();
        btnIngreds=findViewById(R.id.buttonInge);
        btn_toolsAgr =findViewById(R.id.btn_toolsAgr);
        txtreceta =findViewById(R.id.txtreceta);


        acu=((Agree) getIntent().getSerializableExtra("Agreement"));


        txtreceta.setText(acu.getReceta().getName());

        btnIngreds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent( getApplicationContext(), IngredsAgreement.class );
                intent2.putExtra("Agreement", (Serializable) acu);
                startActivity(intent2);

            }
        });
        btn_toolsAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent( getApplicationContext(), ToolsAgreement.class );
                intent2.putExtra("Agreement", (Serializable) acu);
                startActivity(intent2);

            }
        });

    }
}

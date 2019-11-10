package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ChefRecipes extends AppCompatActivity {

    FirebaseDatabase dbRats;
    DatabaseReference dbUsersChefs;
    FirebaseAuth registerAuth;
    DatabaseReference dbChefs;
    DatabaseReference dbRecipes;

    GridLayout gridLayout;
    TextView txt_showselected;
    Button btnRegis;
    List<String> recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_recipes);

        gridLayout = (GridLayout) findViewById(R.id.grid_layoutRecipe);
        txt_showselected = (TextView) findViewById(R.id.txt_showselectedR);
        btnRegis= findViewById(R.id.btn_registrar);
        recipe = new ArrayList<String>();


        dbRats = FirebaseDatabase.getInstance();
        registerAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        dbChefs = dbRats.getReference("userChef");

        dbRecipes = dbRats.getReference("userChef");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("recipe");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectRecipes((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),ChefActivity.class);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

    }

    //Saca los valores de las recetas para meterlos en la lista que en la que se harán los tags
    private void collectRecipes(Map<String,Object> recipes) {


        //iterate through each recipe, ignoring their UID
        for (Map.Entry<String, Object> entry : recipes.entrySet()){

            //Get recipe map
            Map singleRecipe = (Map) entry.getValue();
            //Get name field and append to list
            recipe.add(singleRecipe.get("name").toString());

        }
        tagComponents();


    }


    void tagComponents()
    {

        //Se crea la cantidad de botones necesarios para representar los tags
        for (int i = 0; i < recipe.size(); i++) {
            //Reset Grid Layout

            // Cantidad de hijos del GridLayout.
            int childCount = gridLayout.getChildCount();

            // Get application context.
            Context context = getApplicationContext();
            // Crea cada boton en el contexto de la Actividad
            final Button tags = new Button(context);

            //Tamaño para los botones de tags
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (104 * scale + 0.5f);

            //Le pone el texto. background, el tipo de texto y el tamaño
            tags.setText(recipe.get(i));
            tags.setBackgroundResource(R.drawable.btn_tag);
            tags.setTextAppearance(getApplicationContext(), R.style.typ_grey);
            tags.setWidth(pixels);

            //Click listener de todos los botones tags
            tags.setOnClickListener(new View.OnClickListener() {
                //Mira si esta clickeado o no
                Boolean click = false;
                Boolean first = false;

                @Override
                public void onClick(View v) {

                    //Si no esta clickeado cambia el estilo y lo pone en el color adecuado
                    if (click == false) {
                        //Se asegura de que no vayan mas de 5 tags

                        tags.setBackgroundResource(R.drawable.btn_high_action);
                        tags.setTextAppearance(getApplicationContext(), R.style.typ_white);

                        click = true;
                        if (txt_showselected.getText().toString().equals(".")) {
                            txt_showselected.setText(tags.getText().toString());

                        } else {
                            txt_showselected.setText(txt_showselected.getText().toString() + "  " + tags.getText().toString());

                        }
                    }

                    else {
                        //Si el boton ya a sido clickeado cambia el estilo y borra lo necesario de los tags del usuario
                        if (first == false) {
                            String withTag = txt_showselected.getText().toString();

                            String withoutTag = withTag.replace(tags.getText().toString(), "");
                            txt_showselected.setText(withoutTag);
                            first = true;


                        } else {
                            String withTag = txt_showselected.getText().toString();

                            String withoutTag = withTag.replace("  " + tags.getText().toString(), "");
                            txt_showselected.setText(withoutTag);

                        }
                        tags.setBackgroundResource(R.drawable.btn_tag);
                        tags.setTextAppearance(getApplicationContext(), R.style.typ_grey);

                        click = false;

                    }

                }
            });

            // Se añade el boton al gridLayout
            gridLayout.addView(tags, childCount);
        }
    }
}

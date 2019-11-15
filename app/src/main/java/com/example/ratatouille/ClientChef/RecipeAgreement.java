package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeAgreement extends AppCompatActivity {

    //Atributos necesarios de Firebase
    FirebaseDatabase dbRats;
    DatabaseReference dbUsersChefs;
    FirebaseAuth registerAuth;
    DatabaseReference dbChefs;

    //Elementos del GUI para inflar
    GridLayout gridLayout;
    TextView txt_showselected;
    Button btnAccept;
    Button btnInfo;

    //Listas manejo de recetas
    List<String> recipe;
    List<Recipe> recipeObj = new ArrayList<>();
    UserChef userC;
    List<String> nameRecipes = new ArrayList<>();
    Boolean max=false;
    Recipe chosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_agreement);

        gridLayout =  findViewById(R.id.grid_layoutRecipe2);
        txt_showselected =  findViewById(R.id.txt_showselectedR2);
        btnAccept= findViewById(R.id.btn_aceptarReceta);
        btnInfo= findViewById(R.id.buttonInfo);

        Query queryRecipe = FirebaseDatabase.getInstance().getReference("recipe");
        queryRecipe.addListenerForSingleValueEvent(valueEventListener1);

        Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo("BgtYQ69dO6XYAVR6PabB48m6co62");
        queryChef.addListenerForSingleValueEvent(valueEventListener2);


        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent2 = new Intent( getApplicationContext(), Agreement.class );

                for(int x=0;x<recipeObj.size();x++)
                {

                        if(txt_showselected.getText().toString().replaceAll("\\s+","").equals(recipeObj.get(x).getName()))
                        {
                            chosen = recipeObj.get(x);
                        }
                }

                        intent2.putExtra("REP", (Serializable) chosen);
                        startActivity(intent2);

            }
        });

    }

    private void recipesChef() {

        for(int x=0;x<recipeObj.size();x++)
        {
            for(int z =0;z<userC.getRecipeIds().size();z++)
            {
                if(userC.getRecipeIds().get(z).equals(recipeObj.get(x).getId()))
                {
                    nameRecipes.add(recipeObj.get(x).getName());
                }
            }
        }

        tagComponents();


    }


    ValueEventListener valueEventListener1= new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    recipeObj.add(recipe);
                }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userC = snapshot.getValue(UserChef.class);
                }
                recipesChef();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };






    void tagComponents()
    {


        //Se crea la cantidad de botones necesarios para representar los tags
        for (int i = 0; i < nameRecipes.size(); i++) {
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
            tags.setText(nameRecipes.get(i));
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

                        if(max==false)
                        {
                            tags.setBackgroundResource(R.drawable.btn_high_action);
                            tags.setTextAppearance(getApplicationContext(), R.style.typ_white);

                            click = true;
                            if (txt_showselected.getText().toString().equals(".")) {
                                txt_showselected.setText(tags.getText().toString());

                            } else {
                                txt_showselected.setText(txt_showselected.getText().toString() + "  " + tags.getText().toString());

                            }
                            max=true;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Solo una receta señora",Toast.LENGTH_SHORT).show();
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
                        max=false;

                    }

                }
            });

            // Se añade el boton al gridLayout
            gridLayout.addView(tags, childCount);
        }
    }
}


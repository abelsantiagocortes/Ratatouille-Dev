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

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.Solicitud;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Client.RecipeDescription;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    DatabaseReference dbAgreements;
    FirebaseAuth registerAuth;
    DatabaseReference dbNotifs;
    FirebaseUser user;


    //Elementos del GUI para inflar
    GridLayout gridLayout;
    TextView txt_showselected;
    Button btnAccept;
    Button btnInfo;
    Agree acuerdo;

    //Listas manejo de recetas
    List<String> recipe;
    List<Recipe> recipeObj = new ArrayList<>();
    UserChef userC;
    List<String> nameRecipes = new ArrayList<>();
    Boolean max=false;
    Recipe chosen;
    UserChef chefSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_agreement);

        gridLayout =  findViewById(R.id.grid_layoutRecipe2);
        txt_showselected =  findViewById(R.id.txt_showselectedR2);
        btnAccept= findViewById(R.id.btn_aceptarReceta);
        btnInfo= findViewById(R.id.buttonInfo);

        chefSelected = (UserChef) getIntent().getSerializableExtra("ChefObj");

        final String chefId = chefSelected.getUserId();

        registerAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();

        Query queryRecipe = FirebaseDatabase.getInstance().getReference("recipe");
        queryRecipe.addListenerForSingleValueEvent(valueEventListener1);

        Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByKey().equalTo(chefId);
        queryChef.addListenerForSingleValueEvent(valueEventListener2);



        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!txt_showselected.getText().toString().equals("")){
                   for(int x=0;x<recipeObj.size();x++)
                   {

                       if(txt_showselected.getText().toString().replaceAll("\\s+","").equals(recipeObj.get(x).getName()))
                       {
                           chosen = recipeObj.get(x);
                       }
                   }
                   Intent intent2 = new Intent( getApplicationContext(), RecipeDescription.class );
                   Bundle bu= new Bundle();
                   bu.putSerializable("Recipe", (Serializable) chosen);
                   bu.putSerializable("Chef", (Serializable) chefSelected);

                   intent2.putExtra("Bundle", bu);

                   startActivity(intent2);
               }else{
                   Toast.makeText(getApplicationContext(),"Debe escoger receta para ver su descripción",Toast.LENGTH_SHORT).show();
               }

            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txt_showselected.getText().toString().equals("")) {

                for(int x=0;x<recipeObj.size();x++)
                {
                    if(txt_showselected.getText().toString().replaceAll("\\s+","").equals(recipeObj.get(x).getName()))
                    {
                        chosen = recipeObj.get(x);
                    }
                }
                        dbRats = FirebaseDatabase.getInstance();
                        dbAgreements = dbRats.getReference("agreements");
                        acuerdo = new Agree();
                        acuerdo.setReceta(chosen);
                        acuerdo.setIdChef(chefId);
                        acuerdo.setIdClient(userId);
                        String id = dbAgreements.push().getKey();
                        acuerdo.setAgreementId(id);
                        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
                        dbNotifs = dbRats.getReference("solicitud");

                        String key=dbNotifs.push().getKey();
                        Solicitud notif = new Solicitud(userId,chefId,key);

                        dbNotifs.child(key).setValue(notif);
                        acuerdo.setSolicitudId(key);

                        dbAgreements.child(id).setValue(acuerdo);



                        Intent intent2 = new Intent( getApplicationContext(), AgreementClass.class );
                        intent2.putExtra("Agreement", (Serializable) acuerdo);
                        startActivity(intent2);

                        if (txt_showselected.getText().toString().replaceAll("\\s+", "").equals(recipeObj.get(x).getName())) {
                            chosen = recipeObj.get(x);
                        }
                    }

                    Intent intent2 = new Intent(getApplicationContext(), AgreementClass.class);
                    acuerdo = new Agree();
                    acuerdo.setReceta(chosen);
                    intent2.putExtra("Agreement", (Serializable) acuerdo);
                    startActivity(intent2);

                }else{
                    Toast.makeText(getApplicationContext(),"Debe escoger receta para continuar",Toast.LENGTH_SHORT).show();
                }
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
                            txt_showselected.setText("");
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


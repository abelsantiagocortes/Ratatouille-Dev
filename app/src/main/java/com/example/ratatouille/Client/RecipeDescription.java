package com.example.ratatouille.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.ClientChef.RecipeAgreement;
import com.example.ratatouille.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.Serializable;

public class RecipeDescription extends AppCompatActivity {

    CircularImageView img_recipe;
    TextView txt_nameRecipe;
    TextView txt_recipeDes;
    TextView txt_recipeUten;
    TextView txt_recipeIng;
    TextView txt_cant;

    Button btn_volver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        //Se inflan elementos a utilizar
        img_recipe=findViewById(R.id.img_recipe);
        txt_nameRecipe=findViewById(R.id.txt_nameRecipe);
        txt_recipeDes=findViewById(R.id.txt_recipeDes);
        txt_recipeUten=findViewById(R.id.txt_recipeUten);
        txt_recipeIng=findViewById(R.id.txt_recipeIng);
        btn_volver=findViewById(R.id.btn_volver);
        txt_cant=findViewById(R.id.cantrats);

        img_recipe=findViewById(R.id.img_recipe);

        final Bundle bu = getIntent().getBundleExtra("Bundle");
        //Solo vuelve , la actividad no hace nada raro
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent( getApplicationContext(), RecipeAgreement.class );
                UserChef chef = new UserChef();
                chef = (UserChef) bu.getSerializable("Chef");
                intent2.putExtra("ChefObj", (Serializable) chef);
                startActivity(intent2);
            }
        });
        //Adquiere los valores
        Recipe recipeDes = new Recipe();
        recipeDes = (Recipe)bu.getSerializable("Recipe");

        txt_recipeDes.setMovementMethod(new ScrollingMovementMethod());
        txt_recipeIng.setMovementMethod(new ScrollingMovementMethod());
        txt_recipeUten.setMovementMethod(new ScrollingMovementMethod());

        //Da valores a los textviews
        txt_nameRecipe.setText(recipeDes.getName());
        txt_recipeDes.setText(recipeDes.getDescription());
        String inges="- ";
        String tools="- ";

        for(int i=0;i<recipeDes.getIngredients().size();i++){
                inges=inges+recipeDes.getIngredients().get(i)+" - ";
        }
        for(int i=0;i<recipeDes.getTools().size();i++){
            tools=tools+recipeDes.getTools().get(i)+" - ";
        }
        txt_recipeIng.setText(inges);
        txt_recipeUten.setText(tools);
        txt_cant.setText(String.valueOf(recipeDes.getPrice()));

    }
}

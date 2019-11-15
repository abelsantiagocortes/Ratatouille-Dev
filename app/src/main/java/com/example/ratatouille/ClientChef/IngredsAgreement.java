package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.R;

import java.util.ArrayList;
import java.util.List;

public class IngredsAgreement extends AppCompatActivity {

    Recipe receta;
    List<String> inge;

    GridLayout gridLayout;
    TextView txt_showselected;
    Button btnRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreds_agreement);

        inge = new ArrayList<>();
        gridLayout =  findViewById(R.id.grid_layoutRecipe2);
        txt_showselected =  findViewById(R.id.txt_showselectedR2);
        receta = (Recipe) getIntent().getSerializableExtra("REP");

        inge = receta.getIngredients();
        tagComponents();
    }


    void tagComponents() {

        //Se crea la cantidad de botones necesarios para representar los tags
        for (int i = 0; i < inge.size(); i++) {
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
            tags.setText(inge.get(i));
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

                        tags.setBackgroundResource(R.drawable.btn_high_action);
                        tags.setTextAppearance(getApplicationContext(), R.style.typ_white);

                        click = true;
                        if (txt_showselected.getText().toString().equals(".")) {
                            txt_showselected.setText(tags.getText().toString());

                        } else {
                            txt_showselected.setText(txt_showselected.getText().toString() + "  " + tags.getText().toString());

                        }
                    } else {
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

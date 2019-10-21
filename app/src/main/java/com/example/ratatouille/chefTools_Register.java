package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class chefTools_Register<tagsFire> extends AppCompatActivity {

    GridLayout gridLayout;
    TextView txt_showselected;
    Button btnNotif;
    DatabaseReference dbUsers;
    DatabaseReference dbTags;
    List<String> tagsFire;
    FirebaseAuth registerAuth;
    int canttags =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_tools__register);
        //Se infla el gridlayout y el textview de los tags
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        txt_showselected = (TextView) findViewById(R.id.txt_showselected);
        btnNotif= findViewById(R.id.button2);
        tagsFire = new ArrayList<String>();

        tagsFire.add("Olla1");
        tagsFire.add("Olla2");
        tagsFire.add("Olla3");
        tagsFire.add("Olla4");
        tagComponents();

    }



    void tagComponents()
    {

        //Se crea la cantidad de botones necesarios para representar los tags
        for (int i = 0; i < tagsFire.size(); i++) {
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
            tags.setText(tagsFire.get(i));
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
                        if(canttags<5){
                            tags.setBackgroundResource(R.drawable.btn_high_action);
                            tags.setTextAppearance(getApplicationContext(), R.style.typ_white);

                            click = true;
                            if (txt_showselected.getText().toString().equals(".")) {
                                txt_showselected.setText(tags.getText().toString());
                                canttags++;
                            } else {
                                txt_showselected.setText(txt_showselected.getText().toString() + "  " + tags.getText().toString());
                                canttags++;
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Maximo 5 Tags. ",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //Si el boton ya a sido clickeado cambia el estilo y borra lo necesario de los tags del usuario
                        if (first == false) {
                            String withTag = txt_showselected.getText().toString();

                            String withoutTag = withTag.replace(tags.getText().toString(), "");
                            txt_showselected.setText(withoutTag);
                            first = true;
                            canttags--;

                        } else {
                            String withTag = txt_showselected.getText().toString();

                            String withoutTag = withTag.replace("  " + tags.getText().toString(), "");
                            txt_showselected.setText(withoutTag);
                            canttags--;
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

    private List<String> getSelectedTags()
    {
        List<String> items = Arrays.asList(txt_showselected.getText().toString().split("\\s*,\\s*,"));
        return items;
    }
}

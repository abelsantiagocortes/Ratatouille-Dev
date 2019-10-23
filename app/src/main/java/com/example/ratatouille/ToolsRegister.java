package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolsRegister<tagsFire> extends AppCompatActivity {

    GridLayout gridLayout;
    TextView txt_showselected;
    Button btnRegis;
    FirebaseDatabase dbRats;
    DatabaseReference dbUsersChefs;
    DatabaseReference dbUsersClients;
    DatabaseReference dbTools;
    List<String> tools;
    FirebaseAuth registerAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_tools__register);
        //Se infla el gridlayout y el textview de los tags
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        txt_showselected = (TextView) findViewById(R.id.txt_showselected);
        btnRegis= findViewById(R.id.button2);
        tools = new ArrayList<String>();

        dbRats = FirebaseDatabase.getInstance();
        registerAuth = FirebaseAuth.getInstance();
        dbTools =  dbRats.getReference("tools");

        // Read Tags Every Time is Updated
        dbTools.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tools.clear();
                for(DataSnapshot tagSnapshot : dataSnapshot.getChildren())
                {
                    String itemTool = tagSnapshot.getValue().toString();
                    tools.add(itemTool);

                }
                //Reset the GridLayouts
                gridLayout.removeAllViews();
                tagComponents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerToolsDB();
                Intent intent = getIntent();
                if(intent.getStringExtra("type").equals("clienti")){
                    Intent intent1 = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent1);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }else if(intent.getStringExtra("type").equals("chefsi")){
                    Intent intent1 = new Intent(getApplicationContext(),DescripcionChef.class);
                    startActivity(intent1);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }

            }
        });

    }



    void tagComponents()
    {

        //Se crea la cantidad de botones necesarios para representar los tags
        for (int i = 0; i < tools.size(); i++) {
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
            tags.setText(tools.get(i));
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

    void registerToolsDB()
    {
        List<String> items = Arrays.asList(txt_showselected.getText().toString().split("\\s*,\\s*,"));
        Intent intent = getIntent();
        FirebaseUser user = registerAuth.getCurrentUser();
        String uid= user.getUid();
        if(intent.getStringExtra("type").equals("chefsi"))
        {
            dbUsersChefs =  dbRats.getReference("userChef");
            dbUsersChefs.child(uid).child("tools").setValue(items);
        }
        else if (intent.getStringExtra("type").equals("clienti"))
        {
            dbUsersClients =  dbRats.getReference("userClient");
            dbUsersClients.child(uid).child("tools").setValue(items);
            Intent intenti = new Intent(getApplicationContext(),Home.class);
            startActivity(intenti);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }


    }


}

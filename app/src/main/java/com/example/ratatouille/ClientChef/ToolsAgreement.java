package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Recipe;
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
import java.util.Arrays;
import java.util.List;

public class ToolsAgreement extends AppCompatActivity {

    Recipe receta;
    Agree acu;
    List<String> uten;
    FirebaseAuth loginAuth;

    GridLayout gridLayout;
    TextView txt_showselected;
    DatabaseReference dbAgreements;
    Button btn_utenAgree;
    FirebaseAuth registerAuth;
    FirebaseDatabase dbRats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_agreement);
        uten = new ArrayList<>();
        gridLayout =  findViewById(R.id.grid_layoutToolsAgre);
        txt_showselected =  findViewById(R.id.txt_showselectedTools);
        acu = new Agree();
        acu = (Agree) getIntent().getSerializableExtra("Agreement");
        receta = acu.getReceta();
        btn_utenAgree =  findViewById(R.id.btn_utenAgree);
        registerAuth = FirebaseAuth.getInstance();
        dbRats = FirebaseDatabase.getInstance();

        btn_utenAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAuth = FirebaseAuth.getInstance();
                FirebaseUser user = loginAuth.getCurrentUser();
                String uid= user.getUid();
                Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
                Query queryClient = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(uid);
                queryClient.addListenerForSingleValueEvent(valueEventListener1);
                queryChef.addListenerForSingleValueEvent(valueEventListener2);
            }
        });
        uten = receta.getTools();
        tagComponents();

    }

    void tagComponents() {

        //Se crea la cantidad de botones necesarios para representar los tags
        for (int i = 0; i < uten.size(); i++) {
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
            tags.setText(uten.get(i));
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

    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                List<String> itemsSpaces = Arrays.asList(txt_showselected.getText().toString().split("\\W+"));
                List<String> itemsNoSpaces = new ArrayList<>();

                for(int i=0;i<itemsSpaces.size();i++){
                    if(!itemsSpaces.get(i).equals(""))
                        itemsNoSpaces.add(itemsSpaces.get(i));
                }
                dbAgreements =  dbRats.getReference("agreements");
                dbAgreements.child(acu.getAgreementId()).child("toolsClient").setValue(itemsNoSpaces);
                acu.setToolsClient(itemsNoSpaces);
                Intent intent2 = new Intent( getApplicationContext(), AgreementClass.class );
                intent2.putExtra("Agreement", (Serializable) acu);
                startActivity(intent2);
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
                List<String> itemsSpaces = Arrays.asList(txt_showselected.getText().toString().split("\\W+"));
                List<String> itemsNoSpaces = new ArrayList<>();

                for(int i=0;i<itemsSpaces.size();i++){
                    if(!itemsSpaces.get(i).equals(""))
                        itemsNoSpaces.add(itemsSpaces.get(i));
                }
                dbAgreements =  dbRats.getReference("agreements");
                dbAgreements.child(acu.getAgreementId()).child("ingreChef").setValue(itemsNoSpaces);
                acu.setIngreClient(itemsNoSpaces);
                Intent intent2 = new Intent( getApplicationContext(), AgreementClass.class );
                intent2.putExtra("Agreement", (Serializable) acu);
                startActivity(intent2);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

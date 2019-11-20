package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.Client.Home;
import com.example.ratatouille.R;
import com.example.ratatouille.adapters.PopUpAdapter;
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

public class AgreementClass extends AppCompatActivity {

    ImageView btnRecipe;
    Button btnIngreds;
    UserChef chefSolicitado;
    DatabaseReference dbAgreements;
    Agree acu;
    Button btn_toolsAgr;
    TextView txtreceta;
    FirebaseAuth loginAuth;
    CheckBox chek1;
    CheckBox chek2;
    FirebaseAuth current;
    FirebaseDatabase dbRats;
    Button confri;
    TextView rats;
    TextView rr;
    DatabaseReference dbUsersClients;
    DatabaseReference dbUsersChefs;
    int cant ;
    int cant2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        acu = new Agree();
        btnIngreds=findViewById(R.id.buttonInge);
        btn_toolsAgr =findViewById(R.id.btn_toolsAgr);
        txtreceta =findViewById(R.id.txtreceta);
        chek1=findViewById(R.id.checkClient);
        chek2=findViewById(R.id.checkChef);
        confri=findViewById(R.id.btn_confirmar);
        dbRats = FirebaseDatabase.getInstance();
        rats=findViewById(R.id.titleRats);
        rr=findViewById(R.id.ratcss);



        acu=((Agree) getIntent().getExtras().getSerializable("Agreement"));

        Query queryAgree = FirebaseDatabase.getInstance().getReference("agreements").orderByChild("agreementId").equalTo(acu.getAgreementId());
        queryAgree.addValueEventListener(valueEventListener);

        rr.setText(String.valueOf(acu.getReceta().getPrice()));


        txtreceta.setText(acu.getReceta().getName());

        loginAuth = FirebaseAuth.getInstance();
        FirebaseUser user = loginAuth.getCurrentUser();
        String uid= user.getUid();
        Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
        Query queryChef2 = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(acu.getIdChef());
        Query queryClient = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(uid);
        queryClient.addListenerForSingleValueEvent(valueEventListener1);
        queryChef.addListenerForSingleValueEvent(valueEventListener2);
        queryChef2.addListenerForSingleValueEvent(valueEventListener3);


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

        chek1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chek1.isChecked())
                {
                    dbAgreements =  dbRats.getReference("agreements");
                    dbAgreements.child(acu.getAgreementId()).child("clienteAccept").setValue(true);
                    acu.setClienteAccept(true);
                }
                else
                {
                    dbAgreements =  dbRats.getReference("agreements");
                    dbAgreements.child(acu.getAgreementId()).child("clienteAccept").setValue(false);
                    acu.setClienteAccept(false);
                }
            }
        });

        chek2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chek2.isChecked())
                {
                    dbAgreements =  dbRats.getReference("agreements");
                    dbAgreements.child(acu.getAgreementId()).child("chefAccept").setValue(true);
                    acu.setChefAccept(true);
                }
                else
                {
                    dbAgreements =  dbRats.getReference("agreements");
                    dbAgreements.child(acu.getAgreementId()).child("chefAccept").setValue(false);
                    acu.setChefAccept(false);
                }

            }
        });

        confri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = FirebaseAuth.getInstance();
                FirebaseUser currentUser = current.getCurrentUser();
                String userId = currentUser.getUid();
                dbUsersClients = dbRats.getReference("userClient");
                dbUsersChefs = dbRats.getReference("userChef");
                cant= cant - acu.getReceta().getPrice();
                cant2= cant2+ acu.getReceta().getPrice();

                dbUsersClients.child(acu.getIdClient()).child("cantRats").setValue(cant);
                dbUsersChefs.child(acu.getIdChef()).child("cantRats").setValue(cant2);

                Bundle bund = new Bundle();

                String msn = new String("Gracias por Confirmar tu Orden  \n \n \n  ");
                String btnMsn = new String("Ver Mapa");
                String activityName = new String("Home");

                Intent intent= new Intent(getApplicationContext(), PopUpAdapter.class);

                bund.putString("mensaje", msn);
                bund.putString("contenidoBoton", btnMsn);
                bund.putString("sender", activityName );
                bund.putSerializable("Agreement", acu);
                bund.putString("Viene",getIntent().getExtras().getString("Viene"));
                intent.putExtras(bund);

                startActivity(intent);

            }
        });

        rats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent( getApplicationContext(), Home.class );
                startActivity(intent2);

            }
        });

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Agree acuerdo = snapshot.getValue(Agree.class);
                    ArrayList<String> arr = new ArrayList<String>(acuerdo.getIngreClient());

                    ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, arr);
                    ListView listView =  findViewById(R.id.inClientList);
                    listView.setAdapter(adapter);

                    ArrayList<String> arr2 = new ArrayList<String>(acuerdo.getToolsClient());


                    ArrayAdapter adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, arr2);
                    ListView listView2 =  findViewById(R.id.toolsClientList);
                    listView2.setAdapter(adapter2);


                    ArrayList<String> arr3 = new ArrayList<String>(acuerdo.getIngreChef());


                    ArrayAdapter adapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, arr3);

                    ListView listView3 =  findViewById(R.id.inChefList);

                    listView3.setAdapter(adapter3);


                    ArrayList<String> arr4 = new ArrayList<String>(acuerdo.getToolsChef());


                    ArrayAdapter adapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, arr4);
                    ListView listView4 =  findViewById(R.id.toolsChefList);
                    listView4.setAdapter(adapter4);

                    if(acuerdo.isChefAccept())
                    {
                        chek2.setChecked(true);
                    }
                    else
                    {
                        chek2.setChecked(false);
                    }
                    if(acuerdo.isClienteAccept())
                    {
                        chek1.setChecked(true);
                    }
                    else {
                        chek1.setChecked(false);
                    }

                    if(chek1.isChecked()&&chek2.isChecked())
                    {
                        confri.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        confri.setVisibility(View.GONE);
                    }

                }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                chek2.setEnabled(false);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserClient c = snapshot.getValue(UserClient.class);
                    cant=c.getCantRats();
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
                chek1.setEnabled(false);
                confri.setVisibility(View.GONE);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ValueEventListener valueEventListener3 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserChef c = snapshot.getValue(UserChef.class);
                    cant2=c.getCantRats();
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

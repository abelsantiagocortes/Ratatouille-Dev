package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.Recipe;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

        Query queryAgree = FirebaseDatabase.getInstance().getReference("agreements").orderByChild("agreementId").equalTo(acu.getAgreementId());
        queryAgree.addValueEventListener(valueEventListener);


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
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Agree acuerdo = snapshot.getValue(Agree.class);
                    ArrayList<String> arr = new ArrayList<String>(acuerdo.getIngreClient());
                    String str[] = new String[arr.size()];
                    // ArrayList to Array Conversion
                    for (int j = 0; j < arr.size(); j++) {

                        // Assign each value to String array
                        str[j] = arr.get(j);
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, str);
                    ListView listView =  findViewById(R.id.inClientList);
                    listView.setAdapter(adapter);

                    ArrayList<String> arr2 = new ArrayList<String>(acuerdo.getToolsClient());
                    String str2[] = new String[arr.size()];
                    // ArrayList to Array Conversion
                    for (int j = 0; j < arr2.size(); j++) {

                        // Assign each value to String array
                        str2[j] = arr2.get(j);
                    }

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


                }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

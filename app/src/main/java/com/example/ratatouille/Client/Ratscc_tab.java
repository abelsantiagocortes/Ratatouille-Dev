package com.example.ratatouille.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Ratscc_tab extends Fragment {

    TextView txt_cantRats;
    TextView txt_newCant;
    Button btn_addRats;
    FirebaseAuth current;
    int cant;
    //Atributos necesarios de Firebase
    FirebaseDatabase dbRats;
    DatabaseReference dbUsersClients;
    DatabaseReference dbClients;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_ratscc,container,false);
        txt_cantRats =view.findViewById(R.id.txt_cantRats);
        btn_addRats =view.findViewById(R.id.btn_addRats);
        txt_newCant =view.findViewById(R.id.txt_newCant);

        dbRats = FirebaseDatabase.getInstance();
        current = FirebaseAuth.getInstance();

        FirebaseUser currentUser = current.getCurrentUser();
        String userId = currentUser.getUid();
        btn_addRats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_newCant.getText().toString().equals("")){
                    FirebaseUser currentUser = current.getCurrentUser();
                    String userId = currentUser.getUid();
                    dbUsersClients = dbRats.getReference("userClient");
                    int newCant=cant+ Integer.parseInt(txt_newCant.getText().toString());
                    dbUsersClients.child(userId).child("cantRats").setValue(newCant);
                    txt_cantRats.setText(String.valueOf(newCant));
                    cant=newCant;
                    txt_newCant.setText("");
                }

            }
        });

        dbClients = dbRats.getReference("userClient");

        Query query = FirebaseDatabase.getInstance().getReference("userClient")
                .orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(valueEventListener);


        return view;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserClient client = snapshot.getValue(UserClient.class);
                    cant=client.getCantRats();
                }
                txt_cantRats.setText(String.valueOf(cant));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

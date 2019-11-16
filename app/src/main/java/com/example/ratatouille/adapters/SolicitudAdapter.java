package com.example.ratatouille.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.ClientChef.AgreementClass;
import com.example.ratatouille.R;
import com.example.ratatouille.Class.Solicitud;
import com.example.ratatouille.Class.UserClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolicitudAdapter extends ArrayAdapter<Solicitud> {

    String nombre;
    Solicitud solicitud;
    Agree acuerdito;

    public SolicitudAdapter(Context context, List<Solicitud> users) {
        super(context, 0, users);
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {


        final int pos = position;
        // Get the data item for this position
        acuerdito= new Agree();

        solicitud = getItem(pos);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.solicitud_item, parent, false);
        }


        Query queryChefURL = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(solicitud.getIdCliente());
        final View finalConvertView = convertView;
        queryChefURL.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dir : dataSnapshot.getChildren()){
                        View temp= finalConvertView;
                        UserClient noti = dir.getValue(UserClient.class);
                        nombre = noti.getName();
                        TextView cliente = finalConvertView.findViewById(R.id.Cliente);
                        cliente.setText("Cliente: " +  nombre);

                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("LOGINFAILED","CHEF" );
            }
        });

        TextView cliente = convertView.findViewById(R.id.Cliente);
        cliente.setText("Cliente: " +  nombre);
        TextView titulo = convertView.findViewById(R.id.Titulo);
        titulo.setText("Solicitud #" + pos);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query queryRecipe = FirebaseDatabase.getInstance().getReference("agreements").orderByChild("solicitudId").equalTo(solicitud.getIdSolicitud());
                queryRecipe.addListenerForSingleValueEvent(valueEventListener1);

            }
        });


        return convertView;

    }
    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for(DataSnapshot dir : dataSnapshot.getChildren()){
                    acuerdito = dir.getValue(Agree.class);
                    Intent intent2 = new Intent( getContext(), AgreementClass.class );
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("Agreement", (Serializable) acuerdito);
                    getContext().startActivity(intent2);
                }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}

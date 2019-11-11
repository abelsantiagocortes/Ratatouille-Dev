package com.example.ratatouille.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ratatouille.R;
import com.example.ratatouille.Class.Solicitud;
import com.example.ratatouille.Class.UserClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SolicitudAdapter extends ArrayAdapter<Solicitud> {

    String nombre;
    public SolicitudAdapter(Context context, List<Solicitud> users) {
        super(context, 0, users);
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Solicitud solicitud = getItem(position);

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
        titulo.setText("Solicitud #" + position);


        return convertView;

    }

}

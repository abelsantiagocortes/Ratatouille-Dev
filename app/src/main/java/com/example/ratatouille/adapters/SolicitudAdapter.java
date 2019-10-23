package com.example.ratatouille.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ratatouille.R;
import com.example.ratatouille.Solicitud;

import java.util.ArrayList;
import java.util.List;

public class SolicitudAdapter extends ArrayAdapter<Solicitud> {

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
        TextView cliente = convertView.findViewById(R.id.Cliente);
        cliente.setText("Cliente: " + solicitud.getNombreCliente());
        TextView titulo = convertView.findViewById(R.id.Titulo);
        titulo.setText("Solicitud #" + position);
        return convertView;

    }

}

package com.example.ratatouille.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.ratatouille.R;

public class Foodplates_tab extends Fragment {

    Button btn_break;
    Button btn_lunch;
    Button btn_dinner;
    Button btn_vege;
    Button btn_protein;
    Button btn_dessert;
    Button btn_col;
    Button btn_foreign;
    Button btn_cocktails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_foodplates,container,false);

        btn_break= view.findViewById(R.id.btn_break);
        btn_lunch= view.findViewById(R.id.btn_lunch);
        btn_dinner= view.findViewById(R.id.btn_dinner);
        btn_vege= view.findViewById(R.id.btn_vege);
        btn_protein= view.findViewById(R.id.btn_protein);
        btn_dessert= view.findViewById(R.id.btn_dessert);
        btn_col= view.findViewById(R.id.btn_col);
        btn_foreign= view.findViewById(R.id.btn_foreign);
        btn_cocktails= view.findViewById(R.id.btn_cocktails);

        btn_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_vege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_protein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_foreign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_cocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(getContext(),ChefbyFood.class);
                in.putExtra("foodType","dinner");
                startActivity(in);
            }
        });


        return view;
    }
}
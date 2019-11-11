package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ratatouille.Chef.ChefRegister;
import com.example.ratatouille.Client.ClientRegister;
import com.example.ratatouille.R;

public class Register extends AppCompatActivity {


    ImageView client;
    ImageView chef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        client= (ImageView)findViewById(R.id.imageView3);
        chef= (ImageView)findViewById(R.id.imageView);

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(view.getContext(), ClientRegister.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(view.getContext(), ChefRegister.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }
}

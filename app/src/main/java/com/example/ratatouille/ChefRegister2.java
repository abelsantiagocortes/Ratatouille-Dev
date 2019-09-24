package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ChefRegister2 extends AppCompatActivity {

    Spinner sp;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_register2);

        String[] items = new String[]{"1", "2", "three"};
        sp = (Spinner)findViewById(R.id.spinner);





    }

}

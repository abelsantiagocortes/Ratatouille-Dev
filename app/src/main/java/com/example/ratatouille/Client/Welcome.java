package com.example.ratatouille.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ratatouille.Client.Home;
import com.example.ratatouille.R;

public class Welcome extends AppCompatActivity {

    TextView texti;
    Button wel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        texti=(TextView)findViewById(R.id.textView12);
        wel=(Button)findViewById(R.id.button25) ;
        wel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(view.getContext(), Home.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        Bundle bundle = getIntent().getExtras();
        texti.setText(bundle.getString("name"));
    }
}

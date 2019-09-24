package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClientRegister extends AppCompatActivity {

    Button register;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        register=(Button)findViewById(R.id.btn_register2);
        txt=(TextView)findViewById(R.id.textView5);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), Welcome.class);
                in.putExtra("name", txt.getText().toString());
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

    }
}

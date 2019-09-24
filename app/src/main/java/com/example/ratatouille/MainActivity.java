package com.example.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread tempo= new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent= new Intent(getApplicationContext(),LandingPage.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

            }
        };
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        tempo.start();




    }
}

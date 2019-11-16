package com.example.ratatouille.ClientChef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.ratatouille.Client.Home;
import com.example.ratatouille.ClientChef.LandingPage;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressbar;
    private int progreso = 0;

    private int delayMillis = 30;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            public void run() {
                while (progreso < 100) {
                    progreso += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressbar.setProgress(progreso);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(delayMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Progreso","Terminado");

                FirebaseAuth fbAuth= FirebaseAuth.getInstance();

                FirebaseUser user = fbAuth.getCurrentUser();
                if(user==null)
                {
                    Intent intent= new Intent(getApplicationContext(), LandingPage.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                }else{
                    Intent intent2 = new Intent(getBaseContext(), Home.class);
                    startActivity(intent2);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                }

                finish();


            }
        }).start();
    }
}

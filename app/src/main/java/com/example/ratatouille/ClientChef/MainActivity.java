package com.example.ratatouille.ClientChef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.ratatouille.Chef.ChefActivity;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.Client.Home;
import com.example.ratatouille.ClientChef.LandingPage;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //ProgressBar progressbar;
    private int progreso = 0;
    private FirebaseAuth loginAuth;

    private int delayMillis = 30;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //progressbar = findViewById(R.id.progressBar);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    // After 5 seconds redirect to another intent

                    FirebaseAuth fbAuth= FirebaseAuth.getInstance();

                    FirebaseUser user = fbAuth.getCurrentUser();
                    if(user!=null)
                    {
                        loginAuth = FirebaseAuth.getInstance();
                        user = loginAuth.getCurrentUser();
                        String uid= user.getUid();
                        Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
                        Query queryClient = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(uid);
                        queryChef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserChef chefsito = dataSnapshot.getValue(UserChef.class);
                                if(chefsito!=null) {

                                    Intent intent = new Intent(getBaseContext(), ChefActivity.class);
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("LOGINFAILED","CHEF" );
                            }
                        });

                        queryClient.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserClient clientito = dataSnapshot.getValue(UserClient.class);
                                if(clientito!=null) {

                                    Intent intent = new Intent(getBaseContext(), Home.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("LOGINFAILED","CLIENTE");
                            }
                        });

                    }else{
                        Intent intent2 = new Intent(getBaseContext(), LandingPage.class);
                        startActivity(intent2);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                    }

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }
}

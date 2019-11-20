package com.example.ratatouille.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratatouille.Class.Agree;
import com.example.ratatouille.Map.MapsActivityOSM;
import com.example.ratatouille.Map.MapsActivityOSM_Client;
import com.example.ratatouille.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PopUpAdapter extends AppCompatActivity {

    TextView contain;
    Button boton;
    FirebaseAuth loginAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme( R.style.pop_up);
        setContentView(R.layout.activity_pop__up);

        contain = (TextView) findViewById( R.id.popContent );
        boton = ( Button ) findViewById( R.id.popButton );;

        final Bundle contenidos = getIntent( ).getExtras( );

        contain.setText( contenidos.getString( "mensaje" ) );

        boton.setText( contenidos.getString( "contenidoBoton" ) );


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int)(width*.75), (int)(height*.3) );

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senderNew( contenidos.getString( "sender" ) );
                finish();
            }
        });
    }

    private void senderNew( String sendActivity )
    {

        loginAuth = FirebaseAuth.getInstance();
        FirebaseUser user = loginAuth.getCurrentUser();
        String uid= user.getUid();
        Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
        queryChef.addListenerForSingleValueEvent(valueEventListener2);

        Intent intent = new Intent(getBaseContext(), MapsActivityOSM.class);
        Bundle bund = new Bundle();
        bund.putSerializable("Agreement",getIntent().getSerializableExtra("Agreement"));
        intent.putExtras(bund);
        startActivity(intent);

        /*
        String Viene = getIntent().getExtras().getString("Viene");
        System.out.println(sendActivity);
        String activityToStart;
        if(Viene.equals("CHEF")){
            activityToStart = "com.example.ratatouille.Map.MapsActivityOSM";
        }else{
            activityToStart = "com.example.ratatouille.Map.MapsActivityOSM_Client";
        }
        try {
            Class<?> aac = Class.forName(activityToStart);
            Intent intent = new Intent(this, aac);
            Bundle bund = new Bundle();
            bund.putSerializable("Agreement",getIntent().getSerializableExtra("Agreement"));
            intent.putExtras(bund);
            startActivity(intent);
        } catch (ClassNotFoundException ignored) {
        }
        /
         */
    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                Intent intent = new Intent(getBaseContext(), MapsActivityOSM_Client.class);
                Bundle bund = new Bundle();
                bund.putSerializable("Agreement",getIntent().getSerializableExtra("Agreement"));
                intent.putExtras(bund);
                startActivity(intent);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}

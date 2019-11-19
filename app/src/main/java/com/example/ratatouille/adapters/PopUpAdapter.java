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
import com.example.ratatouille.R;

public class PopUpAdapter extends AppCompatActivity {

    TextView contain;
    Button boton;
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
        System.out.println(sendActivity);
        String activityToStart = "com.example.ratatouille.Map.MapsActivityOSM";
        try {
            Class<?> aac = Class.forName(activityToStart);
            Intent intent = new Intent(this, aac);
            Bundle bund = new Bundle();
            bund.putSerializable("Agreement",getIntent().getSerializableExtra("Agreement"));
            intent.putExtras(bund);
            startActivity(intent);
        } catch (ClassNotFoundException ignored) {
        }
    }
}

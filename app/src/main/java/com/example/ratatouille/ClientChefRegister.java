package com.example.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClientChefRegister extends AppCompatActivity {

    EditText name;
    EditText age;
    EditText address;
    DatabaseReference dbChefs;
    DatabaseReference dbClients;
    FirebaseAuth registerAuth;
    Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register2);

        name=findViewById(R.id.etxtName);
        age = findViewById(R.id.etxtName2);
        btnReg=findViewById(R.id.btn_register4);
        address = findViewById(R.id.etxtName3);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        dbChefs = dbRats.getReference("userChef");
        dbClients = dbRats.getReference("userClient");
        registerAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm())
                registerChef();
                Intent in = new Intent(getApplicationContext(), chefTools_Register.class);
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        String name_value = name.getText().toString() ;
        if (TextUtils.isEmpty(name_value)) {
            name.setError("Required.");
            valid = false;
        } else {
            name.setError(null);
        }
        String age_value = age.getText().toString() ;
        if (TextUtils.isEmpty(age_value)) {
            age.setError("Required.");
            valid = false;
        } else {
            age.setError(null);
        }
        String dir_value = address.getText().toString() ;
        if (TextUtils.isEmpty(dir_value)) {
            address.setError("Required.");
            valid = false;
        } else {
            address.setError(null);
        }

        return valid;
    }

    void registerChef()
    {
        String name_value = name.getText().toString() ;
        String dir_value = address.getText().toString() ;
        int age_value = Integer.parseInt(age.getText().toString()) ;

        FirebaseUser currentUser = registerAuth.getCurrentUser();
        String userId = currentUser.getUid();

        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals("chef"))
        {
            UserChef user = new UserChef(name_value,dir_value,age_value);
            dbChefs.child(userId).setValue(user);
        }
        else if (intent.getStringExtra("type").equals("client"))
        {
            UserClient user = new UserClient(name_value,dir_value,age_value);
            dbClients.child(userId).setValue(user);
        }



    }


}

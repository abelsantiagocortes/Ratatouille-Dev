package com.example.ratatouille.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ratatouille.ClientChef.ClientChefRegister;
import com.example.ratatouille.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ClientRegister extends AppCompatActivity {

    Button register;
    EditText email;
    EditText password;
    String emailClient;
    String passwordClient;
    FirebaseAuth registerAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        register=findViewById(R.id.btn_register56);
        email=findViewById(R.id.textView38);
        password=findViewById(R.id.textView56);

        registerAuth=FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }
    private void register()
    {
        if(validateForm())
        {
            emailClient = email.getText().toString();
            passwordClient = password.getText().toString();
            registerAuth.createUserWithEmailAndPassword(emailClient, passwordClient)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent in = new Intent(getApplicationContext(), ClientChefRegister.class);
                                in.putExtra("type","client");
                                startActivity(in);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                            } else {

                                Toast.makeText(getApplicationContext(), "Registration Failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    private boolean validateForm() {
        boolean valid = true;
        emailClient = email.getText().toString();
        passwordClient = password.getText().toString();

        if (TextUtils.isEmpty(emailClient)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(passwordClient)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        };
        if(!isEmailValid(emailClient)){
            email.setText("");
            email.setError("Wrong Email.");
            valid = false;
        }
        else {
            email.setError(null);
        }

        return valid;
    }

    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||
                !email.contains(".") ||
                email.length() < 5)
            return false;
        return true;
    }

}

package com.example.ratatouille.Chef;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChefRegister extends AppCompatActivity {

    Button but;
    EditText email;
    EditText password;
    String emailChef;
    String passwordChef;
    DatabaseReference dbChefs;
    FirebaseAuth registerAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_register);

        but=findViewById(R.id.btn_register4);
        email=findViewById(R.id.textView8);
        password=findViewById(R.id.textView9);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        dbChefs= dbRats.getReference("userChef");
        registerAuth=FirebaseAuth.getInstance();

        but.setOnClickListener(new View.OnClickListener() {
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
            emailChef = email.getText().toString();
            passwordChef = password.getText().toString();
            registerAuth.createUserWithEmailAndPassword(emailChef, passwordChef)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent in = new Intent(getApplicationContext(), ClientChefRegister.class);
                                in.putExtra("type","chef");
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
        emailChef = email.getText().toString();
        passwordChef = password.getText().toString();

        if (TextUtils.isEmpty(emailChef)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(passwordChef)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        };
        if(!isEmailValid(emailChef)){
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

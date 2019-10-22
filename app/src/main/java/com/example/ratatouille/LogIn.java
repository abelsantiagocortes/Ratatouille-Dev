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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth loginAuth;
    private static final String TAG = "LogIn";

    Button btn_logins;
    EditText frm_email;
    EditText frm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btn_logins=findViewById(R.id.btn_loginN);
        frm_email=findViewById(R.id.frm_email);
        frm_password=findViewById(R.id.frm_password);
        loginAuth = FirebaseAuth.getInstance();


        btn_logins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(frm_email.getText().toString(), frm_password.getText().toString());
            }
        });
    }

    private void logInUser(FirebaseUser currentUser){
        Intent intent;
        if(currentUser!=null){

                FirebaseUser user = loginAuth.getCurrentUser();
                String uid= user.getUid();
                Query queryChef = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
                Query queryClient = FirebaseDatabase.getInstance().getReference("userClient").orderByChild("userId").equalTo(uid);
                queryChef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserChef chefsito = dataSnapshot.getValue(UserChef.class);
                        if(chefsito!=null) {
                            Log.i("LOGIN", "CHEF" + chefsito.toString());
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
                            Log.i("LOGIN", "CLIENTE" + clientito.toString());
                            Intent intent = new Intent(getBaseContext(), Home.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("LOGINFAILED","CLIENTE");
                    }
                });
        } else {
            frm_email.setText("");
            frm_password.setText("");
        }
    }

    //Field Validation

    private boolean validateForm() {
        boolean valid = true;
        String email = frm_email.getText().toString();
        if(!isEmailValid(email)){
            frm_email.setText("");
            frm_email.setError("Required.");
            valid = false;
        }
        else {
            frm_email.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            frm_email.setError("Required.");
            valid = false;
        } else {
            frm_email.setError(null);
        }
        String password = frm_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            frm_password.setError("Required.");
            valid = false;
        } else {
            frm_password.setError(null);
            frm_password.setError(null);
        }
        return valid;
    }


    private void signInUser(String email, String password) {
        if (validateForm()) {
            loginAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = loginAuth.getCurrentUser();
                                logInUser(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                logInUser(null);
                            }
                        }
                    });
        }
    }
    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||
                !email.contains(".") ||
                email.length() < 5)
            return false;
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Revisa si el usuario existe
        FirebaseUser currentUser = loginAuth.getCurrentUser();
        logInUser(currentUser);
    }

    @Override
    public void onBackPressed() {

        Intent intent;
        intent = new Intent(getBaseContext(), LandingPage.class);
        startActivity(intent);
    }
}

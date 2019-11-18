package com.example.ratatouille.Chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.example.ratatouille.Class.Solicitud;
import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.ClientChef.LogIn;
import com.example.ratatouille.R;
import com.example.ratatouille.adapters.SolicitudAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChefActivity extends AppCompatActivity {
    Switch disponible;
    ImageView logOut;
    ImageView iconoChef;
    ListView listaSolicitud;
    Button btnr;

    DatabaseReference dbChefs;
    DatabaseReference dbNotifs;
    StorageReference storageChef;

    FirebaseAuth registerAuth;
    private List<Solicitud> listSolicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        final FirebaseStorage dbRatsStorage = FirebaseStorage.getInstance();
        registerAuth = FirebaseAuth.getInstance();
        dbChefs = dbRats.getReference("userChef");
        storageChef = dbRatsStorage.getReference();
        disponible = findViewById(R.id.Disponible);

        listaSolicitud = findViewById(R.id.ListSolicitud);

        logOut = findViewById(R.id.logOut2);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAuth.signOut();
                Intent intent = new Intent( getApplicationContext(), LogIn.class );
                startActivity(intent);
            }
        });

        btnr=findViewById(R.id.refreshi);
        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChefActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        iconoChef = findViewById(R.id.Icon);

        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();

        disponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dbChefs.child(userId).child("status").setValue(isChecked);
            }
        });
        FirebaseUser user = registerAuth.getCurrentUser();
        String uid= user.getUid();
        Log.i("CHEF", "URL " + uid);
        Query queryChefURL = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(uid);
        queryChefURL.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dir : dataSnapshot.getChildren()){
                        Log.i("SP",dir.getValue(UserChef.class).getPhotoDownloadURL());
                        cargarImagen(dir, dbRatsStorage);
                        disponible.setChecked(dir.getValue(UserChef.class).isStatus());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("LOGINFAILED","CHEF" );
            }
        });

        listSolicitudes = new ArrayList<>();
        Query soli = FirebaseDatabase.getInstance().getReference("solicitud").orderByChild("idChef").equalTo(uid);
        soli.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Solicitud noti = snapshot.getValue(Solicitud.class);
                        listSolicitudes.add(noti);
                    }
                }

                for(int i =0;i<listSolicitudes.size();i++)
                {
                    System.out.println(listSolicitudes.get(i).getIdCliente());
                }
                ArrayAdapter<Solicitud> adapter = new SolicitudAdapter(getApplicationContext(),listSolicitudes);
                listaSolicitud.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists())
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Solicitud noti = snapshot.getValue(Solicitud.class);
                    listSolicitudes.add(noti);
                }
            }

            for(int i =0;i<listSolicitudes.size();i++)
            {
                System.out.println(listSolicitudes.get(i).getIdCliente());
            }

            ArrayAdapter<Solicitud> adapter = new SolicitudAdapter(getApplicationContext(), listSolicitudes);
            listaSolicitud.setAdapter(adapter);
            listSolicitudes.clear();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private Bitmap cargarImagen(DataSnapshot dir, FirebaseStorage dbRatsStorage) {
        final Bitmap[] bitmap = {null};
        StorageReference sRf = dbRatsStorage.getReferenceFromUrl(dir.getValue(UserChef.class).getPhotoDownloadURL());
        try {
            final File localFile = File.createTempFile("images", "jpg");
            sRf.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    iconoChef.setImageBitmap(bitmap[0]);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return bitmap[0];
    }

    @Override
    public void onBackPressed() {

    }
}

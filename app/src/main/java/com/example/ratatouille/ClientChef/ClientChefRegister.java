package com.example.ratatouille.ClientChef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ratatouille.Class.UserChef;
import com.example.ratatouille.Class.UserClient;
import com.example.ratatouille.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.example.ratatouille.permissions.PermissionsActions;
import com.example.ratatouille.permissions.PermissionIds;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ClientChefRegister extends AppCompatActivity {

    EditText name;
    EditText age;
    EditText address;
    DatabaseReference dbChefs;
    DatabaseReference dbClients;
    StorageReference storageChefs;
    StorageReference storageClients;
    FirebaseAuth registerAuth;
    Button btnReg;
    ImageView agregarFoto;
    private static Uri imageUri = null;
    private Geocoder mGeocoder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register2);

        name=findViewById(R.id.etxtName);
        age = findViewById(R.id.etxtName2);
        btnReg=findViewById(R.id.btn_register4);
        address = findViewById(R.id.etxtName3);
        agregarFoto = findViewById(R.id.imageView4);

        FirebaseDatabase dbRats = FirebaseDatabase.getInstance();
        FirebaseStorage dbRatsStorage = FirebaseStorage.getInstance();

        dbChefs = dbRats.getReference("userChef");
        dbClients = dbRats.getReference("userClient");
        storageChefs = dbRatsStorage.getReference("images/userChef");
        storageClients = dbRatsStorage.getReference("images/userClient");

        registerAuth = FirebaseAuth.getInstance();

        mGeocoder = new Geocoder(this);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm())
                    registerChef();
            }
        });

        agregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirImagen();
            }
        });
    }

    private void subirImagen() {
        PermissionsActions.askPermission(this,PermissionIds.REQUEST_READ_EXTERNAL_STORAGE);
        seleccionarImagen();
    }

    private void seleccionarImagen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");
            startActivityForResult(pickImage, PermissionIds.IMAGE_PICKER_REQUEST);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PermissionIds.IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        agregarFoto.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PermissionIds.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
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
        if (imageUri==null) {
            Toast.makeText(getApplicationContext(), "No seas tímid@, sube una foto",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    void registerChef()
    {
        String name_value = name.getText().toString() ;
        String dir_value = address.getText().toString() ;


        int age_value = Integer.parseInt(age.getText().toString()) ;

        FirebaseUser currentUser = registerAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        LatLng pos = encontrarLatLng(dir_value);

        Intent intent = getIntent();

        if(intent.getStringExtra("type").equals("chef"))
        {
            UserChef user = new UserChef(name_value,dir_value,age_value);
            user.setUserId(userId);
            if(pos != null){
                user.setLat(pos.latitude);
                user.setLongi(pos.longitude);
            }
            dbChefs.child(userId).setValue(user);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            storageChefs.child(userId).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageChefs.child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            final String downloadUrl =
                                    uri.toString();
                            Log.i("URL+1", downloadUrl);
                            dbChefs.child(userId).child("photoDownloadURL").setValue(downloadUrl);
                            Intent in = new Intent(getApplicationContext(), ToolsRegister.class);
                            in.putExtra("type","chefsi");
                            startActivity(in);

                        }
                    });
                }
            });

        }
        else if (intent.getStringExtra("type").equals("client"))
        {
            UserClient user = new UserClient(name_value,dir_value,age_value);
            user.setUserId(userId);
            if(pos != null){
                user.setLat(pos.latitude);
                user.setLongi(pos.longitude);
            }
            dbClients.child(userId).setValue(user);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            storageClients.child(userId).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageClients.child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            final String downloadUrl =
                                    uri.toString();
                            Log.i("URL+1", downloadUrl);
                            dbClients.child(userId).child("photoDownloadURL").setValue(downloadUrl);
                            Intent in = new Intent(getApplicationContext(), ToolsRegister.class);
                            in.putExtra("type","clienti");
                            startActivity(in);
                        }
                    });
                }
            });
        }
    }

    private LatLng encontrarLatLng(String dir_value) {
        if(!dir_value.isEmpty()){
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(dir_value, 2);
                Log.i("Posicion", "Obteniendo");
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    LatLng result = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                    Log.i("LatLng",result.toString());
                    return result;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(PermissionsActions.checkPermission(this,PermissionIds.REQUEST_READ_EXTERNAL_STORAGE)){
            seleccionarImagen();
        }
    }
}
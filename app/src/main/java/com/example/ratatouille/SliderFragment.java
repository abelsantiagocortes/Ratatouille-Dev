package com.example.ratatouille;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class SliderFragment extends Fragment {
    View view;
    CircularImageView image_chef;
    RoundedImageView image_plate;
    TextView n_chef,loc_chef;
    FirebaseStorage dbRatsStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_c_slider,container,false);
        image_chef= view.findViewById(R.id.img_chef2);
        n_chef= view.findViewById(R.id.txt_nombreChef);
        image_plate=view.findViewById(R.id.img_chef);
        loc_chef= view.findViewById(R.id.txt_locationChef);
        dbRatsStorage = FirebaseStorage.getInstance();


        if(getArguments()!=null){
            n_chef.setText(getArguments().getString("NameChef"));
            loc_chef.setText(getArguments().getString("LocChef")+ " km");
            //Bitmap[] img = (Bitmap[]) getArguments().getSerializable("ImageChef");

            String id=getArguments().getString("ChefId");



            Query queryChefURL = FirebaseDatabase.getInstance().getReference("userChef").orderByChild("userId").equalTo(id);
            queryChefURL.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot dir : dataSnapshot.getChildren()){
                            Log.i("SP",dir.getValue(UserChef.class).getPhotoDownloadURL());
                            cargarImagen(dir, dbRatsStorage);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("LOGINFAILED","CHEF" );
                }
            });

            //image_chef.setImageBitmap(img[0]);
            image_plate.setImageResource(getArguments().getInt("ImagePlate"));
            image_plate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),ChefProfile.class);
                    String userId = getArguments().getString("ChefId");
                    intent.putExtra("ChefId",userId);
                    startActivity(intent);
                }
            });

        }

        return view;
    }

    private Bitmap cargarImagen(DataSnapshot dir, FirebaseStorage dbRatsStorage) {
        final Bitmap[] bitmap = {null};
        StorageReference sRf = dbRatsStorage.getReferenceFromUrl(dir.getValue(UserChef.class).getPhotoDownloadURL());
        try {
            final File localFile = File.createTempFile("images", "jpg");
            sRf.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    image_chef.setImageBitmap(bitmap[0]);

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
}

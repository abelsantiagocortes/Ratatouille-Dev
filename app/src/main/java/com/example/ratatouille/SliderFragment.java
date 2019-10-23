package com.example.ratatouille;

import android.graphics.Bitmap;
import android.os.Bundle;
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


public class SliderFragment extends Fragment {
    View view;
    CircularImageView image_chef;
    RoundedImageView image_plate;
    TextView n_chef,loc_chef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_c_slider,container,false);
        image_chef= view.findViewById(R.id.img_chef2);
        n_chef= view.findViewById(R.id.txt_nombreChef);
        image_plate=view.findViewById(R.id.img_chef);
        loc_chef= view.findViewById(R.id.txt_locationChef);

        if(getArguments()!=null){
            n_chef.setText(getArguments().getString("NameChef"));
            loc_chef.setText(getArguments().getString("LocChef")+ " km");
            Bitmap[] img = (Bitmap[]) getArguments().getSerializable("ImageChef");
            image_chef.setImageBitmap(img[0]);
            image_plate.setImageResource(getArguments().getInt("ImagePlate"));
            image_plate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });

        }

        return view;
    }
}

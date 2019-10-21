package com.example.ratatouille;

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

import com.github.siyamed.shapeimageview.RoundedImageView;


public class SliderFragment extends Fragment {
    View view;
    RoundedImageView image_chef;
    TextView n_chef,loc_chef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_c_slider,container,false);
        image_chef= view.findViewById(R.id.img_chef);
        n_chef= view.findViewById(R.id.txt_nombreChef);
        loc_chef= view.findViewById(R.id.txt_locationChef);
        RelativeLayout backgrond= view.findViewById(R.id.background);

        if(getArguments()!=null){
            /*n_chef.setText(getArguments().getString("NameChef"));
            loc_chef.setText(getArguments().getString("LocChef"));
            image_chef.setImageResource(getArguments().getInt("plate1_test.png"));
            */
        }

        return view;
    }
}

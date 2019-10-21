package com.example.ratatouille;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class Chefs_tab extends Fragment{

    private ViewPager viewPagerC;
    private MyViewPagerAdapter adapter;
    private LinearLayout dotsLayout;
    Button btn_back,btn_next;

    private String[]names={"chef1","chef2","chef3","chef4"};
    private String[]locations={"1km","10km","50km","8km"};
    private int[] images={R.drawable.chef,R.drawable.chef,R.drawable.chef,R.drawable.chef};
    @Override

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_chefs,container,false);

        viewPagerC=(ViewPager) view.findViewById(R.id.viewPagerC);

        dotsLayout=(LinearLayout) view.findViewById(R.id.LayoutDots);
        btn_back=(Button) view.findViewById(R.id.btn_back);
        btn_next=(Button) view.findViewById(R.id.btn_next);

        loadViewPager();


        return view;


    }
    private void loadViewPager(){
         adapter= new MyViewPagerAdapter(getFragmentManager());
         for(int i=0;i<images.length;i++){
            adapter.addFragment(newInstance(names[i],locations[i],images[i]));
         }

         viewPagerC.setAdapter(adapter);
    }
    private SliderFragment newInstance(String n_chef,String loc_chef,int image){
        Bundle bundle = new Bundle();
        bundle.putString("NameChef",n_chef);
        bundle.putString("LocChef",loc_chef);
        bundle.putInt("ImageChef",image);

        SliderFragment fragment=new SliderFragment();
        fragment.setArguments(bundle);
        return fragment;


    }


}

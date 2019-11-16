package com.example.ratatouille.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ratatouille.ClientChef.ClientChefDistance;
import com.example.ratatouille.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

public class ChefsAdapter  extends RecyclerView.Adapter<ChefsAdapter.MyViewHolder> {
    private List<ClientChefDistance> clientChefDistances;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        CircularImageView chefPhoto;
        TextView distance;
        TextView chefName;

        public MyViewHolder(LinearLayout v) {
            super(v);

            chefPhoto =  v.findViewById(R.id.imgChefRecipe);
            distance =  v.findViewById(R.id.txt_distance);
            chefName =  v.findViewById(R.id.txt_nameChef);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChefsAdapter(List<ClientChefDistance> imgs) {
        this.clientChefDistances = imgs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChefsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chefdistance, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
      //  holder.chefPhoto.setImageBitmap(clientChefDistances.get(position).getImgChef()[0]);
        holder.chefName.setText(clientChefDistances.get(position).getChefName());
        holder.distance.setText(String.valueOf(clientChefDistances.get(position).getDistance()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return clientChefDistances.size();
    }
}
package com.example.easyexchangeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavItemAdapter extends RecyclerView.Adapter<FavItemAdapter.ViewHolder> {

    private List<Product> favItems;

    public FavItemAdapter(List<Product> favItems) {
        this.favItems = favItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items_view,parent,false);
        ViewHolder  holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemPrice.setText(favItems.get(position).getProdPrice());

        holder.itemDescription.setText(favItems.get(position).getProdDescription());

        holder.itemAddress.setText(favItems.get(position).getProdAddress());

        Picasso.get().load(favItems.get(position)
                .getImageUrl())
                .fit()
                .centerInside()
                .into(holder.itemImage);
    }


    @Override
    public int getItemCount() {
        return favItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemPrice
                , itemDescription
                , itemAddress;
        ImageView itemImage, nextButton, bookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemAddress = itemView.findViewById(R.id.itemAddress);
            itemImage = itemView.findViewById(R.id.itemImage);
            nextButton = itemView.findViewById(R.id.nextButton);
            bookmark = itemView.findViewById(R.id.bookmark);
        }
    }
}

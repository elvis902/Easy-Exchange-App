package com.example.easyexchangeapp.Adapters;

import android.content.Context;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private List<Product> productsList;

    public ItemAdapter(List<Product> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.itemPrice.setText(productsList.get(position).getProdPrice());

        holder.itemDescription.setText(productsList.get(position).getProdDescription());

        holder.itemAddress.setText(productsList.get(position).getProdAddress());

        Picasso.get().load(productsList.get(position)
                .getImageUrl())
                .fit()
                .centerInside()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemPrice
                , itemDescription
                , itemAddress;
        ImageView itemImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemAddress = itemView.findViewById(R.id.itemAddress);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}

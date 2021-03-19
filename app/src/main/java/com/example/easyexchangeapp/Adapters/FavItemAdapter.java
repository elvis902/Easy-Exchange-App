package com.example.easyexchangeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;

import java.util.ArrayList;
import java.util.List;

public class FavItemAdapter extends RecyclerView.Adapter<FavItemAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    List<Product> favItems = new ArrayList<>();
    private final OnItemClickListener listener;

    public FavItemAdapter( OnItemClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_items_view,parent,false);
        ViewHolder  holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(favItems.get(position).getProdName());
        holder.itemDetail.setText(favItems.get(position).getProdDescription());
        holder.itemPrice.setText(favItems.get(position).getProdPrice());
        holder.bind(favItems.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return favItems.size();
    }

    public void setFavItems(List<Product> favItems) {
        this.favItems = favItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDetail;
        TextView itemPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.fav_itemNameTV);
            itemDetail = itemView.findViewById(R.id.fav_itemDescriptionTV);
            itemPrice = itemView.findViewById(R.id.fav_itemPriceTV);
        }

        public void bind(Product product, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }
    }
}

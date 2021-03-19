package com.example.easyexchangeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    String data1[], data2[], data3[];
    int images[];
    Context context;
    public ItemAdapter(Context ct, String[] price, String[] description, String[] address, int[] img){
        context = ct;
        data1 = price;
        data2 = description;
        data3 = address;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.home_items_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemPrice.setText(data1[position]);
        holder.itemDescription.setText(data2[position]);
        holder.itemAddress.setText(data3[position]);
        holder.itemImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemPrice, itemDescription, itemAddress;
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

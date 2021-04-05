package com.example.easyexchangeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private List<Product> productsList;
    private onItemClickedListener mListener;
    private Context context;

    public ItemAdapter(List<Product> productsList, onItemClickedListener mListener, Context context) {
        this.productsList = productsList;
        this.mListener = mListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view,mListener);
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
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBookmarks(productsList.get(position));
                Toast.makeText(context, "Bookmarked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    static public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView itemPrice
                , itemDescription
                , itemAddress;
        ImageView itemImage, nextButton, bookmark;
        onItemClickedListener itemClicked;

        public MyViewHolder(@NonNull View itemView, onItemClickedListener itemClicked) {
            super(itemView);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemAddress = itemView.findViewById(R.id.itemAddress);
            itemImage = itemView.findViewById(R.id.itemImage);
            nextButton = itemView.findViewById(R.id.nextButton);
            bookmark = itemView.findViewById(R.id.bookmark);
            this.itemClicked = itemClicked;

            nextButton.setOnClickListener(this);
            bookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClicked.onNextClicked(getAdapterPosition());
        }
    }

    public interface onItemClickedListener{

        void onNextClicked(int position);

    }


    private void addToBookmarks(Product item) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("bookmarks");
        databaseReference.child(item.getItemKey()).setValue(item.getItemKey());
    }


}

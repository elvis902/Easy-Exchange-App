package com.example.easyexchangeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavItemAdapter extends RecyclerView.Adapter<FavItemAdapter.ViewHolder> {

    private List<Product> favItems;
    private Context mContext;
    private OnFavItemClickListener clickListener;

    public FavItemAdapter(List<Product> favItems, Context mContext, OnFavItemClickListener clickListener) {
        this.favItems = favItems;
        this.mContext = mContext;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_fragment_item_layout,parent,false);
        ViewHolder  holder = new ViewHolder(view,clickListener);
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBookmark(favItems.get(position).getItemKey());
                Toast.makeText(mContext, "Bookmark Delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteBookmark(String itemKey) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("bookmarks");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String snapKey = (String) snap.getValue();
                    if(snapKey.equals(itemKey)){
                        snap.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);
    }


    @Override
    public int getItemCount() {
        return favItems.size();
    }


   static  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemPrice
                , itemDescription
                , itemAddress;
        ImageView itemImage, nextButton, delete;
        OnFavItemClickListener mListener;

        public ViewHolder(@NonNull View itemView, OnFavItemClickListener mListener) {
            super(itemView);
            itemPrice = itemView.findViewById(R.id.favFrag_itemPrice);
            itemDescription = itemView.findViewById(R.id.favFrag_itemDescription);
            itemAddress = itemView.findViewById(R.id.favFrag_itemAddress);
            itemImage = itemView.findViewById(R.id.favFrag_itemImage);
            nextButton = itemView.findViewById(R.id.favFrag_nextButton);
            delete = itemView.findViewById(R.id.favFrag_deleteBookmark);
            this.mListener = mListener;
            nextButton.setOnClickListener(this);
        }

       @Override
       public void onClick(View v) {
           mListener.onFavClicked(getAdapterPosition());
       }
   }

   public interface OnFavItemClickListener{
        void onFavClicked(int position);
   }

}

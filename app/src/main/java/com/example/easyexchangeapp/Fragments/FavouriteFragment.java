package com.example.easyexchangeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangeapp.Activity.ProductDetails;
import com.example.easyexchangeapp.Adapters.FavItemAdapter;
import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private RecyclerView favItemsRV;
    private FavItemAdapter favItemAdapter;
    private List<Product> favItemsList = new ArrayList<>();
    private List<String> favItemKeyList = new ArrayList<>();

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_favourite, container, false);
        favItemsRV = v.findViewById(R.id.favFragment_RV);

        //Firebase initializations
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Getting the keys of the bookmarked items
        getFavItemKeys();

        //Getting the fav items
        getFavItems();

        //Putting into the RV
        favItemAdapter = new FavItemAdapter(favItemsList,getContext());
        favItemAdapter.notifyDataSetChanged();
        favItemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        favItemsRV.hasFixedSize();
        favItemsRV.setAdapter(favItemAdapter);


        return v;

    }

    private void getFavItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.STORAGE_LOCATION);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favItemsList.clear();
                String key;
                for(DataSnapshot itemSnap : snapshot.getChildren()){
                    Product tempProduct = itemSnap.getValue(Product.class);
                    key = tempProduct.getItemKey();
                    if(favItemKeyList.contains(key))
                        favItemsList.add(tempProduct);
                }
                favItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(eventListener);
        System.out.print("Size - "+favItemsList.size());
    }

    private void getFavItemKeys() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("bookmarks");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favItemKeyList.clear();
                String tempItemKey;

                for(DataSnapshot keySnap : snapshot.getChildren()){
                    tempItemKey = (String) keySnap.getValue();
                    favItemKeyList.add(tempItemKey);
                    System.out.println( "---- "+ tempItemKey + " ---- \n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "An unexpected error occurred!", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addListenerForSingleValueEvent(eventListener);

    }
}
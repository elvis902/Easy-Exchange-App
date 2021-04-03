package com.example.easyexchangeapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.Adapters.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment {

    private RecyclerView homeRV;
    private DatabaseReference reference;
    private ItemAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private ValueEventListener eventListener;

    public HomeFragment(){
        //Required Empty Public Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout =  inflater.inflate(R.layout.fragment_main, container, false);

        homeRV = (RecyclerView) fragmentLayout.findViewById(R.id.home_RV);

        reference = FirebaseDatabase.getInstance().getReference(Constants.STORAGE_LOCATION);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot prodSnap : snapshot.getChildren()){
                    Product tempModel = prodSnap.getValue(Product.class);
                    productList.add(tempModel);
                    System.out.print("TEST: "+tempModel.getItemKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        };
        reference.addListenerForSingleValueEvent(eventListener);

        adapter = new ItemAdapter(productList);
        homeRV.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRV.hasFixedSize();
        homeRV.setAdapter(adapter);

        return fragmentLayout;
    }
}
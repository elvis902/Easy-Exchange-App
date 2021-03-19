package com.example.easyexchangeapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.easyexchangeapp.Adapters.FavItemAdapter;
import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    RecyclerView favItemsRV;
    List<Product> favItemsList = new ArrayList<>();

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_favourite, container, false);

        favItemsRV = (RecyclerView) v.findViewById(R.id.favFragment_RV);
        favItemsRV.hasFixedSize();

        favItemsList.add(new Product("Water Resistance Travel Bag","PG Hostel, NITS","1999"));
        favItemsList.add(new Product("Water Resistance Travel Bag","PG Hostel, NITS","1999"));
        favItemsList.add(new Product("Water Resistance Travel Bag","PG Hostel, NITS","1999"));
        favItemsList.add(new Product("Water Resistance Travel Bag","PG Hostel, NITS","1999"));

        FavItemAdapter adapter = new FavItemAdapter();
        adapter.setFavItems(favItemsList);
        favItemsRV.setLayoutManager(new LinearLayoutManager(v.getContext()));
        favItemsRV.hasFixedSize();
        favItemsRV.setAdapter(adapter);
        return v;

    }
}
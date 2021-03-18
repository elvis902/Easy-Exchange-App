package com.example.easyexchangeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easyexchangeapp.myAdapter.ItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;

    String price[] = {"569", "Rs. 4588", "Rs. 8970", "Rs. 1999", "169", "123", "458", "897", "794", "169"},
            description[] = {"Water Resistance Travel Bag", "Asus Zenfone Max", "Comfy Double Bed", "Oneplus Bullet Wireless", "mno", "abc", "def", "ghi", "jkl", "mno"},
            address[] = {"PG Hostel, NITS", "Hostel 2, NITS", "Dean Office, NITS", "Hostel 7, NITS", "uegieoihg", "qwe", "rtyy", "uiop", "gaiwhgafdf", "uegieoihg"};
    int images[] = {R.drawable.bag, R.drawable.phone, R.drawable.bed,
            R.drawable.hp, R.drawable.pen, R.drawable.pen,
            R.drawable.bag, R.drawable.bag, R.drawable.bag,
            R.drawable.pen};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ItemAdapter itemAdapter;
//        itemAdapter = new ItemAdapter(getContext(),price, description, address, images);
//        recyclerView = recyclerView.findViewById(R.id.home_list_item);
//        recyclerView.setAdapter(itemAdapter);
//        recyclerView.setLayoutManager(layoutManager);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
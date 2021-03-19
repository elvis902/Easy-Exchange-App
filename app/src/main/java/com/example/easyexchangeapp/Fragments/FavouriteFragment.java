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

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_favourite, container, false);

        return v;

    }
}
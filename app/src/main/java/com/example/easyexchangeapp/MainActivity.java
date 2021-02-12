package com.example.easyexchangeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.myAdapter.ItemAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    String price[] = {"569", "Rs. 4588", "Rs. 8970", "Rs. 1999", "169", "123", "458", "897", "794", "169"},
            description[] = {"Water Resistance Travel Bag", "Asus Zenfone Max", "Comfy Double Bed", "Oneplus Bullet Wireless", "mno", "abc", "def", "ghi", "jkl", "mno"},
            address[] = {"PG Hostel, NITS", "Hostel 2, NITS", "Dean Office, NITS", "Hostel 7, NITS", "uegieoihg", "qwe", "rtyy", "uiop", "gaiwhgafdf", "uegieoihg"};
    int images[] = {R.drawable.bag,R.drawable.phone,R.drawable.bed,
            R.drawable.hp,R.drawable.pen,R.drawable.pen,
            R.drawable.bag,R.drawable.bag,R.drawable.bag,
            R.drawable.pen};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //price = getResources().getStringArray(R.array.price);
        //description = getResources().getStringArray(R.array.description);
        //address = getResources().getStringArray(R.array.address);

        ItemAdapter itemAdapter = new ItemAdapter(this, price, description, address, images);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
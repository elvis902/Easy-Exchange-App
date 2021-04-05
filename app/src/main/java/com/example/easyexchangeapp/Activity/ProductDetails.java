package com.example.easyexchangeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {

    private TextView prodName, prodDescription, prodPrice, prodAddress;
    private ImageView prodImage;
    private Button chatBuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        prodName = findViewById(R.id.detail_itemName);
        prodDescription = findViewById(R.id.detail_itemDescription);
        prodPrice = findViewById(R.id.detail_itemPrice);
        prodAddress = findViewById(R.id.detail_itemAddress);
        prodImage = findViewById(R.id.detail_itemImage);
        chatBuyButton=findViewById(R.id.button);

        Product item = (Product) getIntent().getSerializableExtra("item_bundle");

        assert item != null;

        displayInfo(item);

        chatBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra(Constants.USER_ID,item.getSellerID());
                startActivity(intent);
            }
        });
    }

    private void displayInfo(Product item){
        prodName.setText(item.getProdName());
        prodAddress.setText(item.getProdAddress());
        prodPrice.setText(item.getProdPrice());
        prodDescription.setText(item.getProdDescription());
        Picasso.get().
                load(item.getImageUrl()).
                centerInside().
                fit().
                into(prodImage);
    }
}
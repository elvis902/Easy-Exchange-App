package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDetails extends AppCompatActivity {

    private TextView prodName, prodDescription, prodPrice, prodAddress;
    private ImageView prodImage;
    private Button chatBuyButton;
    private DatabaseReference databaseReference;
    private String clientId,userId;
    private String sellerName;
    private String chatRoomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");


        prodName = findViewById(R.id.detail_itemName);
        prodDescription = findViewById(R.id.detail_itemDescription);
        prodPrice = findViewById(R.id.detail_itemPrice);
        prodAddress = findViewById(R.id.detail_itemAddress);
        prodImage = findViewById(R.id.detail_itemImage);
        chatBuyButton=findViewById(R.id.button);

        Product item = (Product) getIntent().getSerializableExtra("item_bundle");
        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());
        userId=manager.getValue(Constants.USER_ID);
        clientId=item.getSellerID();
        if(clientId.equals(userId)){
            chatBuyButton.setVisibility(View.INVISIBLE);
        }
        sellerName=item.getSellerName();
        assert item != null;

        displayInfo(item);

        chatBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRoomAvailability();
            }
        });
    }
    private void checkRoomAvailability(){
        DatabaseReference chatReference=databaseReference.child(userId).child("chatRooms");
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> chatRoomIds=new HashMap<>();
                for(DataSnapshot temp: snapshot.getChildren()){
                    chatRoomIds.put(temp.getKey(),temp.getKey());
                }
                if(!chatRoomIds.containsKey(userId+"**"+clientId)&&!chatRoomIds.containsKey(clientId+"**"+userId)){
                    System.out.println("TEST: Needs to be Created!");
                    chatRoomId=userId+"**"+clientId;
                    chatReference.child(chatRoomId).setValue(chatRoomId);
                    databaseReference.child(clientId).child("chatRooms").child(chatRoomId).setValue(chatRoomId);
                }else if (chatRoomIds.containsKey(userId+"**"+clientId)){
                    System.out.println("TEST: Already created!");
                    chatRoomId=userId+"**"+clientId;
                }else if(chatRoomIds.containsKey(clientId+"**"+userId)){
                    System.out.println("TEST: Already created!");
                    chatRoomId=clientId+"**"+userId;
                }
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("chat-room",chatRoomId);
                intent.putExtra("client",sellerName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error creating chat room!",Toast.LENGTH_LONG).show();
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
package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.ChatModel;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String clientId,userId;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());

        Intent intent=getIntent();
        clientId=intent.getStringExtra(Constants.USER_ID);
        userId=manager.getValue(Constants.USER_ID);


    }

    private String checkRoomAvailability(){
        DatabaseReference chatReference=databaseReference.child(userId).child("chat-rooms");
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot temp: snapshot.getChildren()){
                    if(!temp.hasChild(userId+"**"+clientId)&&!temp.hasChild(clientId+"**"+userId)){
                        chatReference.child(userId+"**"+clientId);
                        databaseReference.child(clientId).child("chat-rooms").child(userId+"**"+clientId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return "";
    }

}
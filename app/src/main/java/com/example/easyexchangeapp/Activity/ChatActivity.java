package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangeapp.Adapters.ChatAdapter;
import com.example.easyexchangeapp.Adapters.ItemAdapter;
import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.ChatModel;
import com.example.easyexchangeapp.NotificationManagerFiles.NotificationServiceClass;
import com.example.easyexchangeapp.Models.RegisterUser;

import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference usernameRef;
    private String clientName,userName,chatRoomId,userId;
    private DatabaseReference databaseReference;
    private RecyclerView chatRV;
    private EditText messageBody;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private Class<? extends DatabaseReference> receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRV=findViewById(R.id.chatRecyclerView);
        messageBody=findViewById(R.id.chatMessageText);
        sendButton=findViewById(R.id.chatSendButton);

        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());


        Intent intent=getIntent();
        clientName=intent.getStringExtra("client");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(clientName);
        
        userName=manager.getValue(Constants.USER_NAME);
        chatRoomId=intent.getStringExtra("chat-room");
        userId=manager.getValue(Constants.USER_ID);
        chatAdapter = new ChatAdapter(getApplicationContext());
        chatRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatRV.hasFixedSize();
        chatRV.setAdapter(chatAdapter);


        usernameRef= FirebaseDatabase.getInstance().getReference().child("Users");
        usernameRef.child(clientName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot tempSnap: snapshot.getChildren()){
                    if(tempSnap.getKey().equals("userName")){
                        setTitle(tempSnap.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        databaseReference= FirebaseDatabase.getInstance().getReference().child("Chat-Rooms").child(chatRoomId);
        databaseReference.child("search").setValue(chatRoomId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ChatModel> chatList=new ArrayList<>();
                for(DataSnapshot temp: snapshot.getChildren()){
                    if(temp.getKey().equals("search")||temp.getKey().equals("sender")){
                        continue;
                    }else{
                        chatList.add(temp.getValue(ChatModel.class));
                    }
                }
                chatAdapter.setChatModelList(chatList);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error getting messages!",Toast.LENGTH_LONG).show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageBody.getText().toString();
                if(!message.equals("")){
                    databaseReference.child("sender").setValue(userId);
                    databaseReference.push().setValue(new ChatModel(userName,message,userId));
                    messageBody.setText("");
                }
            }
        });
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent notificationServiceIntent=new Intent(this, NotificationServiceClass.class);
        notificationServiceIntent.putExtra("temp",chatRoomId);
        startService(notificationServiceIntent);
    }

}
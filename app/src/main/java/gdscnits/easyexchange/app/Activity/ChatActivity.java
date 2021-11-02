package gdscnits.easyexchange.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import gdscnits.easyexchange.app.Adapters.ChatAdapter;
import gdscnits.easyexchange.app.Constants.Constants;
import gdscnits.easyexchange.app.Models.ChatModel;
import gdscnits.easyexchange.app.NotificationManagerFiles.NotificationServiceClass;
import com.example.easyexchangeapp.R;
import gdscnits.easyexchange.app.SharedPrefManager.SharedPrefManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference usernameRef;
    private String clientName,userName,chatRoomId,userId;
    private DatabaseReference databaseReference;
    private RecyclerView chatRV;
    private EditText messageBody;
    private ImageView sendButton;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatModel> chatList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        chatRV=findViewById(R.id.chatRecyclerView);
        messageBody=findViewById(R.id.chatMessageText);
        sendButton=findViewById(R.id.chatSendButton);



        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());

        Intent intent=getIntent();
        clientName=intent.getStringExtra("client");
        userName=manager.getValue(Constants.USER_NAME);
        chatRoomId=intent.getStringExtra("chat-room");
        userId=manager.getValue(Constants.USER_ID);
        chatAdapter = new ChatAdapter(getApplicationContext(),chatList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        chatRV.setLayoutManager(llm);
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
                chatList.clear();
                for(DataSnapshot temp: snapshot.getChildren()){
                    if(temp.getKey().equals("search")||temp.getKey().equals("sender")){
                        continue;
                    }else{
                        chatList.add(temp.getValue(ChatModel.class));
                    }
                }
                chatRV.scrollToPosition(chatList.size()-1);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
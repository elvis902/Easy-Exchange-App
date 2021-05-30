package com.example.easyexchangeapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.Activity.ChatActivity;
import com.example.easyexchangeapp.Models.MyChatRecord;
import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyConversations extends RecyclerView.Adapter<MyConversations.MyViewHolder> {
    DatabaseReference usernameRef;
    List<MyChatRecord> chatRecordList=new ArrayList<>();
    private Context context;

    public MyConversations(Context context){
        this.context=context;
    }

    public void setChatRecordList(List<MyChatRecord> chatRecordList) {
        this.chatRecordList = chatRecordList;
        usernameRef= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyConversations.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_record_layout,parent,false);
        MyConversations.MyViewHolder holder = new MyConversations.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyConversations.MyViewHolder holder, int position) {
        MyChatRecord currentUser=chatRecordList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchChatActivity(currentUser.getChatRoomID(),currentUser.getSender());
            }
        });
        usernameRef.child(currentUser.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot tempSnap: snapshot.getChildren()){
                    if(tempSnap.getKey().equals("userName")){
                        holder.profName.setText(tempSnap.getValue(String.class));
                    }else if(tempSnap.getKey().equals("profile_image")){
                        Picasso.get().load(tempSnap.getValue(String.class))
                                .fit()
                                .centerInside()
                                .into(holder.profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRecordList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView profName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg=itemView.findViewById(R.id.chatRecord_profImg);
            profName=itemView.findViewById(R.id.chatRecord_Name);
        }
    }

    private void launchChatActivity(String chatRoomId,String clientName){
        Intent intent=new Intent(context,ChatActivity.class);
        intent.putExtra("chat-room",chatRoomId);
        intent.putExtra("client",clientName);
        context.startActivity(intent);
    }
}
package com.example.easyexchangeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.ChatModel;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    List<ChatModel> chatModelList=new ArrayList<>();
    Context context;
    String currentUID;

    public ChatAdapter(Context context,List<ChatModel> chatModelList){
        this.context=context;
        this.chatModelList=chatModelList;
        SharedPrefManager manager=new SharedPrefManager(context);
        currentUID=manager.getValue(Constants.USER_ID);
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout,parent,false);
        ChatAdapter.MyViewHolder holder = new ChatAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatModel currentChat=chatModelList.get(position);
        if(currentUID.equals(currentChat.getSenderID())){
            holder.senderMessage.setText(currentChat.getMessage());
            holder.senderName.setText(currentChat.getSender());
            holder.clientMessage.setText("");
            holder.clientName.setText("");
        }else{
            holder.senderMessage.setText("");
            holder.senderName.setText("");
            holder.clientMessage.setText(currentChat.getMessage());
            holder.clientName.setText(currentChat.getSender());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView clientName, clientMessage, senderName,senderMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName=itemView.findViewById(R.id.chat_clientName);
            clientMessage=itemView.findViewById(R.id.chat_clientMessage);
            senderName=itemView.findViewById(R.id.chat_senderName);
            senderMessage=itemView.findViewById(R.id.chat_senderMessage);
        }

    }
}
